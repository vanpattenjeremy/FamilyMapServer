package com.fms.familymapserver.UI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.fms.familymapserver.DataAccess.FamilyTree;
import com.fms.familymapserver.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private RecyclerView mFilterRecycleView;
    private FilterAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mFilterRecycleView = findViewById(R.id.Filter_Recycler_View);
        mFilterRecycleView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();
    }

    private void updateUI()
    {
        mAdapter = new FilterAdapter(FamilyTree.getEventTypeList());
        mFilterRecycleView.setAdapter(mAdapter);
    }

    private class FilterHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private Switch mSwitch;
        private String mType;

        public FilterHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_filter, parent, false));

            mTitleTextView = itemView.findViewById(R.id.Filter_Title);
            mDescriptionTextView = itemView.findViewById(R.id.Filter_Description);
            mSwitch = itemView.findViewById(R.id.Filter_Switch);
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    FamilyTree.setEventTypeFilter(mType,isChecked);
                }
            });
        }

        public void bind(String type)
        {
            mType = type;
            mTitleTextView.setText(mType + " Events");
            mDescriptionTextView.setText("FILTER BY " + mType.toUpperCase() + " EVENTS");
            mSwitch.setChecked(FamilyTree.getEventTypeFilter(mType));
        }
    }

    private class FilterAdapter extends RecyclerView.Adapter{

        private ArrayList<String> mFilters;

        public FilterAdapter(ArrayList<String> filters)
        {
            mFilters = filters;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FilterActivity.this);
            return new FilterHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            String type = FamilyTree.getEventTypeList().get(position);
            ((FilterHolder)holder).bind(type);
        }

        @Override
        public int getItemCount() {
            return mFilters.size();
        }
    }
}
