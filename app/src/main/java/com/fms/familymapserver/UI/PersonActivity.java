package com.fms.familymapserver.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fms.familymapserver.DataAccess.FamilyTree;
import com.fms.familymapserver.DataAccess.FullPerson;
import com.fms.familymapserver.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {

    private TextView mFirstNameView;
    private TextView mLastNameView;
    private TextView mGenderView;
    private PersonsExpandableListAdapter mAdapter;
    private ExpandableListView mListView;
    private FullPerson mFullPerson;
    public static final String EXTRA_PERSON_ID = "com.fms.familymapserver.UI.personId";

    public static Intent newIntent(Context packageContext, String personId) {
        Intent intent = new Intent(packageContext, PersonActivity.class);
        intent.putExtra(EXTRA_PERSON_ID, personId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_person);
        mListView = findViewById(R.id.Person_Expandable_List);
        updateUI();
    }

    private void updateUI() {
        List<String> dataHeaders = new ArrayList<>();
        HashMap<String, List<String>> children = new HashMap<>();
        String personId = (String) this.getIntent().getSerializableExtra(EXTRA_PERSON_ID);

        mFullPerson = FamilyTree.getFullPerson(personId);
        mFirstNameView = findViewById(R.id.First_Name_Person_View);
        mFirstNameView.setText(mFullPerson.getFirst_name());
        mLastNameView = findViewById(R.id.Last_Name_Person_View);
        mLastNameView.setText(mFullPerson.getLast_name());
        mGenderView = findViewById(R.id.Gender_Person_View);
        if(mFullPerson.getGender().equals("m"))
        {
            mGenderView.setText("Male");
        }
        else
        {
            mGenderView.setText("Female");
        }

        ArrayList<String> headers = new ArrayList<>();
        headers.add("LIFE EVENTS");
        headers.add("FAMILY");

        HashMap<String, List<String>> listChildren = new HashMap<>();
        ArrayList<String> lifeEvents = new ArrayList<>();
        ArrayList<String> events = mFullPerson.getEvents();
        if(events != null) {
            for (String eventId : events) {
                Event event = FamilyTree.getEvent(eventId);
                lifeEvents.add(event.getEvent_type() + ": " + event.getCity() + ", " + event.getCountry()
                        + "(" + Integer.toString(event.getYear()) + ")" + "\n"
                        + mFullPerson.getFirst_name() + " " + mFullPerson.getLast_name());
            }
        }
        listChildren.put(headers.get(0),lifeEvents);

        ArrayList<String> family = new ArrayList<>();
        Person father = FamilyTree.getPerson(mFullPerson.getFather());
        if(father != null) {
            family.add(father.getFirst_name() + " " + father.getLast_name() + "\n" + "FATHER");
        }
        Person mother = FamilyTree.getPerson(mFullPerson.getMother());
        if(mother != null) {
            family.add(mother.getFirst_name() + " " + mother.getLast_name() + "\n" + "MOTHER");
        }
        Person spouse = FamilyTree.getPerson(mFullPerson.getSpouse());
        if(spouse != null) {
            family.add(spouse.getFirst_name() + " " + spouse.getLast_name() + "\n" + "SPOUSE");
        }
        Person child = FamilyTree.getPerson(mFullPerson.getChild());
        if(child != null) {
            family.add(child.getFirst_name() + " " + child.getLast_name() + "\n" + "CHILD");
        }
        listChildren.put(headers.get(1),family);


        mAdapter = new PersonsExpandableListAdapter(headers,listChildren);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        setResult(resultCode);
    }

    private class PersonsExpandableListAdapter extends BaseExpandableListAdapter {

        private List<String> mListDataHeaders;
        private HashMap<String, List<String>> mListHashMap;

        public PersonsExpandableListAdapter(List<String> listDataHeaders, HashMap<String,
                List<String>> listHashMap) {
            mListDataHeaders = listDataHeaders;
            mListHashMap = listHashMap;

            mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    String item = mListHashMap.get(mListDataHeaders.get(groupPosition))
                            .get(childPosition);
                    Intent intent;
                    if (groupPosition == 0)
                    {
                        intent = EventActivity.newIntent(PersonActivity.this,
                                mFullPerson.getEvents().get(childPosition));
                    }
                    else{
                        if (item.contains("FATHER")) {
                            intent = PersonActivity.newIntent(PersonActivity.this,
                                    mFullPerson.getFather());
                        }
                        else if(item.contains("MOTHER"))
                        {
                            intent = PersonActivity.newIntent(PersonActivity.this,
                                    mFullPerson.getMother());
                        }
                        else if(item.contains("SPOUSE"))
                        {
                            intent = PersonActivity.newIntent(PersonActivity.this,
                                    mFullPerson.getSpouse());
                        }
                        else
                        {
                            intent = PersonActivity.newIntent(PersonActivity.this,
                                    mFullPerson.getChild());
                        }
                    }
                    startActivityForResult(intent,1);
                    return true;
                }
            });
        }

        @Override
        public int getGroupCount() {
            return mListDataHeaders.size();
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            return mListHashMap.get(mListDataHeaders.get(groupPosition)).size();
        }
        @Override
        public Object getGroup(int groupPosition) {
            return mListDataHeaders.get(groupPosition);
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mListHashMap.get(mListDataHeaders.get(groupPosition)).get(childPosition);
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if(convertView == null)
            {
                LayoutInflater layoutInflater = LayoutInflater.from(PersonActivity.this);
                convertView = layoutInflater.inflate(R.layout.list_header_person, null);
            }
            TextView header = convertView.findViewById(R.id.Person_List_Header);
            header.setText(headerTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String childTitle = (String) getChild(groupPosition, childPosition);
            if(convertView == null)
            {
                LayoutInflater layoutInflater = LayoutInflater.from(PersonActivity.this);
                convertView = layoutInflater.inflate(R.layout.list_item_person, null);
            }

            TextView info = convertView.findViewById(R.id.Person_List_Item_Info);
            info.setText(childTitle);
            ImageView iconView = convertView.findViewById(R.id.Person_List_Icon);
            IconDrawable icon;
            int color;
            if(groupPosition == 0)
            {
                icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_pin);
                color = R.color.neutral;
            }
            else
            {

                if(mListHashMap.get(mListDataHeaders.get(groupPosition))
                        .get(childPosition).contains("FATHER")) {
                    icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male);
                    color = R.color.colorMaleIcon;
                }
                else if(mListHashMap.get(mListDataHeaders.get(groupPosition)
                ).get(childPosition).contains("MOTHER"))
                {
                    icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female);
                    color = R.color.colorFemaleIcon;
                }
                else if(mListHashMap.get(mListDataHeaders.get(groupPosition))
                        .get(childPosition).contains("SPOUSE")
                        && FamilyTree.getPerson(mFullPerson.getSpouse()).getGender().equals("m"))
                {
                    icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male);
                    color = R.color.colorMaleIcon;
                }
                else if(mListHashMap.get(mListDataHeaders.get(groupPosition))
                        .get(childPosition).contains("SPOUSE")
                        && FamilyTree.getPerson(mFullPerson.getSpouse()).getGender().equals("f"))
                {
                    icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female);
                    color = R.color.colorFemaleIcon;
                }
                else if(FamilyTree.getPerson(mFullPerson.getChild()).getGender().equals("m"))
                {
                    icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male);
                    color = R.color.colorMaleIcon;
                }
                else
                {
                    icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female);
                    color = R.color.colorFemaleIcon;
                }
            }
            iconView.setImageDrawable(icon.colorRes(color));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
