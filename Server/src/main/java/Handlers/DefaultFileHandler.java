package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultFileHandler implements HttpHandler {

    public void handle(HttpExchange exchange)
    {
        try {
            String filePathStr = "C:/Users/jerem/AndroidStudioProjects/FamilyMapServer/web";
            Path filePath = FileSystems.getDefault().getPath(filePathStr + "/index.html");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
            Files.copy(filePath, exchange.getResponseBody());

            exchange.getResponseBody().close();
        }
        catch(IOException e)
        {
            try {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
            catch(IOException f)
            {}
        }
    }
}
