package id.ac.umn.wisuh;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

public class CarwashActivity extends AppCompatActivity {
    LinearLayout llayout;
    private int btni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwash);

        llayout = findViewById(R.id.scrollview);
        btni = 1;

        //Adding secara dynamic
        for (int i = 1; i <= 7; i++) {
            RelativeLayout rlayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams imageButtonParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlayout.setId(i);
            rlayout.setPadding(20, 20, 20, 20);


            ImageButton imgbtn = new ImageButton(this);
            imgbtn.setLayoutParams(imageButtonParam);
            imgbtn.setId(btni);
            imgbtn.getLayoutParams().height = 510;
            imgbtn.getLayoutParams().width = 1100;
            imgbtn.setBackgroundResource(R.drawable.input_field);
            imgbtn.setPadding(16,16,16,16);
            imgbtn.setScaleType(ImageButton.ScaleType.FIT_START);
            imgbtn.setImageResource(R.drawable.car_washing_icon);

            TextView tview = new TextView(this);
            tview.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            tview.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            tview.setLayoutParams(imageButtonParam);
            tview.setText("Carwhas");
            tview.setTextSize(15);
            tview.setTypeface(Typeface.DEFAULT_BOLD);
            tview.setTypeface(Typeface.SANS_SERIF);
            tview.setTextColor(Color.BLACK);
            tview.setGravity(Gravity.BOTTOM);
//            tview.getMarginStart
//
//            android:layout_marginStart="-370dp"
//            android:layout_marginTop="150dp"
//            android:layout_toEndOf="@+id/btn_carwash"

            //TextView textView = new TextView(this);
            //textView.setText("TextView " + String.valueOf(i));
            llayout.addView(rlayout);
            rlayout.addView(imgbtn);
            rlayout.addView(tview);
            btni = btni + 1;
        }

    }
}
