package Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.apache.tools.ant.taskdefs.condition.Http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.RegisterRequest;
import Result.RegisterResult;
import Service.RegisterService;

public class RegisterHandler implements HttpHandler {

    public void handle(HttpExchange exchange)
    {
        try{
            if(exchange.getRequestMethod().toLowerCase().equals("post"))
            {
                RegisterService registerService = new RegisterService();
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();

                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
                RegisterRequest request = gson.fromJson(reader,RegisterRequest.class);
                RegisterResult result = registerService.register(request);
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
                exchange.close();
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
