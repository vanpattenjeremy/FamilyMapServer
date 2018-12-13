package Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.LoginRequest;
import Result.LoginResult;
import Service.LoginService;

public class LoginHandler implements HttpHandler {

    public void handle(HttpExchange exchange)
    {
        try{
            if(exchange.getRequestMethod().toLowerCase().equals("post"))
            {
                LoginService loginService = new LoginService();
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();

                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
                LoginRequest request = gson.fromJson(reader,LoginRequest.class);
                LoginResult result = loginService.login(request);
                if(result.getMessage() == null)
                {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                }
                else
                {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
                }
                OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
                writer.write(gson.toJson(result));
                writer.flush();
                exchange.getResponseBody().close();
            }
            else
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                exchange.getResponseBody().close();
            }
        }
        catch(IOException e)
        {
            try{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
                exchange.getResponseBody().close();
            }
            catch(IOException f)
            {}
        }
    }
}
