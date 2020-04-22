package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //Declaration EditTexts
    EditText etFName,etLName,etPhoneNum,etEmail,etPassword,etRePassword;

    //Declaration Button
    Button btnRegister;

    //Declaration SqliteHelper
    SqliteHelper sqliteHelper;

    //Firebase Authentication
    private FirebaseAuth mAuth;

    //Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sqliteHelper = new SqliteHelper(this);
        initLogin();
        initViews();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String FName = etFName.getText().toString();
                    String LName = etLName.getText().toString();
                    String PhoneNum = etPhoneNum.getText().toString();
                    String Email = etEmail.getText().toString();
                    String Password = etPassword.getText().toString();

                    register(Email,Password,PhoneNum,FName,LName);


//                    //Check in the database is there any user associated with  this email
//                    if (!sqliteHelper.isEmailExists(Email)) {
//                        //Email does not exist now add new user to database
//                        sqliteHelper.addCustomer(new Customer(null, FName, LName, PhoneNum, Email, Password));
//                        Snackbar.make(btnRegister, "User created successfully! Please Login ", Snackbar.LENGTH_LONG).show();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        },
//                                Snackbar.LENGTH_LONG);
//                    }else {
//                        //Email exists with email input provided so show error user already exist
//                        Snackbar.make(btnRegister, "User already exists with same email ", Snackbar.LENGTH_LONG).show();
//                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    //this method used to set Login TextView click event
    private void initLogin() {
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //connect XML views to its Objects
    private void initViews() {
        etFName = (EditText) findViewById(R.id.etFName);
        etLName = (EditText) findViewById(R.id.etLName);
        etPhoneNum = (EditText) findViewById(R.id.etNoHp);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }

    //validasi input user
    public boolean validate() {
        boolean valid = false;

        //ambil values et
        String FName = etFName.getText().toString();
        String PhoneNum = etPhoneNum.getText().toString();
        String Email = etEmail.getText().toString();
        String Password = etPassword.getText().toString();
        String RePassword = etRePassword.getText().toString();

        //Handling validation for first name field
        if (FName.isEmpty()) {
            valid = false;
            Toast.makeText(getApplicationContext(),"Insert First Name!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }

        //Handling validation for phone number field
        if (PhoneNum.isEmpty()) {
            valid = false;
            Toast.makeText(getApplicationContext(),"Insert Phone Number!", Toast.LENGTH_SHORT).show();
        } else {
            if (PhoneNum.length() > 9) {
                valid = true;
            } else {
                valid = false;
                Toast.makeText(getApplicationContext(),"Invalid Phone Number!", Toast.LENGTH_SHORT).show();
            }
        }

        //Handling validation for Email field
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            Toast.makeText(getApplicationContext(),"Invalid email!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }

        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            Toast.makeText(getApplicationContext(),"Invalid password!", Toast.LENGTH_SHORT).show();
        } else {
            if (Password.length() > 5) {
                valid = true;
            } else {
                valid = false;
                Toast.makeText(getApplicationContext(),"Invalid password!", Toast.LENGTH_SHORT).show();
            }
        }

        //Handling validation for Password field
        if (RePassword.isEmpty()) {
            valid = false;
            Toast.makeText(getApplicationContext(),"Invalid password!", Toast.LENGTH_SHORT).show();
        } else {
            if (RePassword.length() > 5) {
                valid = true;
            } else {
                valid = false;
                Toast.makeText(getApplicationContext(),"Invalid password!", Toast.LENGTH_SHORT).show();
            }
        }

        //Validate password and re-type password is correct
        if(!Password.equals(RePassword)) {
            valid = false;
            Toast.makeText(getApplicationContext(),"Re-type password must be same with password", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }

        return valid;
    }

    public void register(final String Email, final String Password, final String PhoneNum, final String FName, final String LName){
        mAuth.createUserWithEmailAndPassword(Email, Password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("register", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Map<String, Object> users = new HashMap<>();
                        users.put("email", Email);
                        users.put("password", Password);
                        users.put("pNumber", PhoneNum);
                        users.put("fName", FName);
                        users.put("lName", LName);

                        db.collection("users").document(user.getUid())
                            .set(users)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("register", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("register", "Error writing document", e);
                                }
                            });
                        Snackbar.make(btnRegister, "User created successfully! Please Login ", Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },Snackbar.LENGTH_LONG);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("register", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                }
            });
    }
}
