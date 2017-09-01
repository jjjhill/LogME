package josh.logme;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Set;


/**
 * Created by Josh PC on 8/30/2017.
 */
public class JsonUtil {

    public static String toJSon(Entry entry) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("glucose", entry.bg); // Set the first name/pair
            jsonObj.put("dose", entry.dose);
            jsonObj.put("notes", entry.notes);
            jsonObj.put("date", entry.datetime);


            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }

    public static Entry fromJson(String json) {
        try {
            // Here we convert JSON to Java Object
            JSONObject jsonObj = new JSONObject(json);
            Entry entry = new Entry();
            entry.bg = jsonObj.getDouble("glucose");
            entry.dose = jsonObj.getDouble("dose");
            entry.notes = jsonObj.getString("notes");
            entry.datetime = jsonObj.getString("date");

            return entry;


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String settingsToJSon(Settings settings) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("ratio", settings.ratio); // Set the first name/pair
            jsonObj.put("correction", settings.correction);


            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }

    public static Settings settingsFromJson(String json) {
        try {
            // Here we convert JSON to Java Object
            JSONObject jsonObj = new JSONObject(json);
            Settings settings = new Settings();
            settings.ratio = jsonObj.getDouble("ratio");
            settings.correction = jsonObj.getDouble("correction");

            return settings;


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}