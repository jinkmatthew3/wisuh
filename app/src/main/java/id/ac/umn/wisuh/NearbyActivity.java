package id.ac.umn.wisuh;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static androidx.constraintlayout.widget.ConstraintLayout.*;

public class NearbyActivity extends AppCompatActivity {
    LinearLayout clayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        clayout = findViewById(R.id.scrollview);

        //Adding secara dynamic
        for (int i = 1; i <= 7; i++) {
            ImageView imgView = new ImageView(this);
            imgView.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            imgView.setMaxHeight(510);
//            imgView.setMaxWidth(215);
            imgView.setBackgroundResource(R.drawable.bar_icon);
            //TextView textView = new TextView(this);
            //textView.setText("TextView " + String.valueOf(i));
            clayout.addView(imgView);
        }

    }
}
