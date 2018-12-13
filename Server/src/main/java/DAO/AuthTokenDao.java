package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Exceptions.InternalServerException;
import Exceptions.InvalidAuthTokenException;
import Model.AuthToken;

public class AuthTokenDao extends DaoInterface{

    public AuthTokenDao(Connection conn)
    {
        super(conn);
    }

    public String getUsername(String authToken) throws InternalServerException, InvalidAuthTokenException
    {
        try{
            String sql = "SELECT * FROM Auth_Tokens WHERE token = ?";
            PreparedStatement stmt = getConn().prepareStatement(sql);

            stmt.setString(1,authToken);

            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return rs.getString("username");
            }
            else
            {
                throw new InvalidAuthTokenException("Auth_Token does not exist");
            }
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to retrieve username");
        }
    }

    public void addAuthToken(AuthToken newAT) throws InternalServerException
    {
        try{
            String sql = "INSERT INTO Auth_Tokens(token, username) VALUES(?,?)";
            PreparedStatement stmt = getConn().prepareStatement(sql);

            stmt.setString(1,newAT.getToken());
            stmt.setString(2, newAT.getUsername());

            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to add new authtoken to Auth_Tokens table");
        }
    }

    public void clearTable() throws InternalServerException
    {
        try{
            String sql = "DELETE FROM Auth_Tokens";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to clear Auth_Tokens table");
        }
    }
}
