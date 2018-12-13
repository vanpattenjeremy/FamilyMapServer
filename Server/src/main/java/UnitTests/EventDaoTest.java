package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import DAO.Database;
import DAO.EventDao;
import Exceptions.InternalServerException;
import Model.Event;

import static org.junit.Assert.assertEquals;

public class EventDaoTest {

    @Test
    public void testGetEvents()
    {
        Database database = null;
        String newID = null;
        try{
            System.out.println("Testing get events on empty Events table");
            database = new Database();

            EventDao eventDao = new EventDao(database.openConnection());
            eventDao.clearTable();
            eventDao.getEvents("Jeremy");

            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            assertEquals("Failed to get Events from table",e.getMessage());
        }

        System.out.println("Passed");

        try{
            System.out.println("Testing get events with a single event in the Events table");
            addEventToDatabaseHelper(database);
            EventDao eventDao = new EventDao(database.openConnection());
            ArrayList<Event> events = eventDao.getEvents("Jeremy");
            database.closeConnection(true);
            assertEquals(1,events.size());
            Event event = events.get(0);
            assertEquals("Jeremy", event.getDescendant());
            assertEquals("blah",event.getEvent_id());
            assertEquals("bleh",event.getPerson_id());
            assertEquals(10.0,event.getLatitude(),0);
            assertEquals(10.0,event.getLongitude(),0);
            assertEquals("USA",event.getCountry());
            assertEquals("provo", event.getCity());
            assertEquals("born", event.getEvent_type());
            assertEquals(2000,event.getYear());

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }


        System.out.println("Passed");

        try {
            System.out.println("Testing get multiple events");
            addMultipleEventsToDatabaseHelper(database);
            EventDao eventDao = new EventDao(database.openConnection());
            ArrayList<Event> events = eventDao.getEvents("Jeremy");
            database.closeConnection(true);
            assertEquals(2,events.size());
            Event event = events.get(0);
            assertEquals("Jeremy", event.getDescendant());
            assertEquals("blah",event.getEvent_id());
            assertEquals("bleh",event.getPerson_id());
            assertEquals(10.0,event.getLatitude(),0);
            assertEquals(10.0,event.getLongitude(),0);
            assertEquals("USA",event.getCountry());
            assertEquals("provo", event.getCity());
            assertEquals("born", event.getEvent_type());
            assertEquals(2000,event.getYear());
            event = events.get(1);
            assertEquals("Jeremy", event.getDescendant());
            assertEquals("meh",event.getEvent_id());
            assertEquals("moh",event.getPerson_id());
            assertEquals(20.0,event.getLatitude(),0);
            assertEquals(20.0,event.getLongitude(),0);
            assertEquals("Russia",event.getCountry());
            assertEquals("siberia", event.getCity());
            assertEquals("died", event.getEvent_type());
            assertEquals(2010,event.getYear());
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");
    }
    @Test
    public void testGetOneEvent()
    {
        Database database = null;
        String newId = null;
        try {
            System.out.println("Testing getting one event on empty table (Event does not exist)");
            database = new Database();

            EventDao eventDao = new EventDao(database.openConnection());
            eventDao.clearTable();
            eventDao.getOneEvent("Jeremy","blah");

            database.closeConnection(true);
        }
        catch(Exception e)
        {
            assertEquals("Could not find event_id", e.getMessage());
        }

        System.out.println("Passed");

        try{
            database.closeConnection(true);
            System.out.println("Testing getting one event on a table with the existing event and correct username");
            addEventToDatabaseHelper(database);

            EventDao eventDao = new EventDao(database.openConnection());
            Event event = eventDao.getOneEvent("Jeremy","blah");
            database.closeConnection(true);

            assertEquals("Jeremy", event.getDescendant());
            assertEquals("blah",event.getEvent_id());
            assertEquals("bleh",event.getPerson_id());
            assertEquals(10.0,event.getLatitude(),0);
            assertEquals(10.0,event.getLongitude(),0);
            assertEquals("USA",event.getCountry());
            assertEquals("provo", event.getCity());
            assertEquals("born", event.getEvent_type());
            assertEquals(2000,event.getYear());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");

        try{
            System.out.println("Testing getting one event on a table with the correct event_id, but incorrect username");
            EventDao eventDao = new EventDao(database.openConnection());
            Event event = eventDao.getOneEvent("Bob", "blah");

            database.closeConnection(true);

            System.out.println("Got and event for Bob when none should exist");
            Assert.fail();
        }
        catch(Exception e)
        {
            assertEquals("That event does not belong to the current user", e.getMessage());
        }

        System.out.println("Passed");
        try{
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            System.out.println("Could not close database");
            Assert.fail();
        }


    }

    @Test
    public void testCreateEvent()
    {
        Database database = null;
        String newToken = null;
        try{
            System.out.println("Testing adding a new Event");
            database = new Database();
            EventDao EventDao = new EventDao(database.openConnection());
            EventDao.clearTable();

            String descendant = "Jeremy";
            String event_id = "blah";
            String person_id = "bleh";
            float latitude = (float) 10.0;
            float longitude = (float) 10.0;
            String country = "USA";
            String city = "provo";
            String event_type = "born";
            int year = 2000;
            Event event = new Event(descendant,event_id,person_id,latitude,longitude,country,city,event_type,year);
            EventDao.createEvent(event);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        try{
            EventDao eventDao = new EventDao(database.openConnection());
            ArrayList<Event> events = eventDao.getEvents("Jeremy");
            database.closeConnection(true);
            Event event = events.get(0);
            assertEquals("Jeremy", event.getDescendant());
            assertEquals("blah",event.getEvent_id());
            assertEquals("bleh",event.getPerson_id());
            assertEquals(10.0,event.getLatitude(),0);
            assertEquals(10.0,event.getLongitude(),0);
            assertEquals("USA",event.getCountry());
            assertEquals("provo", event.getCity());
            assertEquals("born", event.getEvent_type());
            assertEquals(2000,event.getYear());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");

        try{
            System.out.println("Testing adding an event with same information, but different event_id");

            EventDao eventDao = new EventDao(database.openConnection());

            String descendant = "Jeremy";
            String event_id = "meh";
            String person_id = "bleh";
            float latitude = (float) 10.0;
            float longitude = (float) 10.0;
            String country = "USA";
            String city = "provo";
            String event_type = "born";
            int year = 2000;
            Event event = new Event(descendant,event_id,person_id,latitude,longitude,country,city,event_type,year);
            eventDao.createEvent(event);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        try{
            EventDao eventDao = new EventDao(database.openConnection());
            ArrayList<Event> events = eventDao.getEvents("Jeremy");
            database.closeConnection(true);
            Event event = events.get(1);
            assertEquals("Jeremy", event.getDescendant());
            assertEquals("meh",event.getEvent_id());
            assertEquals("bleh",event.getPerson_id());
            assertEquals(10.0,event.getLatitude(),0);
            assertEquals(10.0,event.getLongitude(),0);
            assertEquals("USA",event.getCountry());
            assertEquals("provo", event.getCity());
            assertEquals("born", event.getEvent_type());
            assertEquals(2000,event.getYear());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");

        try{
            System.out.println("Testing adding an event with different information, but same event_id");
            EventDao eventDao = new EventDao(database.openConnection());

            String descendant = "Bob";
            String event_id = "blah";
            String person_id = "moh";
            float latitude = (float) 20.0;
            float longitude = (float) 20.0;
            String country = "Russia";
            String city = "Siberia";
            String event_type = "died";
            int year = 2010;
            Event event = new Event(descendant,event_id,person_id,latitude,longitude,country,city,event_type,year);
            eventDao.createEvent(event);
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            assertEquals("Failed to add event to the Events table",e.getMessage());
        }

        try {
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");
    }

    @Test
    public void testClearTable()
    {
        try{
            System.out.println("Testing clear on Events table with data");
            Database database = new Database();
            addEventToDatabaseHelper(database);

            EventDao eventDao = new EventDao(database.openConnection());
            eventDao.clearTable();

            System.out.println("Passed");

            System.out.println("Testing clear on empty Events table");
            eventDao.clearTable();
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");
    }

    private void addEventToDatabaseHelper(Database database) throws InternalServerException
    {
        EventDao eventDao = new EventDao(database.openConnection());
        eventDao.clearTable();
        String descendant = "Jeremy";
        String event_id = "blah";
        String person_id = "bleh";
        float latitude = (float) 10.0;
        float longitude = (float) 10.0;
        String country = "USA";
        String city = "provo";
        String event_type = "born";
        int year = 2000;
        Event event = new Event(descendant,event_id,person_id,latitude,longitude,country,city,event_type,year);
        eventDao.createEvent(event);
        database.closeConnection(true);
    }

    private void addMultipleEventsToDatabaseHelper(Database database) throws InternalServerException
    {
        EventDao eventDao = new EventDao(database.openConnection());
        eventDao.clearTable();
        String descendant = "Jeremy";
        String event_id = "blah";
        String person_id = "bleh";
        float latitude = (float) 10.0;
        float longitude = (float) 10.0;
        String country = "USA";
        String city = "provo";
        String event_type = "born";
        int year = 2000;
        Event event = new Event(descendant,event_id,person_id,latitude,longitude,country,city,event_type,year);
        eventDao.createEvent(event);
        String descendant1 = "Jeremy";
        String event_id1 = "meh";
        String person_id1 = "moh";
        float latitude1 = (float) 20.0;
        float longitude1 = (float) 20.0;
        String country1 = "Russia";
        String city1 = "siberia";
        String event_type1 = "died";
        int year1 = 2010;
        Event event1 = new Event(descendant1,event_id1,person_id1,latitude1,longitude1,country1,city1,event_type1,year1);
        eventDao.createEvent(event1);
        database.closeConnection(true);
    }
}
