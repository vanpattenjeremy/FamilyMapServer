package Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.LoadRequest;
import Result.LoadResult;
import Service.LoadService;

public class LoadHandler implements HttpHandler {

    public void handle(HttpExchange exchange) {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                LoadService loadService = new LoadService();
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();

                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
                LoadRequest request = gson.fromJson(reader, LoadRequest.class);
                LoadResult result = loadService.load(request);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
                writer.write(gson.toJson(result));
                writer.flush();
                exchange.getResponseBody().close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.close();
            }
        } catch (IOException e) {
            try {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                exchange.close();
            } catch (IOException f) {
            }
        }
    }
}
