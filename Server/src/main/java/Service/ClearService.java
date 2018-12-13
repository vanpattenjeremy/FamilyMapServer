package Service;

import java.sql.Connection;

import DAO.AuthTokenDao;
import DAO.Database;
import DAO.EventDao;
import DAO.PersonDao;
import DAO.UserDao;
import Exceptions.InternalServerException;
import Result.ClearResult;

public class ClearService {

    public ClearResult clear()
    {
        Database database = null;
        boolean commit = true;
        ClearResult result = null;
        try {
            database =  new Database();
            Connection conn = database.openConnection();

            UserDao userDao = new UserDao(conn);
            PersonDao personDao = new PersonDao(conn);
            EventDao eventDao = new EventDao(conn);
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);

            userDao.clearTable();
            personDao.clearTable();
            eventDao.clearTable();
            authTokenDao.clearTable();
        }
        catch(InternalServerException e)
        {
            commit = false;
            result = new ClearResult(e.getMessage());
        }
        try {
            database.closeConnection(commit);
            if(commit)
            {
                return new ClearResult("Clear succeeded");
            }
            else
            {
                return result;
            }
        }
        catch(InternalServerException e)
        {
            return new ClearResult("Connection not closed");
        }
    }
}
