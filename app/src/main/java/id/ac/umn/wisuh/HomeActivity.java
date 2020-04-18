package id.ac.umn.wisuh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity  extends AppCompatActivity {
    private ImageButton accountbtn, activitybtn, lovebtn, settingbtn, nearbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        accountbtn = findViewById(R.id.accountbtn);
        activitybtn = findViewById(R.id.activitybtn);
        lovebtn = findViewById(R.id.lovebtn);
        settingbtn = findViewById(R.id.settingbtn);
        nearbtn = findViewById(R.id.nearbybtn);


        nearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindahnear = new Intent(HomeActivity.this, NearbyActivity.class);
                startActivity(pindahnear);
            }
        });

    }
}
