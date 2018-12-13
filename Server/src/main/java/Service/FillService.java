package Service;

import java.sql.Connection;
import java.util.UUID;

import DAO.Database;
import DAO.EventDao;
import DAO.PersonDao;
import DAO.UserDao;
import DataContainers.DataCoder;
import DataContainers.Names;
import DataContainers.Place;
import DataContainers.Places;
import Exceptions.InternalServerException;
import Model.Event;
import Model.Person;
import Model.User;
import Request.FillRequest;
import Result.FillResult;

public class FillService {

    private Names names = null;
    private Places places = null;
    private Connection conn = null;
    private int personsAdded = 0;
    private int eventsAdded = 0;

    public FillService(){};
    public FillService(Connection conn)
    {
        this.conn = conn;
    }

    public FillResult fill(FillRequest request)
    {
        User user = null;
        Database database = null;
        personsAdded = 0;
        eventsAdded = 0;
        try {
            database = new Database();
            conn = database.openConnection();
            UserDao userDao = new UserDao(conn);
            user = userDao.getUser(request.getUsername());
        }
        catch(InternalServerException e)
        {
            FillResult result = new FillResult(e.getMessage());
            try{
                database.closeConnection(false);
            }
            catch(InternalServerException f)
            {
                result = new FillResult(f.getMessage());
            }
            return result;
        }

        names = DataCoder.decodeNames();
        places = DataCoder.decodePlaces();

        Person person = new Person(user.getUsername(),user.getPerson_id(),user.getFirst_name(),user.getLast_name(),user.getGender());

        try {
            PersonDao personDao = new PersonDao(conn);
            EventDao eventDao = new EventDao(conn);

            personDao.clearTable(user.getUsername());
            eventDao.clearTable(user.getUsername());
            createParents(person, request.getGenerations(), 1);
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            FillResult result = new FillResult(e.getMessage());
            try{
                database.closeConnection(false);
            }
            catch(InternalServerException f)
            {
                result = new FillResult(f.getMessage());
            }
            return result;
        }

        FillResult result = new FillResult("Successfully added " + Integer.toString(personsAdded) + " persons and " + Integer.toString(eventsAdded) + " events to the database.");
        return result;

    }

    private Person createPerson(String username, String gender) {
        String person_id = UUID.randomUUID().toString();
        String firstName = null;
        if(gender.equals("m"))
        {
            int random = (int)(Math.random() * names.getMaleNames().size());
            firstName = names.getMaleNames().get(random);
        }
        else
        {
            int random = (int)(Math.random() * names.getFemaleNames().size());
            firstName = names.getFemaleNames().get(random);
        }
        int random = (int)(Math.random() * names.getLastNames().size());
        String lastName = names.getLastNames().get(random);
        return new Person(username,person_id,firstName,lastName,gender);

    }

    private void createParents(Person person, int maxGenerations, int currGeneration) throws InternalServerException
    {
        if(currGeneration <= maxGenerations) {
            Person mom = createPerson(person.getDescendant(), "f");
            Person dad = createPerson(person.getDescendant(), "m");
            mom.setSpouse(dad.getPerson_id());
            dad.setSpouse(mom.getPerson_id());

            if (currGeneration != maxGenerations) {
                createParents(mom, maxGenerations, currGeneration + 1);
                createParents(dad, maxGenerations, currGeneration + 1);
            } else {
                addPerson(mom);
                generateEvents(mom, currGeneration + 1);
                personsAdded++;
                addPerson(dad);
                generateEvents(dad, currGeneration + 1);
                personsAdded++;
            }

            person.setMother(mom.getPerson_id());
            person.setFather(dad.getPerson_id());
        }
        addPerson(person);
        generateEvents(person, currGeneration);
        personsAdded++;
    }

    private void addPerson(Person person) throws InternalServerException
    {
        PersonDao personDao = new PersonDao(conn);
        personDao.createPerson(person);
    }

    private void generateEvents(Person person, int generation) throws InternalServerException
    {
        EventDao eventDao = new EventDao(conn);

        String descendant = person.getDescendant();
        String event_id = UUID.randomUUID().toString();
        String person_id = person.getPerson_id();
        int random = (int)(Math.random() * places.getData().size());
        Place place = places.getData().get(random);
        float latitude = place.getLatitude();
        float longitude = place.getLongitude();
        String country = place.getCountry();
        String city = place.getCity();
        String event_type = "birth";
        int year = 2020 - (20 * generation);
        Event birth = new Event(descendant,event_id,person.getPerson_id(),latitude,longitude,country,city,event_type,year);
        eventDao.createEvent(birth);
        eventsAdded++;

        event_id = UUID.randomUUID().toString();
        random = (int)(Math.random() * places.getData().size());
        place = places.getData().get(random);
        latitude = place.getLatitude();
        longitude = place.getLongitude();
        country = place.getCountry();
        city = place.getCity();
        event_type = "baptism";
        year = year + 8;    //8 years old
        Event baptism = new Event(descendant,event_id,person.getPerson_id(),latitude,longitude,country,city,event_type,year);
        eventDao.createEvent(baptism);
        eventsAdded++;

        if(generation > 1)
        {
            event_id = UUID.randomUUID().toString();
            random = (int) (Math.random() * places.getData().size());
            place = places.getData().get(random);
            latitude = place.getLatitude();
            longitude = place.getLongitude();
            country = place.getCountry();
            city = place.getCity();
            event_type = "marriage";
            year = year + 11;   //19 years old
            Event marriage = new Event(descendant, event_id, person.getPerson_id(), latitude, longitude, country, city, event_type, year);
            eventDao.createEvent(marriage);
            eventsAdded++;
        }

        if(generation > 3)
        {
            event_id = UUID.randomUUID().toString();
            random = (int)(Math.random() * places.getData().size());
            place = places.getData().get(random);
            latitude = place.getLatitude();
            longitude = place.getLongitude();
            country = place.getCountry();
            city = place.getCity();
            event_type = "death";
            year = year + 51;   //70 years old
            Event death = new Event(descendant,event_id,person.getPerson_id(),latitude,longitude,country,city,event_type,year);
            eventDao.createEvent(death);
            eventsAdded++;
        }
    }
}
