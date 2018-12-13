package Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.apache.tools.ant.taskdefs.email.Header;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.EventsRequest;
import Request.OneEventRequest;
import Result.EventsResult;
import Result.OneEventResult;
import Service.EventService;

public class EventHandler implements HttpHandler {

    public void handle(HttpExchange exchange)
    {
        try{
            if(exchange.getRequestMethod().toLowerCase().equals("get")) {
                EventService eventService = new EventService();
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();


                Headers headers = exchange.getRequestHeaders();
                if (headers.containsKey("Authorization")) {
                    String authToken = headers.getFirst("Authorization");

                    String path = exchange.getRequestURI().getPath();

                    if (path.length() <= 6) {
                        EventsRequest request = new EventsRequest(authToken);
                        EventsResult result = eventService.getEvents(request);
                        if (result.getMessage() == null) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                        }
                        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
                        writer.write(gson.toJson(result));
                        writer.flush();
                    } else {
                        path = path.substring(7);
                        OneEventRequest request = new OneEventRequest(authToken, path);
                        OneEventResult result = eventService.getEvent(request);
                        if (result.getMessage() == null) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                        }
                        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
                        writer.write(gson.toJson(result));
                        writer.flush();
                    }

                    exchange.getResponseBody().close();
                }
                else
                {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                    exchange.getResponseBody().close();
                }
            }
            else
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                exchange.getResponseBody().close();
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
