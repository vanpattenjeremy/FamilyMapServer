package com.fms.familymapserver.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.fms.familymapserver.DataAccess.FamilyTree;
import com.fms.familymapserver.DataAccess.ServerProxy;
import com.fms.familymapserver.UI.Context;

import java.io.IOException;
import java.net.URL;

import Request.RegisterRequest;
import Result.RegisterResult;

public class RegisterTask extends AsyncTask<String, Void, String> {

    Context mContext;
    public RegisterTask(Context context)
    {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String host = params[0];
        String port = params[1];
        String username = params[2];
        String password = params[3];
        String email = params[4];
        String firstName = params[5];
        String lastName = params[6];
        String gender = params[7];
        try{
            URL url = new URL("http://" + host + ":" + port + "/user/register");

            RegisterRequest request = new RegisterRequest(username,password,email,firstName,lastName,gender);
            RegisterResult result = new ServerProxy().register(url,request);
            if(result == null)
            {
                return "Could not access server";
            }
            if(result.getMessage() != null)
            {
                return result.getMessage();
            }
            else
            {
                FamilyTree.setUserPerson(result.getPersonID());
                new GetFamilyTreeTask(mContext).execute(result.getAuthToken(), host, port);
                return null;
            }
        }
        catch(IOException e)
        {
            return null;
        }
    }
    protected void onPostExecute(String toast)
    {
        if(toast != null)
        {
            mContext.popToast(toast);
        }
    }
}
