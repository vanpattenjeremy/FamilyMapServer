import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import Handlers.ClearHandler;
import Handlers.DefaultFileHandler;
import Handlers.EventHandler;
import Handlers.FillHandler;
import Handlers.LoadHandler;
import Handlers.LoginHandler;
import Handlers.PersonHandler;
import Handlers.RegisterHandler;

public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    private void run(String portNumber)
    {
        System.out.println("Initializing HTTP server");
        try {
            server = HttpServer.create( new InetSocketAddress(Integer.parseInt(portNumber)),MAX_WAITING_CONNECTIONS);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);

        System.out.println("Creating contexts");

        server.createContext("/clear", new ClearHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/event/", new EventHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/person/", new PersonHandler());
        server.createContext("/fill/",new FillHandler());
        server.createContext("/", new DefaultFileHandler());

        System.out.println("Starting server");

        server.start();
        System.out.println("Server started");
    }

    public static void main(String args[])
    {
        String portNumber = "8080";
        new Server().run(portNumber);
    }

}
