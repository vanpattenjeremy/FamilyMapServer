package Service;

import java.sql.Connection;

import DAO.AuthTokenDao;
import DAO.Database;
import DAO.PersonDao;
import Exceptions.DoesNotBelongException;
import Exceptions.InternalServerException;
import Exceptions.InvalidAuthTokenException;
import Exceptions.InvalidIDException;
import Request.OnePersonRequest;
import Request.PersonsRequest;
import Result.OnePersonResult;
import Result.PersonsResult;

public class PersonService {

    public PersonsResult getPersons(PersonsRequest request)
    {
        Database database = null;

        try {
            database =  new Database();
            Connection conn = database.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            String username = authTokenDao.getUsername(request.getAuthToken());

            PersonDao personDao = new PersonDao(conn);
            PersonsResult personsResult = new PersonsResult(personDao.getPersons(username));
            database.closeConnection(true);
            if(personsResult.getData().size() == 0)
            {
                personsResult = new PersonsResult("No persons found for that user");
            }

            return personsResult;
        }
        catch(InternalServerException | InvalidAuthTokenException e)
        {
            try {
                database.closeConnection(true);
            }
            catch(Exception f)
            {
                return new PersonsResult(f.getMessage());
            }
            return new PersonsResult(e.getMessage());
        }
    }

    public OnePersonResult getPerson(OnePersonRequest request)
    {
        Database database = null;

        try{

            database =  new Database();
            Connection conn = database.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            String username = authTokenDao.getUsername(request.getAuthToken());
            String person_id = request.getPersonId();

            PersonDao personDao = new PersonDao(conn);
            OnePersonResult result = new OnePersonResult(personDao.getOnePerson(username, person_id));
            database.closeConnection(true);
            return result;

        }
        catch(InternalServerException | InvalidAuthTokenException | InvalidIDException | DoesNotBelongException e)
        {
            try {
                database.closeConnection(true);
            }
            catch(Exception f)
            {
                return new OnePersonResult(f.getMessage());
            }
            return new OnePersonResult(e.getMessage());
        }
    }
}
