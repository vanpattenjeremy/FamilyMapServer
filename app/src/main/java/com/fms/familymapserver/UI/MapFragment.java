package com.fms.familymapserver.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fms.familymapserver.DataAccess.FamilyTree;
import com.fms.familymapserver.DataAccess.FullPerson;
import com.fms.familymapserver.DataAccess.MapSettings;
import com.fms.familymapserver.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    private TextView mNameTextView;
    private TextView mEventTextView;
    private ImageView mGenderImageView;
    private LinearLayout mMarkerDisplay;
    private SupportMapFragment mMapFragment;
    private String mCurrentPerson;
    private FamilyTree mFamilyTree;
    private static final String ARG_EVENT_ID = "eventId";
    private ArrayList<Polyline> mapLines = new ArrayList<>();

    public static MapFragment newInstance(String eventId)
    {
        MapFragment fragment = new MapFragment();
        if(eventId != null) {
            Bundle args = new Bundle();
            args.putCharSequence(ARG_EVENT_ID, eventId);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View v = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);

        mMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mNameTextView = v.findViewById(R.id.Name_View);
        mEventTextView = v.findViewById(R.id.Event_View);
        mGenderImageView = v.findViewById(R.id.Gender_View);
        mMarkerDisplay = v.findViewById(R.id.Marker_Info_Display);
        mMarkerDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = PersonActivity.newIntent(getActivity(), mCurrentPerson);
                startActivityForResult(intent,1);
                //Toast.makeText(getActivity(), "Clicked on display thing do that thing now", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.setMapType(MapSettings.getMapType());
        ArrayList<Event> events = FamilyTree.getEvents().getData();
        String selectedEvent = null;
        if(getArguments() != null)
        {
            selectedEvent = (String) getArguments().getCharSequence(ARG_EVENT_ID);
        }

        for(Event event:events)
        {
            LatLng location = new LatLng(event.getLatitude(),event.getLongitude());
            Marker currMarker;
            if(event.getEvent_type().toLowerCase().equals("birth")) {
                currMarker = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            else if(event.getEvent_type().toLowerCase().equals("baptism"))
            {
                currMarker = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            }
            else if(event.getEvent_type().toLowerCase().equals("marriage"))
            {
                currMarker = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            }
            else if(event.getEvent_type().toLowerCase().equals("death"))
            {
                currMarker = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            }
            else
            {
                currMarker = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
            currMarker.setTag(event);

            if(event.getEvent_id().equals(selectedEvent))
            {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                selectMarker(currMarker, googleMap);
            }
        }
        googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                selectMarker(marker, googleMap);
                return false;
            }
        });
    }

    private void selectMarker(Marker marker, GoogleMap googleMap)
    {
        Event event = (Event) marker.getTag();

        Person person = FamilyTree.getPerson(event.getPerson_id());
        mCurrentPerson = person.getPerson_id();
        String name = person.getFirst_name() + " " + person.getLast_name();
        String gender = person.getGender();
        String location = event.getCity() + "," + event.getCountry();
        String year = Integer.toString(event.getYear());

        mNameTextView.setText(name);
        mEventTextView.setText(location + "(" + year + ")");
        IconDrawable genderIcon;
        int color;
        if(gender.equals("m"))
        {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male);
            color = R.color.colorMaleIcon;
        }
        else
        {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female);
            color = R.color.colorFemaleIcon;
        }
        mGenderImageView.setImageDrawable(genderIcon.colorRes(color));
        drawLines(event,googleMap);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        if(getActivity() instanceof EventActivity)
        {
            setHasOptionsMenu(false);
        }
        else
        {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.fragment_map, menu);
            IconDrawable searchIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_search);
            IconDrawable filterIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_filter);
            IconDrawable settingsIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_cog);
            menu.findItem(R.id.search_badge).setIcon(searchIcon.colorRes(R.color.colorActionBarIcons).actionBarSize());
            menu.findItem(R.id.filter_badge).setIcon(filterIcon.colorRes(R.color.colorActionBarIcons).actionBarSize());
            menu.findItem(R.id.settings_badge).setIcon(settingsIcon.colorRes(R.color.colorActionBarIcons).actionBarSize());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = (String)item.getTitle();
        if(title == null)
        {
            Intent intent = new Intent(getActivity(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(title.equals("Search"))
        {
            Intent intent = new Intent(getActivity(),SearchActivity.class);
            startActivity(intent);
        }
        else if(title.equals("Filter"))
        {
            Intent intent = new Intent(getActivity(),FilterActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getActivity(),SettingActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK)
        {
            ((Context)getActivity()).refreshMap();
        }
        getActivity().setResult(resultCode);
    }

    private void drawLines(Event root, GoogleMap map) {
        for (Polyline line : mapLines) {
            line.remove();
        }

        LatLng rootLocation = new LatLng(root.getLatitude(), root.getLongitude());

        if(MapSettings.isShowSpouseLine()) {

            PolylineOptions spouseLine = new PolylineOptions();
            spouseLine.add(rootLocation);
            FullPerson spouse = mFamilyTree
                    .getFullPerson(mFamilyTree.getPerson(root.getPerson_id()).getSpouse());
            if (spouse != null && spouse.getEvents() != null) {
                Event spouseEvent = mFamilyTree.getEvent(spouse.getEvents().get(0));
                LatLng nextLocation = new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude());
                spouseLine.add(nextLocation);
            }
            mapLines.add(map.addPolyline(spouseLine.color(MapSettings.getSpouseLineColor())));
        }

        if(MapSettings.isShowFamilyTreeLine()) {
            drawParentsLine(root, map, 25);
        }

        if(MapSettings.isShowLifeStoryLine()) {
            PolylineOptions lifeStoryLine = new PolylineOptions();
            lifeStoryLine.add(rootLocation);
            for (String eventId : mFamilyTree.getFullPerson(mCurrentPerson).getEvents()) {
                Event event = mFamilyTree.getEvent(eventId);
                LatLng eLocation = new LatLng(event.getLatitude(), event.getLongitude());
                lifeStoryLine.add(eLocation);
            }
            mapLines.add(map.addPolyline(lifeStoryLine.color(MapSettings.getLifeStoryLineColor())));
        }
    }

    private void drawParentsLine(Event root, GoogleMap map, float width)
    {
        PolylineOptions fatherLine = new PolylineOptions();
        PolylineOptions motherLine = new PolylineOptions();
        LatLng rootLocation = new LatLng(root.getLatitude(),root.getLongitude());

        FullPerson father = mFamilyTree
                .getFullPerson(mFamilyTree.getPerson(root.getPerson_id()).getFather());
        if(father != null && father.getEvents() != null) {
            Event fatherEvent = mFamilyTree.getEvent(father.getEvents().get(0));
            LatLng fatherLocation = new LatLng(fatherEvent.getLatitude(),fatherEvent.getLongitude());
            fatherLine.add(rootLocation, fatherLocation);
            mapLines.add(map.addPolyline(fatherLine.color(MapSettings.getFamilyTreeLineColor()).width(width)));
            drawParentsLine(fatherEvent, map, width-6);
        }

        FullPerson mother = mFamilyTree
                .getFullPerson(mFamilyTree.getPerson(root.getPerson_id()).getMother());
        if(mother != null && mother.getEvents() != null)
        {
            Event motherEvent = mFamilyTree.getEvent(mother.getEvents().get(0));
            LatLng motherLocation = new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude());
            motherLine.add(rootLocation, motherLocation);
            mapLines.add(map.addPolyline(motherLine.color(MapSettings.getFamilyTreeLineColor()).width(width)));
            drawParentsLine(motherEvent, map, width-6);
        }

    }
}
