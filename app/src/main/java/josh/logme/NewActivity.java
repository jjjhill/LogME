package josh.logme;

import android.animation.TypeConverter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.view.View;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Date;


public class NewActivity extends AppCompatActivity {
    private static final String TAG = NewActivity.class.getSimpleName();
    public String SETTINGS_FILE = "Settings.txt";
    public double ratioC2I;
    public double correctionFactor;
    public double targetBG = 6.0;
    public double carb, currentBG, totalDose;
    public Entry currentEntry;
    public Settings settings;
    public String pageData = "";
    public WebView webView;
    public String url = "https://www.nutritionvalue.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        NumberPicker carbNP = (NumberPicker) findViewById(R.id.carb_input);
        carbNP.setMinValue(0);
        carbNP.setMaxValue(250);
        carbNP.setValue(10);
        carbNP.setWrapSelectorWheel(false);

        NumberPicker bg1 = (NumberPicker) findViewById(R.id.bg_input_whole);
        bg1.setMinValue(0);
        bg1.setMaxValue(25);
        bg1.setValue(5);
        bg1.setWrapSelectorWheel(false);

        NumberPicker bg2 = (NumberPicker) findViewById(R.id.bg_input_decimal);
        bg2.setMinValue(0);
        bg2.setMaxValue(9);
        bg2.setValue(0);
        bg2.setWrapSelectorWheel(true);

        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setEnabled(false);

        EditText txt = (EditText) findViewById(R.id.txtCustom);
        txt.setEnabled(false);

        currentEntry = new Entry();

        try {
            FileInputStream fis = openFileInput(SETTINGS_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            settings = JsonUtil.settingsFromJson(br.readLine());
            ratioC2I = settings.ratio; //get "carb to insulin ratio" from file
            correctionFactor = settings.correction;
        }catch(Exception e) {
            e.printStackTrace();
        }



        webView = (WebView) findViewById(R.id.webView1);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
    public void get_dose (View view) {
        double iDue2Correction, iDue2Carb;
        NumberPicker bg1 = (NumberPicker) findViewById(R.id.bg_input_whole);
        NumberPicker bg2 = (NumberPicker) findViewById(R.id.bg_input_decimal);
        NumberPicker carbNP = (NumberPicker) findViewById(R.id.carb_input);
        TextView txtDisplay = (TextView) findViewById(R.id.txtDosage);

        carb = (double)carbNP.getValue();
        currentBG = (double)bg1.getValue() + ((double)bg2.getValue() / 10);
        //Log.d(TAG, "carb: " + carb + ". glucose: " + glucose + ".");

        iDue2Carb = carb / ratioC2I;
        iDue2Correction = (currentBG - targetBG) / correctionFactor;
        totalDose = iDue2Carb + iDue2Correction;

        txtDisplay.setText("You should take " + totalDose + " units of bolus.");
        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setEnabled(true);

    }

    public void checkCustom (View view) {
        RadioButton rbcustom = (RadioButton) findViewById(R.id.rb_Custom);
        EditText custom = (EditText) findViewById(R.id.txtCustom);
        if (rbcustom.isChecked())
            custom.setEnabled(true);
        else
            custom.setEnabled(false);

    }

    public void save (View view) throws IOException {
        double amount;
        RadioButton rbcustom = (RadioButton) findViewById(R.id.rb_Custom);
        EditText custom = (EditText) findViewById(R.id.txtCustom);
        NumberPicker carbNP = (NumberPicker) findViewById(R.id.carb_input);
        //custom dosage
        if (rbcustom.isChecked()){
            if (!custom.getText().toString().matches("")) {
                amount = Double.parseDouble(custom.getText().toString());
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No dosage specified").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();

                builder.show();
                return;
            }
        }
        //recommended dosage
        else {
            amount = totalDose;
        }

        /* Save data*/
        currentEntry.datetime = (new Date()).toString();
        currentEntry.bg = currentBG;
        currentEntry.dose = amount;
        currentEntry.carbs = (double)carbNP.getValue();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setTitle("Add a note!").setView(input).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                try {
                    //store notes
                    String notes = input.getText().toString();
                    currentEntry.notes = notes;
                    writeJson();

                    currentEntry = new Entry(); //erase entry data
                    open_log();
                }catch(IOException e){
                    Log.d(TAG, e.toString());
                }

            }
        }).create();
        builder.show();

         /* ****** */

    }

    public void writeJson() throws IOException{
        String FILENAME = "log.txt";
        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
        String strEntry = JsonUtil.toJSon(currentEntry) + "\n";
        fos.write(strEntry.getBytes());
    }

    public void open_log(){
        Intent intent = new Intent(this, LogActivity.class);
        startActivity(intent);
    }

    //for debugging
    public void erase_mem(View view) throws IOException{
        String simpleFileName = "log.txt";
        deleteFile(simpleFileName);


        //set searchbar innerhtml here
//        webView.loadUrl("javascript:(function(){"+
//                "l=document.getElementById('food_query');"+
//                "e=document.createEvent('HTMLEvents');"+
//                "e.initEvent('click',true,true);"+
//                "l.dispatchEvent(e);"+
//                "})()");
//
//        Log.d(TAG, "url: " + webView.getUrl());
//
//        //String html = URLDecoder.decode(url, "UTF-8").substring(9);
//
//        getUrlData urlData = new getUrlData();
//        urlData.execute();
    }

    private class getUrlData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params){
            Document document = null;
            Element list;

            String searchPage = "https://www.nutritionvalue.org/";
            try {
                document = Jsoup.connect(searchPage).get();
            }catch(IOException e) {
                e.printStackTrace();
            }

            if (document != null)
                list = document.getElementById("new_food_list");
            else
                list = null;


            pageData = list.toString();
            return pageData;
        }
    }
}
