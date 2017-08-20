package josh.logme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void open_new(View view) {
        Intent intent = new Intent(this, NewActivity.class);
        startActivity(intent);
    }

    public void open_log(View view) {
        Intent intent = new Intent(this, NewActivity.class);
        startActivity(intent);
    }

    public void open_settings(View view) {
        Intent intent = new Intent(this, NewActivity.class);
        startActivity(intent);
    }
}
