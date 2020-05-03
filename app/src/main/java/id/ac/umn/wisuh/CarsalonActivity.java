package id.ac.umn.wisuh;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class CarsalonActivity extends AppCompatActivity {
    LinearLayout llayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carsalon);


        for (int i = 1; i <= 7; i++) {
            RelativeLayout rlayout = new RelativeLayout(this);


            ImageView imgView = new ImageView(this);
            imgView.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
//            imgView.setMaxHeight(510);
//            imgView.setMaxWidth(215);
            imgView.setBackgroundResource(R.drawable.bar_icon);
            //TextView textView = new TextView(this);
            //textView.setText("TextView " + String.valueOf(i));
            llayout.addView(imgView);
        }
    }
}
