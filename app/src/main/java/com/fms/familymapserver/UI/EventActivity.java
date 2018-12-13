package com.fms.familymapserver.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fms.familymapserver.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class EventActivity extends SingleFragmentActivity {

    private static final String EXTRA_EVENT_ID = "com.fms.familymapserver.UI.eventId";

    public static Intent newIntent(Context packageContext, String eventId) {
        Intent intent = new Intent(packageContext, EventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
    }

    @Override
    protected Fragment createFragment() {
        String eventId = (String) getIntent().getCharSequenceExtra(EXTRA_EVENT_ID);
        return MapFragment.newInstance(eventId);
    }

    @Override
    public void continueApp()
    {

    }

    @Override
    public void refreshMap()
    {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        fm.beginTransaction().replace(R.id.activity_fragment_xml,fragment).commit();
        setResult(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(resultCode);
    }
}
