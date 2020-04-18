package id.ac.umn.wisuh;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static androidx.constraintlayout.widget.ConstraintLayout.*;

public class NearbyActivity extends AppCompatActivity {
    ConstraintLayout clayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        //Adding secara dynamic
        for (int i = 1; i <= 2; i++) {
//            ImageView imgView = new ImageView(this);
            TextView textView = new TextView(this);
            textView.setText("TextView " + String.valueOf(i));
            clayout.addView(textView);
        }


    }
}
