package DAO;

import java.sql.Connection;

import Exceptions.InternalServerException;

public class DaoInterface {

    private Connection conn;

    public DaoInterface(Connection conn)
    {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void clearTable() throws InternalServerException {}

}
