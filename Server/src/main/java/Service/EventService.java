package Service;

import java.sql.Connection;

import DAO.AuthTokenDao;
import DAO.Database;
import DAO.EventDao;
import Exceptions.DoesNotBelongException;
import Exceptions.InternalServerException;
import Exceptions.InvalidAuthTokenException;
import Exceptions.InvalidIDException;
import Request.EventsRequest;
import Request.OneEventRequest;
import Result.EventsResult;
import Result.OneEventResult;

public class EventService {

    public EventsResult getEvents(EventsRequest request)
    {
        Database database = null;

        try {
            database =  new Database();
            Connection conn = database.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            String username = authTokenDao.getUsername(request.getAuthtoken());

            EventDao eventDao = new EventDao(conn);
            EventsResult eventsResult = new EventsResult(eventDao.getEvents(username));
            database.closeConnection(true);
            if(eventsResult.getData().size() == 0)
            {
                eventsResult = new EventsResult("No events found for that user");
            }

            return eventsResult;
        }
        catch(InternalServerException | InvalidAuthTokenException e)
        {
            try {
                database.closeConnection(true);
            }
            catch(Exception f)
            {
                return new EventsResult(f.getMessage());
            }
            return new EventsResult(e.getMessage());
        }
    }

    public OneEventResult getEvent(OneEventRequest request)
    {
        Database database = null;

        try{

            database =  new Database();
            Connection conn = database.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            String username = authTokenDao.getUsername(request.getAuthToken());
            String event_id = request.getEventId();

            EventDao eventDao = new EventDao(conn);
            OneEventResult result = new OneEventResult(eventDao.getOneEvent(username, event_id));
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
                return new OneEventResult(f.getMessage());
            }
            return new OneEventResult(e.getMessage());
        }
    }
}
