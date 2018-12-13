package com.fms.familymapserver.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.fms.familymapserver.UI.Context;
import com.fms.familymapserver.UI.MainActivity;
import com.fms.familymapserver.DataAccess.FamilyTree;
import com.fms.familymapserver.DataAccess.ServerProxy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

import Result.EventsResult;
import Result.PersonsResult;

public class GetFamilyTreeTask extends AsyncTask<String, Void, Void> {

    private Context mContext;

    public GetFamilyTreeTask(Context context)
    {
        mContext = context;
    }
    @Override
    protected Void doInBackground(String... params) {
        String authToken = params[0];
        String host = params[1];
        String port = params[2];
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            URL url = new URL("http://" + host + ":" + port + "/person");
            String personsResultJSON = new ServerProxy().getUrl(url, authToken);
            PersonsResult personsResult = gson.fromJson(personsResultJSON, PersonsResult.class);

            url = new URL("http://" + host + ":" + port + "/event");
            String eventsResultJSON = new ServerProxy().getUrl(url, authToken);
            EventsResult eventsResult = gson.fromJson(eventsResultJSON, EventsResult.class);

            FamilyTree.setHost(host);
            FamilyTree.setPort(port);
            FamilyTree.setAuthToken(authToken);
            FamilyTree.setEvents(eventsResult);
            FamilyTree.setPersons(personsResult);
            FamilyTree.generateFullPersonMap();
        } catch (IOException e) {
            Log.i("GetFamilyTreeTask", "Failed to fetch URL: ", e);
            return null;
        }
        return null;
    }

    protected void onPostExecute(Void result) {

        if(FamilyTree.getPersons().getMessage() != null)
        {
            mContext.popToast(FamilyTree.getEvents().getMessage());
        }
        else if(FamilyTree.getEvents().getMessage() != null)
        {
            mContext.popToast(FamilyTree.getPersons().getMessage());
        }
        else
        {
            mContext.continueApp();
        }
    }
}
