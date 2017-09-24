package josh.logme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.app.ListActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    String FILENAME = "log.txt";
    private static final String TAG = LogActivity.class.getSimpleName();
    public List<Entry> entries = new ArrayList<Entry>();
    private ArrayList<HashMap<String, String>> list;
    public static final String FIRST_COLUMN="Time";
    public static final String SECOND_COLUMN="B.G.";
    public static final String THIRD_COLUMN="Dose";
    public static final String FOURTH_COLUMN="Carbs";
    public static final String FIFTH_COLUMN="Notes";

    HashMap<String,String> hashmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Entry current;
        list = new ArrayList<>();

        /* DEBUG : try to read from that file*/
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String s;
            while ((s = br.readLine()) != null) {
                current = JsonUtil.fromJson(s);
                entries.add(current);
            }
            populateList();

            ListView lv = (ListView)findViewById(R.id.list_view);
            ListViewAdapter adapter = new ListViewAdapter(this, list);
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.log_list, R.id.txtTime, times);
            lv.setAdapter(adapter);


        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

        /* ******* */

    }

    public void populateList(){
        for (Entry entry : entries){
            hashmap = new HashMap<>();
            hashmap.put(FIRST_COLUMN, entry.datetime);
            hashmap.put(SECOND_COLUMN, Double.toString(entry.bg));
            hashmap.put(THIRD_COLUMN, String.format("%.2f", entry.dose));
            hashmap.put(FOURTH_COLUMN, Double.toString(entry.carbs));
            hashmap.put(FIFTH_COLUMN, entry.notes);
            list.add(hashmap);
        }
        //headings added here for now, (because of implementation of ListViewAdapter adding entries in reverse order (newest first))
        hashmap = new HashMap<>();
        hashmap.put(FIRST_COLUMN, "TIME");
        hashmap.put(SECOND_COLUMN, "B.G.");
        hashmap.put(THIRD_COLUMN, "DOSE");
        hashmap.put(FOURTH_COLUMN, "CARB");
        hashmap.put(FIFTH_COLUMN, "NOTES");
        list.add(hashmap);
    }
}
