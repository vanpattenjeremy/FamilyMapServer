package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import DAO.AuthTokenDao;
import DAO.Database;
import DAO.EventDao;
import Model.AuthToken;
import Model.Event;
import Request.EventsRequest;
import Request.OneEventRequest;
import Result.EventsResult;
import Result.OneEventResult;
import Service.ClearService;
import Service.EventService;

import static org.junit.Assert.assertEquals;

public class EventTest {

    @Test
    public void testGetEvents()
    {
        try {
            System.out.println("Testing getEvents on an empty Events table");
            ClearService clearService = new ClearService();
            clearService.clear();
            Database database = new Database();
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());
            AuthToken at = new AuthToken("blah","Jeremy");
            authTokenDao.addAuthToken(at);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        EventsRequest request = new EventsRequest("blah");
        EventService eventService = new EventService();
        EventsResult result = eventService.getEvents(request);
        assertEquals("No events found for that user", result.getMessage());
        System.out.println("Passed");

        try{
            System.out.println("Testing getEvents on an Events table with one event");
            Database database = new Database();
            EventDao eventDao = new EventDao(database.openConnection());
            Event e = new Event("Jeremy", "blah", "blah", (float)10.0, (float)10.0, "USA", "provo", "born", 2000);
            eventDao.createEvent(e);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        result = eventService.getEvents(request);
        ArrayList<Event> events = result.getData();
        Event event = events.get(0);
        assertEquals("Jeremy", event.getDescendant());
        assertEquals("blah",event.getEvent_id());
        assertEquals("blah",event.getPerson_id());
        assertEquals(10.0,event.getLatitude(),0);
        assertEquals(10.0,event.getLongitude(),0);
        assertEquals("USA",event.getCountry());
        assertEquals("provo", event.getCity());
        assertEquals("born", event.getEvent_type());
        assertEquals(2000,event.getYear());

        System.out.println("Passed");

        try{
            System.out.println("Testing getEvents on an Events table with multiple events for one user");
            Database database = new Database();
            EventDao eventDao = new EventDao(database.openConnection());
            Event e = new Event("Jeremy", "meh", "meh", (float)20.0, (float)20.0, "USA", "provo", "death", 2030);
            eventDao.createEvent(e);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        result = eventService.getEvents(request);
        events = result.getData();
        event = events.get(0);
        assertEquals("Jeremy", event.getDescendant());
        assertEquals("blah",event.getEvent_id());
        assertEquals("blah",event.getPerson_id());
        assertEquals(10.0,event.getLatitude(),0);
        assertEquals(10.0,event.getLongitude(),0);
        assertEquals("USA",event.getCountry());
        assertEquals("provo", event.getCity());
        assertEquals("born", event.getEvent_type());
        assertEquals(2000,event.getYear());
        event = events.get(1);
        assertEquals("Jeremy", event.getDescendant());
        assertEquals("meh",event.getEvent_id());
        assertEquals("meh",event.getPerson_id());
        assertEquals(20.0,event.getLatitude(),0);
        assertEquals(20.0,event.getLongitude(),0);
        assertEquals("USA",event.getCountry());
        assertEquals("provo", event.getCity());
        assertEquals("death", event.getEvent_type());
        assertEquals(2030,event.getYear());

        System.out.println("Passed");
    }

    @Test
    public void testGetEvent()
    {
        try {
            System.out.println("Testing getEvent on an empty Events table");
            ClearService clearService = new ClearService();
            clearService.clear();
            Database database = new Database();
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());
            AuthToken at = new AuthToken("blah","Jeremy");
            authTokenDao.addAuthToken(at);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        OneEventRequest request = new OneEventRequest("blah", "blah");
        EventService eventService = new EventService();
        OneEventResult result = eventService.getEvent(request);
        assertEquals("Could not find event_id", result.getMessage());
        System.out.println("Passed");

        try{
            System.out.println("Testing getEvent on an Events table with one event");
            Database database = new Database();
            EventDao eventDao = new EventDao(database.openConnection());
            Event e = new Event("Jeremy", "blah", "blah", (float)10.0, (float)10.0, "USA", "provo", "born", 2000);
            eventDao.createEvent(e);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        result = eventService.getEvent(request);
        Event event = result.getEvent();
        assertEquals("Jeremy", event.getDescendant());
        assertEquals("blah",event.getEvent_id());
        assertEquals("blah",event.getPerson_id());
        assertEquals(10.0,event.getLatitude(),0);
        assertEquals(10.0,event.getLongitude(),0);
        assertEquals("USA",event.getCountry());
        assertEquals("provo", event.getCity());
        assertEquals("born", event.getEvent_type());
        assertEquals(2000,event.getYear());

        System.out.println("Passed");

        try{
            System.out.println("Testing getEvent on an Events table with multiple events for one user");
            Database database = new Database();
            EventDao eventDao = new EventDao(database.openConnection());
            Event e = new Event("Jeremy", "meh", "meh", (float)20.0, (float)20.0, "USA", "provo", "death", 2030);
            eventDao.createEvent(e);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        result = eventService.getEvent(request);
        event = result.getEvent();
        assertEquals("Jeremy", event.getDescendant());
        assertEquals("blah",event.getEvent_id());
        assertEquals("blah",event.getPerson_id());
        assertEquals(10.0,event.getLatitude(),0);
        assertEquals(10.0,event.getLongitude(),0);
        assertEquals("USA",event.getCountry());
        assertEquals("provo", event.getCity());
        assertEquals("born", event.getEvent_type());
        assertEquals(2000,event.getYear());

        System.out.println("Passed");
    }
}
