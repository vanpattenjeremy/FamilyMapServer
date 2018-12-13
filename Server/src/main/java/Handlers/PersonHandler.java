package Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.OnePersonRequest;
import Request.PersonsRequest;
import Result.OnePersonResult;
import Result.PersonsResult;
import Service.PersonService;

public class PersonHandler implements HttpHandler {

    public void handle(HttpExchange exchange)
    {
        try{
            if(exchange.getRequestMethod().toLowerCase().equals("get")) {
                PersonService personService = new PersonService();
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();


                Headers headers = exchange.getRequestHeaders();
                if (headers.containsKey("Authorization")) {
                    String authToken = headers.getFirst("Authorization");

                    String path = exchange.getRequestURI().getPath();

                    if (path.length() <= 7) {
                        PersonsRequest request = new PersonsRequest(authToken);
                        PersonsResult result = personService.getPersons(request);
                        if (result.getMessage() == null) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                        }
                        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
                        writer.write(gson.toJson(result));
                        writer.flush();
                    } else {
                        path = path.substring(8);
                        OnePersonRequest request = new OnePersonRequest(authToken, path);
                        OnePersonResult result = personService.getPerson(request);
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
