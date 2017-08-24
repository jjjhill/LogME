package josh.logme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.view.View;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewActivity extends AppCompatActivity {
    private static final String TAG = NewActivity.class.getSimpleName();
    public int ratioC2I;
    public double correctionFactor = 2.0;
    public double targetBG = 6.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        NumberPicker carbNP = (NumberPicker) findViewById(R.id.carb_input);
        carbNP.setMinValue(10);
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



        ratioC2I = 20; //get "carb to insulin ratio" from database
    }
    public void get_dose(View view){
        double carb, currentBG, recommendedDose, iDue2Correction, iDue2Carb, totalDose;
        NumberPicker bg1 = (NumberPicker) findViewById(R.id.bg_input_whole);
        NumberPicker bg2 = (NumberPicker) findViewById(R.id.bg_input_decimal);
        NumberPicker carbNP = (NumberPicker) findViewById(R.id.carb_input);
        TextView txtDisplay = (TextView) findViewById(R.id.txtDebug);

        carb = (double)carbNP.getValue();
        currentBG = (double)bg1.getValue() + ((double)bg2.getValue() / 10);
        //Log.d(TAG, "carb: " + carb + ". glucose: " + glucose + ".");

        iDue2Carb = carb / ratioC2I;
        iDue2Correction = (currentBG - targetBG) / correctionFactor;
        totalDose = iDue2Carb + iDue2Correction;

        txtDisplay.setText("You should take " + totalDose + " units of bolus.");

    }

}
