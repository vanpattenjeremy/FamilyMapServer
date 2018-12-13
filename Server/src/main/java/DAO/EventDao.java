package DAO;

import com.google.gson.internal.bind.SqlDateTypeAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Exceptions.DoesNotBelongException;
import Exceptions.InternalServerException;
import Exceptions.InvalidIDException;
import Model.Event;

public class EventDao extends DaoInterface{

    public EventDao(Connection conn)
    {
        super(conn);
    }

    public void createEvent(Event newEvent) throws InternalServerException
    {
        try{
            String sql = "INSERT INTO Events(descendant,event_id,person_id,latitude,longitude,country,city,event_type,year) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.setString(1,newEvent.getDescendant());
            stmt.setString(2,newEvent.getEvent_id());
            stmt.setString(3,newEvent.getPerson_id());
            stmt.setFloat(4,newEvent.getLatitude());
            stmt.setFloat(5,newEvent.getLongitude());
            stmt.setString(6,newEvent.getCountry());
            stmt.setString(7,newEvent.getCity());
            stmt.setString(8,newEvent.getEvent_type());
            stmt.setInt(9,newEvent.getYear());

            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to add event to the Events table");
        }
    }

    public ArrayList<Event> getEvents(String username) throws InternalServerException
    {
        try{
            String sql = "SELECT * FROM Events WHERE descendant = ?";
            PreparedStatement stmt = getConn().prepareStatement(sql);

            stmt.setString(1,username);

            ResultSet rs = stmt.executeQuery();

            ArrayList<Event> events = new ArrayList<Event>();

            while(rs.next())
            {
                String event_id = rs.getString("event_id");
                String person_id = rs.getString("person_id");
                float latitude = rs.getFloat("latitude");
                float longitude = rs.getFloat("longitude");
                String country = rs.getString("country");
                String city = rs.getString("city");
                String event_type = rs.getString("event_type");
                int year = rs.getInt("year");
                Event e = new Event(username, event_id, person_id, latitude, longitude, country, city, event_type, year);

                events.add(e);
            }

            return events;
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to get Events from table");
        }
    }

    public Event getOneEvent(String username, String event_id)
            throws InternalServerException, InvalidIDException, DoesNotBelongException
    {
        try{
            String sql = "SELECT * FROM Events WHERE event_id = ?";
            PreparedStatement stmt = getConn().prepareStatement(sql);

            stmt.setString(1,event_id);

            ResultSet rs = stmt.executeQuery();
            if(!rs.next())
            {
                throw new InvalidIDException("Could not find event_id");
            }
            String eventUser = rs.getString("descendant");
            if(!username.equals(eventUser))
            {
                throw new DoesNotBelongException("That event does not belong to the current user");
            }

            String person_id = rs.getString("person_id");
            float latitude = rs.getFloat("latitude");
            float longitude = rs.getFloat("longitude");
            String country = rs.getString("country");
            String city = rs.getString("city");
            String event_type = rs.getString("event_type");
            int year = rs.getInt("year");
            return new Event(username, event_id, person_id, latitude, longitude, country, city, event_type, year);
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to get Event from table");
        }
    }

    public void clearTable() throws InternalServerException
    {
        try{
            String sql = "DELETE FROM Events";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to clear Events table");
        }
    }

    public void clearTable(String username) throws InternalServerException
    {
        try{
            String sql = "DELETE FROM Events WHERE descendant = ?";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.setString(1,username);
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to delete events for the given user");
        }
    }
}
