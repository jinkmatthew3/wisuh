package id.ac.umn.wisuh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private ImageButton btnnearby;
    private ImageButton btnsalon;

    //buat dapetin usernya
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //Firebase Firestore
    private FirebaseFirestore db;

    private TextView tvSaldo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        tvSaldo = view.findViewById(R.id.tvSaldo);
        btnnearby = view.findViewById(R.id.btn_carwash);
        btnnearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindahnearby = new Intent(getActivity().getApplication(), CarwashActivity.class);
                pindahnearby.putExtra("tipeCarwash","Carwash");
                startActivity(pindahnearby);
            }
        });

        btnsalon = view.findViewById(R.id.btn_salon);
        btnsalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindahsalon = new Intent(getActivity().getApplication(), CarwashActivity.class);
                pindahsalon.putExtra("tipeCarwash","Salon");
                startActivity(pindahsalon);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        final DocumentReference docRef = db.collection("users").document(user.getUid());

        if(user != null){
//          docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Double saldo = document.getDouble("saldo");
                            tvSaldo.setText("IDR. " + saldo);
                        }
                        else {
                            Log.d("signIn", "No such document");
                        }
                    } else {
                        Log.d("signIn", "get failed with ", task.getException());
                    }
                }
            });
        }
        return view;
    }
}
