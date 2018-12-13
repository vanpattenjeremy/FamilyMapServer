package Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.FillRequest;
import Result.FillResult;
import Service.FillService;


public class FillHandler implements HttpHandler {

    public void handle(HttpExchange exchange)
    {
        try{
            if(exchange.getRequestMethod().toLowerCase().equals("post")) {
                FillService fillService = new FillService();
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();

                FillRequest request = null;
                String path = exchange.getRequestURI().getPath();
                if (path.indexOf('/', 5) != path.lastIndexOf('/')) {
                    String username = path.substring(6, path.lastIndexOf('/'));
                    String generation = path.substring(path.lastIndexOf('/')+1);
                    request = new FillRequest(username);
                    request.setGenerations(Integer.valueOf(generation));
                } else {
                    String username = path.substring(6);
                    request = new FillRequest(username);
                }
                FillResult result = fillService.fill(request);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
                writer.write(gson.toJson(result));
                writer.flush();
                exchange.getResponseBody().close();
            }
            else
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                exchange.getRequestBody().close();
            }

        }
        catch(IOException e)
        {
            try {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                exchange.getResponseBody().close();
            }
            catch(IOException f)
            {}
        }
    }

}
