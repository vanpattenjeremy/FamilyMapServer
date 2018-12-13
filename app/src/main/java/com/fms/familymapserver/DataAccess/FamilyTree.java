package com.fms.familymapserver.DataAccess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import Model.Event;
import Model.Person;
import Result.EventsResult;
import Result.PersonsResult;

public class FamilyTree {
    private static EventsResult sEvents;
    private static PersonsResult sPersons;
    private static Map<String, Event> sEventMap;    //this one
    private static Map<String, Person> sPersonMap;
    private static Map<String, FullPerson> sFullPersonMap;  //this one
    private static String mAuthToken;
    private static String sUserPerson;  //this one
    private static ArrayList<String> sEventTypeList;
    private static HashMap<String,Boolean> sEventTypeFilters = new HashMap<>(); //this one maybe
    private static EventsResult sFilteredEvents;
    private static Map<String, FullPerson> sFilteredFullPersonMap;
    private static String sHost;
    private static String sPort;

    private FamilyTree()
    {
        generateFullPersonMap();
    }

    public static FullPerson getFullPerson(String personId)
    {
        return sFilteredFullPersonMap.get(personId);
    }

    public static void generateFullPersonMap()
    {
        sFullPersonMap = new HashMap<>();
        sEventTypeList = new ArrayList<>();
        HashSet<String> eventTypeSet = new HashSet<>();

        if(sEvents == null || sPersons == null)
        {
            return;
        }
        for(Event event:sEvents.getData())
        {
            String personId = event.getPerson_id();
            if(!sFullPersonMap.containsKey(personId))
            {
                Person person = sPersonMap.get(personId);
                sFullPersonMap.put(personId,
                        new FullPerson(person.getDescendant(),personId,person.getFirst_name(),
                                person.getLast_name(),person.getGender()));
                sFullPersonMap.get(personId).setFather(person.getFather());
                sFullPersonMap.get(personId).setMother(person.getMother());
                sFullPersonMap.get(personId).setSpouse(person.getSpouse());
            }
            sFullPersonMap.get(personId).addEvent(event.getEvent_id());
            eventTypeSet.add(event.getEvent_type().toLowerCase());
        }

        for(String type:eventTypeSet)
        {
            sEventTypeList.add(type);
            sEventTypeFilters.put(type,true);
        }
        sEventTypeFilters.put("male",true);
        sEventTypeFilters.put("female",true);
        sEventTypeFilters.put("mother",true);
        sEventTypeFilters.put("father",true);
        for(Person child:sPersons.getData())
        {
            String childId = child.getPerson_id();
            String fatherId = child.getFather();
            String motherId = child.getMother();
            if(fatherId != null) {
                sFullPersonMap.get(fatherId).addChild(childId);
            }
            if(motherId != null) {
                sFullPersonMap.get(motherId).addChild(childId);
            }
        }
        sEventTypeList.add("male");
        sEventTypeList.add("female");
        sEventTypeList.add("father");
        sEventTypeList.add("mother");
        sFilteredFullPersonMap = sFullPersonMap;
    }

    public static EventsResult getEvents() {
        return sFilteredEvents;
    }

    public static void setEvents(EventsResult events) {
        sEvents = events;
        sFilteredEvents = events;
        if(events == null)
        {
            return;
        }
        ArrayList<Event> eventsList = sEvents.getData();
        sEventMap = new HashMap<>();
        for(Event event:eventsList)
        {
            sEventMap.put(event.getEvent_id(),event);
        }
    }

    public static Event getEvent(String eventId)
    {
        return sEventMap.get(eventId);
    }

    public static PersonsResult getPersons() {
        return sPersons;
    }

    public static Person getPerson(String personId)
    {
        return sPersonMap.get(personId);
    }

    public static void setPersons(PersonsResult persons) {
        sPersons = persons;
        if(persons == null)
        {
            return;
        }
        ArrayList<Person> personsList = sPersons.getData();
        sPersonMap = new HashMap<>();
        for(Person person:personsList)
        {
            sPersonMap.put(person.getPerson_id(),person);
        }
    }

    public static String getAuthToken() {
        return mAuthToken;
    }

    public static void setAuthToken(String authToken) {
        mAuthToken = authToken;
    }

    public static void setUserPerson(String personId)
    {
        sUserPerson = personId;
    }

    public static ArrayList<String> getEventTypeList()
    {
        return sEventTypeList;
    }

    public static void setEventTypeFilter(String type,boolean isOn)
    {
        sEventTypeFilters.put(type,isOn);
        generateFilteredEvents();
    }

    public static boolean getEventTypeFilter(String type)
    {
        return sEventTypeFilters.get(type);
    }

