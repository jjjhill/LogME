package josh.logme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SettingsActivity extends AppCompatActivity {
    Settings settings;
    EditText ratio, correction, target;
    String SETTINGS_FILE = "Settings.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {
            FileInputStream fis = openFileInput(SETTINGS_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            settings = JsonUtil.settingsFromJson(br.readLine());
        }catch(Exception e){e.printStackTrace();}


        ratio = (EditText) findViewById(R.id.editRatio);
        correction = (EditText) findViewById(R.id.editCorrection);
        target = (EditText) findViewById(R.id.editTarget);

        if (settings != null) {
            ratio.setText(Double.toString(settings.ratio));
            correction.setText(Double.toString(settings.correction));
            target.setText(Double.toString(settings.targetBG));
        }
        settings = new Settings();
    }

    public void saveSettings(View view) throws IOException {
        settings.ratio = Double.parseDouble(ratio.getText().toString());
        settings.correction = Double.parseDouble(correction.getText().toString());
        settings.targetBG = Double.parseDouble(target.getText().toString());
        FileOutputStream fos = openFileOutput(SETTINGS_FILE, Context.MODE_PRIVATE);
        String strEntry = JsonUtil.settingsToJSon(settings);
        fos.write(strEntry.getBytes());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
