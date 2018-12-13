package com.fms.familymapserver.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.fms.familymapserver.DataAccess.FamilyTree;
import com.fms.familymapserver.DataAccess.MapSettings;
import com.fms.familymapserver.R;
import com.fms.familymapserver.Tasks.GetFamilyTreeTask;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class SettingActivity extends AppCompatActivity implements Context {

    private Switch mLifeStorySwitch;
    private Switch mFamilyTreeSwitch;
    private Switch mSpouseSwitch;
    private Spinner mLifeStorySpinner;
    private Spinner mFamilyTreeSpinner;
    private Spinner mSpouseSpinner;
    private Spinner mMapSpinner;
    private LinearLayout mLogoutLayout;
    private LinearLayout mReSyncLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupAdapters();
        setupListeners();
    }

    private void setupAdapters()
    {
        mLifeStorySpinner = findViewById(R.id.Life_Story_Color_Spinner);
        mFamilyTreeSpinner = findViewById(R.id.Family_Tree_Color_Spinner);
        mSpouseSpinner = findViewById(R.id.Spouse_Color_Spinner);
        mMapSpinner = findViewById(R.id.Map_Type_Spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.color_spinner,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLifeStorySpinner.setAdapter(adapter);
        mLifeStorySpinner.setSelection(translateColor(MapSettings.getLifeStoryLineColor()));
        mFamilyTreeSpinner.setAdapter(adapter);
        mFamilyTreeSpinner.setSelection(translateColor(MapSettings.getFamilyTreeLineColor()));
        mSpouseSpinner.setAdapter(adapter);
        mSpouseSpinner.setSelection(translateColor(MapSettings.getSpouseLineColor()));

        adapter = ArrayAdapter.createFromResource(this, R.array.map_backgounds,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMapSpinner.setAdapter(adapter);
        mMapSpinner.setSelection(translateType(MapSettings.getMapType()));
    }

    private int translateColor(int color)
    {
        switch(color)
        {
            case Color.GREEN:
                return 0;
            case Color.BLUE:
                return 1;
            case Color.RED:
                return 2;
            default:
                return 0;
        }
    }

    private int translateType(int type)
    {
        switch(type)
        {
            case MAP_TYPE_NORMAL:
                return 0;
            case MAP_TYPE_HYBRID:
                return 1;
            case MAP_TYPE_SATELLITE:
                return 2;
            case MAP_TYPE_TERRAIN:
                return 3;
            default:
                return 0;
        }
    }


    private void setupListeners()
    {
        mLifeStorySwitch = findViewById(R.id.Life_Story_Switch);
        mLifeStorySwitch.setChecked(MapSettings.isShowLifeStoryLine());
        mLifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapSettings.setShowLifeStoryLine(isChecked);
            }
        });
        mFamilyTreeSwitch = findViewById(R.id.Family_Tree_Switch);
        mFamilyTreeSwitch.setChecked(MapSettings.isShowFamilyTreeLine());
        mFamilyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapSettings.setShowFamilyTreeLine(isChecked);
            }
        });
        mSpouseSwitch = findViewById(R.id.Spouse_Switch);
        mSpouseSwitch.setChecked(MapSettings.isShowSpouseLine());
        mSpouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapSettings.setShowSpouseLine(isChecked);
            }
        });

        mLogoutLayout = findViewById(R.id.Logout_Layout);
        mLogoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyTree.setAuthToken(null);
                MapSettings.reset();
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        mReSyncLayout = findViewById(R.id.Re_Sync_Data_Layout);
        mReSyncLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetFamilyTreeTask(SettingActivity.this)
                        .execute(FamilyTree.getAuthToken(), FamilyTree.getHost(), FamilyTree.getPort());
            }
        });

        mLifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int color = 0;
                switch(position)
                {
                    case 0:
                        color = Color.GREEN;
                        break;
                    case 1:
                        color = Color.BLUE;
                        break;
                    case 2:
                        color = Color.RED;
                        break;
                }
                MapSettings.setLifeStoryLineColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        mFamilyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int color = 0;
                    switch(position)
                    {
                        case 0:
                            color = Color.GREEN;
                            break;
                        case 1:
                            color = Color.BLUE;
                            break;
                        case 2:
                            color = Color.RED;
                            break;
                    }
                    MapSettings.setFamilyTreeLineColor(color);
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int color = 0;
                switch(position)
                {
                    case 0:
                        color = Color.GREEN;
                        break;
                    case 1:
                        color = Color.BLUE;
                        break;
                    case 2:
                        color = Color.RED;
                        break;
                }
                MapSettings.setSpouseLineColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int type = 0;
                switch(position)
                {
                    case 0:
                        type = MAP_TYPE_NORMAL;
                        break;
                    case 1:
                        type = MAP_TYPE_HYBRID;
                        break;
                    case 2:
                        type = MAP_TYPE_SATELLITE;
                        break;
                    case 3:
                        type = MAP_TYPE_TERRAIN;
                        break;
                }
                MapSettings.setMapType(type);
            }
        });
    }

    @Override
    public void popToast(String toast) {
        Toast.makeText(this, toast,Toast.LENGTH_SHORT);
    }

    @Override
    public void continueApp(){
        Toast.makeText(this,"Re-Sync Successful",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshMap() {

    }

}
