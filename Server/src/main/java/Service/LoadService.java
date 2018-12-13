package Service;

import java.sql.Connection;
import java.util.ArrayList;

import DAO.Database;
import DAO.EventDao;
import DAO.PersonDao;
import DAO.UserDao;
import Exceptions.InternalServerException;
import Exceptions.UsernameAlreadyTakenException;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.LoadResult;

public class LoadService {

    public LoadResult load(LoadRequest request)
    {
        LoadResult result = null;
        Database database = null;
        ClearService clearService = new ClearService();
        clearService.clear();

        try {
            database = new Database();
            Connection conn = database.openConnection();

            loadUsers(request.getUsers(),conn);
            loadPersons(request.getPersons(),conn);
            loadEvents(request.getEvents(),conn);

            database.closeConnection(true);
        }
        catch(Exception e)
        {
            try {
                database.closeConnection(false);
            }
            catch(InternalServerException f)
            {
                result = new LoadResult(f.getMessage());
                return result;
            }
            result = new LoadResult(e.getMessage());
            return result;
        }

        result = new LoadResult("Successfully added " + request.getUsers().size() + " users, " + request.getPersons().size() + " persons, and " + request.getPersons().size() + " events to the database.");
        return result;


    }

    private void loadUsers(ArrayList<User> users, Connection conn) throws UsernameAlreadyTakenException {
        UserDao userDao = new UserDao(conn);
        for(User u:users)
        {
            userDao.addUser(u);
        }
    }

    private void loadPersons(ArrayList<Person> persons, Connection conn) throws InternalServerException {
        PersonDao personDao = new PersonDao(conn);
        for(Person p:persons)
        {
            personDao.createPerson(p);
        }
    }

    private void loadEvents(ArrayList<Event> events, Connection conn) throws InternalServerException {
        EventDao eventDao = new EventDao(conn);
        for(Event e:events)
        {
            eventDao.createEvent(e);
        }
    }

}
