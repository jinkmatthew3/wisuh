package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    //Declaration EditTexts
    EditText etEmail;
    EditText etPassword;

    //Declaration Button
    Button btnLogin;

    //Declaration SqliteHelper
    SqliteHelper sqliteHelper;

    //Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //Firebase Firestore
    private FirebaseFirestore db;

    //ProgressBar
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() == null) {
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

                        //loading progress Bar
                        progressBar.setVisibility(View.VISIBLE);

                        //Authenticate user
                        Customer currentUser = sqliteHelper.Authenticate(new Customer(null, null,null,null, Email, Password));

                        signIn(Email,Password);

                    /*//Check Authentication is successful or not
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
                    }*/
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
        //Ambil document dengan index user getUid terus masukkin ke String buat ke Intent berikutnya
        if(user != null){
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String email = document.getString("email");
                            String pNumber = document.getString("pNumber");
                            String fName = document.getString("fName");
                            String lName = document.getString("lName");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("email",email);
                            intent.putExtra("nomorHp",pNumber);
                            intent.putExtra("fname",fName);
                            intent.putExtra("lname",lName);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d("signIn", "No such document");
                        }
                    } else {
                        Log.d("signIn", "get failed with ", task.getException());
                    }
                }
            });
        }

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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

    private void signIn(String Email, String Password){
        mAuth.signInWithEmailAndPassword(Email, Password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();
                    Log.d("signIn",user.getUid());

                    //Ambil document dengan index user getUid terus masukkin ke String buat ke Intent berikutnya
                    DocumentReference docRef = db.collection("users").document(user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String email = document.getString("email");
                                    String pNumber = document.getString("pNumber");
                                    String fName = document.getString("fName");
                                    String lName = document.getString("lName");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("email",email);
                                    intent.putExtra("nomorHp",pNumber);
                                    intent.putExtra("fname",fName);
                                    intent.putExtra("lname",lName);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.d("signIn", "No such document");
                                }
                            } else {
                                Log.d("signIn", "get failed with ", task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
