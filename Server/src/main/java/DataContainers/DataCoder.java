package DataContainers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class DataCoder {

    public static Places decodePlaces()
    {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("C:\\Users\\jerem\\AndroidStudioProjects\\FamilyMapServer\\Server\\libs\\locations.json"));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Could not find the file");
        }
        Places places = gson.fromJson(bufferedReader,Places.class);


        return places;
        //"C:\\Users\\jerem\\AndroidStudioProjects\\FamilyMapServer\\Server\\libs\\locations.json"
    }

    public static Names decodeNames()
    {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("C:\\Users\\jerem\\AndroidStudioProjects\\FamilyMapServer\\Server\\libs\\nameData.json"));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Could not find the file");
        }
        Names names = gson.fromJson(bufferedReader,Names.class);

        return names;
    }
}
