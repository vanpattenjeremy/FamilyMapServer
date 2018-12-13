package Service;

import java.util.UUID;

import DAO.Database;
import DAO.UserDao;
import Exceptions.InternalServerException;
import Model.User;
import Request.FillRequest;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;

public class RegisterService {

    public RegisterResult register(RegisterRequest request)
    {
        Database database = null;
        User user = null;
        try{
            database = new Database();
            UserDao userDao = new UserDao(database.openConnection());
            String personID = UUID.randomUUID().toString();
            user = new User(request.getUserName(),request.getPassword(),request.getEmail(),request.getFirstName(),request.getLastName(),request.getGender(),personID);
            userDao.addUser(user);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            RegisterResult result = new RegisterResult(e.getMessage());
            try{
                database.closeConnection(false);
            }
            catch(InternalServerException f)
            {
                result = new RegisterResult(f.getMessage());
            }
            return result;
        }
        FillService fillService = new FillService();
        FillRequest fRequest = new FillRequest(user.getUsername());
        fillService.fill(fRequest);

        LoginService loginService = new LoginService();
        LoginRequest lRequest = new LoginRequest(user.getUsername(),user.getPassword());
        LoginResult lResult = loginService.login(lRequest);

        RegisterResult result = new RegisterResult(lResult.getAuthToken(),lResult.getUsername(), lResult.getPersonID());

        return result;
    }
}
