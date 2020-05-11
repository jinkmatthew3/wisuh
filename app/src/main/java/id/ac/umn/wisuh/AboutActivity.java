package id.ac.umn.wisuh;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    AboutAdapter adapter;
    List<AboutModel> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

//        toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_blue_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        end of toolbar code

        models = new ArrayList<>();
        models.add(new AboutModel(R.drawable.girl, "Bella Anggraini", "bella.utomo@student.umn.ac.id"));
        models.add(new AboutModel(R.drawable.girl2, "Devira Paramitha", "devira.kurniawan@student.umn.ac.id"));
        models.add(new AboutModel(R.drawable.man2, "Dhammajoti", "dhammajoti@student.umn.ac.id"));
        models.add(new AboutModel(R.drawable.girl3, "Meilona Eurica Karmelia", "meilona.karmelia@student.umn.ac.id"));
        models.add(new AboutModel(R.drawable.man, "Michael Roni", "michael.roni@student.umn.ac.id"));

        adapter = new AboutAdapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color5)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
}