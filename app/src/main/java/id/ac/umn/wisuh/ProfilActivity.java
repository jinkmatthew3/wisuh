package id.ac.umn.wisuh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProfilActivity extends AppCompatActivity {

    TextView uname, nohp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        uname = findViewById(R.id.username);
        nohp = findViewById(R.id.noHp);
        Intent intent = getIntent();

//        final String email = intent.getStringExtra("email");
        final String pNumber = intent.getStringExtra("nomorHp");
        final String fName = intent.getStringExtra("fname");
        final String lName = intent.getStringExtra("lname");
        uname.setText(fName+" "+lName);
        nohp.setText(pNumber);

    }
}
