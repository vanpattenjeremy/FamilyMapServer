package com.fms.familymapserver.DataAccess;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;

public class ServerProxy {

    public String getUrl(URL url, String AuthToken)
    {
        try{
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization",AuthToken);
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = connection.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while((length = responseBody.read(buffer)) != -1)
                {
                    baos.write(buffer, 0, length);
                }

                String responseBodyData = baos.toString();
                return responseBodyData;
            }
        }
        catch(Exception e)
        {
            Log.e("HttpClient",e.getMessage(),e);
        }
        return null;
    }

    public LoginResult login(URL url, LoginRequest request)
    {
        try{
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(gson.toJson(request));
            writer.flush();
            connection.getOutputStream().close();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                LoginResult result = gson.fromJson(reader,LoginResult.class);

                return result;
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public RegisterResult register(URL url, RegisterRequest request)
    {
        try{
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(gson.toJson(request));
            writer.flush();
            connection.getOutputStream().close();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                RegisterResult result = gson.fromJson(reader,RegisterResult.class);

                return result;
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }
}
