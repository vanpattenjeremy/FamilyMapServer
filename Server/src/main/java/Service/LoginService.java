package Service;

import java.util.UUID;

import DAO.AuthTokenDao;
import DAO.Database;
import DAO.UserDao;
import Exceptions.InternalServerException;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

public class LoginService {

    public LoginResult login(LoginRequest request)
    {
        Database database = null;
        LoginResult result = null;
        try{
            String username = request.getUsername();
            String password = request.getPassword();
            database = new Database();
            UserDao userDao = new UserDao(database.openConnection());
            if(userDao.validateUser(username,password))
            {
                User user = userDao.getUser(username);
                AuthTokenDao authTokenDao = new AuthTokenDao(userDao.getConn());
                String token = UUID.randomUUID().toString();
                AuthToken at = new AuthToken(token, username);
                authTokenDao.addAuthToken(at);
                result = new LoginResult(token,username,user.getPerson_id());
            }
            else
            {
                result = new LoginResult("Incorrect username or password");
            }
            database.closeConnection(true);
            return result;
        }
        catch(InternalServerException e)
        {
            try{
                database.closeConnection(false);
            }
            catch(InternalServerException f)
            {
                result = new LoginResult(f.getMessage());
                return result;
            }
            result = new LoginResult(e.getMessage());
            return result;
        }
    }
}
