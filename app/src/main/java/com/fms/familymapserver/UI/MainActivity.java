package com.fms.familymapserver.UI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fms.familymapserver.DataAccess.FamilyTree;
import com.fms.familymapserver.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends SingleFragmentActivity {

    private static final int REQUEST_ERROR = 0;

    public void refreshMap()
    {
        //switch from the login fragment to the map fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        fm.beginTransaction().replace(R.id.activity_fragment_xml, fragment).commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if(errorCode != ConnectionResult.SUCCESS)
        {
            Dialog errorDialog = apiAvailability.getErrorDialog(this, errorCode, REQUEST_ERROR,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog)    {   finish();  }
                    });
            errorDialog.show();
        }
        if(FamilyTree.getAuthToken() != null)
        {
            refreshMap();
        }
        else
        {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = new LoginFragment();
            fm.beginTransaction().replace(R.id.activity_fragment_xml,fragment).commit();
        }
    }

    @Override
    protected Fragment createFragment()
    {
        return new LoginFragment();
    }

    @Override
    public void continueApp()
    {
        refreshMap();
    }

}
