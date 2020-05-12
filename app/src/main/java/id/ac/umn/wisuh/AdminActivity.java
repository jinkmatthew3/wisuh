package id.ac.umn.wisuh;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.viewpager.widget.ViewPager;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import com.google.android.material.tabs.TabLayout;
        import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager2);
        tabLayout.addTab(tabLayout.newTab().setText("Top UP"));
        tabLayout.addTab(tabLayout.newTab().setText("Register Car Wash"));
        tabLayout.addTab(tabLayout.newTab().setText("Register Car Salon"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public boolean onCreateOptionsMenu(Menu menu_logout) {
        getMenuInflater().inflate(R.menu.menu_logout,menu_logout);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if ( id == R.id.logout_menu) {
            //ini isi intent ke logout menu login
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}



