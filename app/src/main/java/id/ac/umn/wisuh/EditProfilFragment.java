package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;

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
public class EditProfilFragment extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //Firebase Firestore
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_fragment);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        user = mAuth.getCurrentUser();

        user = mAuth.getCurrentUser();
        Log.d("user aktif", String.valueOf(user));
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
}