    private static void generateFilteredEvents()
    {
        ArrayList<Event> filteredEvents = new ArrayList<>();
        sFilteredFullPersonMap = new HashMap<>();
        for(Event event:sEvents.getData())
        {
            if(sEventTypeFilters.get(event.getEvent_type().toLowerCase()))
            {
                String personId = event.getPerson_id();
                if(!sFilteredFullPersonMap.containsKey(personId))
                {
                    FullPerson person = sFullPersonMap.get(personId);
                    sFilteredFullPersonMap.put(personId,
                            new FullPerson(person.getDescendant(),personId,person.getFirst_name(),
                                    person.getLast_name(),person.getGender()));
                    sFilteredFullPersonMap.get(personId).setFather(person.getFather());
                    sFilteredFullPersonMap.get(personId).setMother(person.getMother());
                    sFilteredFullPersonMap.get(personId).setSpouse(person.getSpouse());
                    sFilteredFullPersonMap.get(personId).addChild(person.getChild());
                }
                if((sFilteredFullPersonMap.get(personId).getGender().equals("m")
                        && sEventTypeFilters.get("male"))
                        || (sFilteredFullPersonMap.get(personId).getGender().equals("f")
                        && sEventTypeFilters.get("female"))) {
                    filteredEvents.add(event);
                    sFilteredFullPersonMap.get(personId).addEvent(event.getEvent_id());
                }
            }
        }
        sFilteredEvents = new EventsResult(filteredEvents);
        generateFilteredPeople();
    }

    private static void generateFilteredPeople()
    {
        HashMap<String, FullPerson> tempFilteredPersons = new HashMap<>();
        FullPerson root = sFilteredFullPersonMap.get(sUserPerson);
        if(root == null)
        {
            root = sFullPersonMap.get(sUserPerson);
        }
        else
        {
            tempFilteredPersons.put(sUserPerson,root);
        }
        if(sEventTypeFilters.get("father"))
        {
                if(sFilteredFullPersonMap.get(root.getFather()) != null) {
                    tempFilteredPersons.put(root.getFather(), sFilteredFullPersonMap.get(root.getFather()));
                    addParents(tempFilteredPersons,sFilteredFullPersonMap.get(root.getFather()));
                }
        }
        if(sEventTypeFilters.get("mother"))
        {
                if(sFilteredFullPersonMap.get(root.getMother()) != null) {
                    tempFilteredPersons.put(root.getMother(), sFilteredFullPersonMap.get(root.getMother()));
                    addParents(tempFilteredPersons,sFilteredFullPersonMap.get(root.getMother()));
                }
        }

        sFilteredFullPersonMap = tempFilteredPersons;
        ArrayList<Event> tempFilteredEvents = new ArrayList<>();
        for(String key:sFilteredFullPersonMap.keySet())
        {
            if(sFilteredFullPersonMap.get(key).getEvents() != null) {
                for (String eventId : sFilteredFullPersonMap.get(key).getEvents()) {
                    tempFilteredEvents.add(sEventMap.get(eventId));
                }
            }
        }
        sFilteredEvents = new EventsResult(tempFilteredEvents);
    }

    private static void addParents(HashMap<String, FullPerson> persons, Person person)
    {
        if(person.getFather() != null) {
                persons.put(person.getFather(), sFilteredFullPersonMap.get(person.getFather()));
            addParents(persons,sFilteredFullPersonMap.get(person.getFather()));
        }
        if(person.getMother() != null) {
                persons.put(person.getMother(), sFilteredFullPersonMap.get(person.getMother()));
            addParents(persons,sFilteredFullPersonMap.get(person.getMother()));
        }
    }

    public static ArrayList<Person> searchPerson(String searchString)
    {
        ArrayList<Person> searchResults = new ArrayList<>();
        for(String key:sFilteredFullPersonMap.keySet())
        {
            FullPerson person = sFilteredFullPersonMap.get(key);
            if(person.getFirst_name().toLowerCase().contains(searchString.toLowerCase())
                    || person.getLast_name().toLowerCase().contains(searchString.toLowerCase()))
            {
                searchResults.add(person);
            }
        }
        return searchResults;
    }

    public static ArrayList<Event> searchEvent(String searchString)
    {
        ArrayList<Event> searchResults = new ArrayList<>();
        for(Event event:sFilteredEvents.getData())
        {
            if(event.getCountry().toLowerCase().contains(searchString.toLowerCase())
                    || event.getCity().toLowerCase().contains(searchString.toLowerCase())
                    || event.getEvent_type().toLowerCase().contains(searchString.toLowerCase())
                    || Integer.toString(event.getYear()).contains(searchString.toLowerCase()))
            {
                searchResults.add(event);
            }
        }
        return searchResults;
    }

    public static FullPerson getSafeFullPerson(String personId)
    {
        return sFullPersonMap.get(personId);
    }

    public static String getHost() {
        return sHost;
    }

    public static void setHost(String host) {
        sHost = host;
    }

    public static String getPort() {
        return sPort;
    }

    public static void setPort(String port) {
        sPort = port;
    }
}
