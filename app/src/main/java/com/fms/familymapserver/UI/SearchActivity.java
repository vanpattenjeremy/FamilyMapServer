package com.fms.familymapserver.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.fms.familymapserver.DataAccess.FamilyTree;
import com.fms.familymapserver.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mSearchRecyclerView;
    private SearchAdapter mAdapter;
    private EditText mSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Iconify.with(new FontAwesomeModule());
        mSearchRecyclerView = findViewById(R.id.Search_Result_Recycler);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }

    private void updateUI()
    {
        mSearchBar = findViewById(R.id.Search_Bar);
        mSearchBar.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Person> people = FamilyTree.searchPerson(s.toString());
                ArrayList<Event> events = FamilyTree.searchEvent(s.toString());
                mAdapter = new SearchAdapter(people,events);
                mSearchRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private String mType;
        private String mText;
        private Event mEvent;
        private Person mPerson;
        private ImageView mIconView;
        private TextView mTextView;
        public SearchHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_search, parent, false));

            mIconView = itemView.findViewById(R.id.Search_Icon);
            mTextView = itemView.findViewById(R.id.Search_Description);
            itemView.setOnClickListener(this);
        }

        public void bind(String type, Event event)
        {
            mType = type;
            mEvent = event;
            String personName = FamilyTree.getPerson(event.getPerson_id()).getFirst_name()
                    + " " + FamilyTree.getPerson(event.getPerson_id()).getLast_name();
            mText = event.getEvent_type() + ":" + event.getCity() + "," + event.getCountry()
                    + "(" + event.getYear() + ")\n" + personName;

            IconDrawable icon;
            int color;
            icon =  new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_pin);
            color = R.color.neutral;
            mIconView.setImageDrawable(icon.colorRes(color));
            mTextView.setText(mText);
        }

        public void bind(String type, Person person)
        {
            mType = type;
            mPerson = person;

            mText = person.getFirst_name() + " " + person.getLast_name();

            IconDrawable icon;
            int color;

            if(mType.equals("m"))
            {
                icon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male);
                color = R.color.colorMaleIcon;
            }
            else
            {
                icon =  new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female);
                color = R.color.colorFemaleIcon;
            }
            mIconView.setImageDrawable(icon.colorRes(color));
            mTextView.setText(mText);
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            if(mType.equals("event"))
            {
                intent = EventActivity.newIntent(SearchActivity.this,mEvent.getEvent_id());
            }
            else
            {
                intent = PersonActivity.newIntent(SearchActivity.this,mPerson.getPerson_id());
            }
            startActivity(intent);
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter{

        ArrayList<Person> mPersonResults;
        ArrayList<Event> mEventResults;

        public SearchAdapter(ArrayList<Person> personResults, ArrayList<Event> eventResutls)
        {
            mPersonResults = personResults;
            mEventResults = eventResutls;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SearchActivity.this);
            return new SearchActivity.SearchHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            String type;
            if(position >= mPersonResults.size())
            {
                Event event = mEventResults.get(position - mPersonResults.size());
                type = "event";
                ((SearchHolder)holder).bind(type,event);
            }
            else
            {
                Person person = mPersonResults.get(position);
                type = person.getGender();
                ((SearchHolder)holder).bind(type, person);
            }

        }

        @Override
        public int getItemCount() {
            return mPersonResults.size() + mEventResults.size();
        }
    }
}
