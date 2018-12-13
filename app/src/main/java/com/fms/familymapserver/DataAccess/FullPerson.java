package com.fms.familymapserver.DataAccess;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Model.Person;

public class FullPerson extends Person {
    public FullPerson(String descendant, String person_id, String first_name, String last_name, String gender) {
        super(descendant, person_id, first_name, last_name, gender);
    }

    private String child;
    private ArrayList<String> events;

    public String getChild() {
        return child;
    }

    public void setEvents(ArrayList<String> events)
    {
        this.events = events;
    }

    public void addEvent(String event)
    {
        if(events == null)
        {
            events = new ArrayList<>();
        }
        events.add(event);
    }

    public void addChild(String childId)
    {
        child = childId;
    }

    public ArrayList<String> getEvents() {
        return events;
    }
}
