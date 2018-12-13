package com.fms.familymapserver.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fms.familymapserver.R;

public abstract class SingleFragmentActivity extends AppCompatActivity implements Context {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_fragment_xml);

        if(fragment == null)
        {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.activity_fragment_xml, fragment).commit();
        }

    }
    @Override
    public void popToast(String toast)
    {
        Toast.makeText(SingleFragmentActivity.this,toast,Toast.LENGTH_SHORT).show();
    }
}
