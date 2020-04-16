package id.ac.umn.wisuh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView cobaDoang;
    public String email,pNumber,fName,lName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cobaDoang = findViewById(R.id.awal);
        Intent intent = getIntent();
        email = (String) intent.getStringExtra("email");
        pNumber = intent.getStringExtra("nomorHp");
        fName = intent.getStringExtra("fname");
        lName = intent.getStringExtra("lname");
        Button btnProfil = findViewById(R.id.btnProfil);

        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("nomorHp",pNumber);
                intent.putExtra("fname",fName);
                intent.putExtra("lname",lName);
                startActivity(intent);

            }
        });
//        cobaDoang.setText(email+pNumber+fName+lName);
    }
}
