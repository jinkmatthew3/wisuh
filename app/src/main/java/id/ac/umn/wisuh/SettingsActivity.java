package id.ac.umn.wisuh;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    Switch switch01,switch02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_blue_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //  end of toolbar code

        switch01 = findViewById(R.id.switch01);
        switch02 = findViewById(R.id.switch02);
        SharedPreferences sharedPrefs = getSharedPreferences("Settings", MODE_PRIVATE);
        switch01.setChecked(sharedPrefs.getBoolean("notif", true));
        switch02.setChecked(sharedPrefs.getBoolean("sound", false));

//        Switch notif preference
        switch01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                    editor.putBoolean("notif", true);
                    editor.apply();
                } else {
                    // The toggle is disabled
                    SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                    editor.putBoolean("notif", false);
                    editor.apply();
                }
            }
        });

        //Check log
        if(switch01.isChecked()){
            Log.i("Notif ", "on");
        } else {
            Log.i("Notif ", "off");
        }
//        End of switch01

//        switch02 Sound preference
        switch02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                    editor.putBoolean("sound", true);
                    editor.apply();
                } else {
                    // The toggle is disabled
                    SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                    editor.putBoolean("sound", false);
                    editor.apply();
                }
            }
        });

        //Check log
        if(switch02.isChecked()){
            Log.i("Sound ", "on");
        } else {
            Log.i("Sound ", "off");
        }
    }
}