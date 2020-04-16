package id.ac.umn.wisuh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    //Declaration EditTexts
    EditText etEmail;
    EditText etPassword;

    //Declaration Button
    Button btnLogin;

    //Declaration SqliteHelper
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sqliteHelper = new SqliteHelper(this);
        initRegister();
        initViews();

        //set click event of login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check user input is correct or not
                if (validate()) {

                    //Get values from EditText fields
                    String Email = etEmail.getText().toString();
                    String Password = etPassword.getText().toString();

                    //Authenticate user
                    Customer currentUser = sqliteHelper.Authenticate(new Customer(null, null,null,null, Email, Password));

                    //Check Authentication is successful or not
                    if (currentUser != null) {
                        Snackbar.make(btnLogin, "Successfully Logged in!", Snackbar.LENGTH_LONG).show();

                        //User Logged in Successfully Launch You home screen activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email",currentUser.email);
                        intent.putExtra("nomorHp",currentUser.pNumber);
                        intent.putExtra("fname",currentUser.fName);
                        intent.putExtra("lname",currentUser.lName);
                        startActivity(intent);
                        finish();

                    } else {
                        //User Logged in Failed
                        Snackbar.make(btnLogin, "Incorrect Email or Password!", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    //this method used to set Create account TextView text and click event( maltipal colors
    // for TextView yet not supported in Xml so i have done it programmatically)
    private void initRegister() {
        TextView tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

    }

    //This method is for handling fromHtml method deprecation
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String Email = etEmail.getText().toString();
        String Password = etPassword.getText().toString();

        //Handling validation for Email field
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
//            Toast.makeText(getApplicationContext(),"Invalid Email Format!",Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }


        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
//            Toast.makeText(getApplicationContext(),"Password can't be empty!",Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
//            if (Password.length() > 5) {
//                valid = true;
//            } else {
//                valid = false;
//                Toast.makeText(getApplicationContext(),"Password is too short!",Toast.LENGTH_SHORT).show();
//            }
        }
        return valid;
    }
}
