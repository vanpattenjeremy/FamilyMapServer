package com.fms.familymapserver.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.fms.familymapserver.DataAccess.FamilyTree;
import com.fms.familymapserver.DataAccess.ServerProxy;
import com.fms.familymapserver.UI.Context;

import java.io.IOException;
import java.net.URL;

import Request.LoginRequest;
import Result.LoginResult;

public class LoginTask extends AsyncTask<String, Void, String> {

    private Context mContext;

    public LoginTask(Context context)
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
        try{
            URL url = new URL("http://" + host + ":" + port + "/user/login");
            LoginRequest request = new LoginRequest(username, password);
            LoginResult result = new ServerProxy().login(url, request);
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
        catch (IOException e)
        {
            return null;
        }
    }

    protected void onPostExecute(String toast)
    {
        if(toast != null) {
            mContext.popToast(toast);
        }
    }
}