package josh.logme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.view.View;

public class NewActivity extends AppCompatActivity {
    public int ratioC2I;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        NumberPicker np = (NumberPicker) findViewById(R.id.carb_input);
        np.setMinValue(10);
        np.setMaxValue(250);
        np.setValue(10);
        np.setWrapSelectorWheel(false);

        //ratioC2I = (int); //get carb to insulin ratio from database
    }

    public void get_dose(View view){

    }
}
