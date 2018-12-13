package Model;

public class Event {

    private String descendant;
    private String event_id;
    private String person_id;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String event_type;
    private int year;

    public Event(String descendant, String event_id, String person_id, float latitude, float longitude, String country,
                 String city, String event_type, int year)
    {
        this.descendant = descendant;
        this.event_id = event_id;
        this.person_id = person_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.event_type = event_type;
        this.year = year;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEvent_type() {
        return event_type;
    }

    public int getYear() {
        return year;
    }
}
