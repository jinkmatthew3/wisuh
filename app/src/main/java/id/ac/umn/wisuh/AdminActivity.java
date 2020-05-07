package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    EditText etEmailUser, etNominal;
    Button btnConfirm;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        etEmailUser = (EditText) findViewById(R.id.etEmailUser);
        etNominal = (EditText) findViewById(R.id.etNominal);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        //ambil database
        db = FirebaseFirestore.getInstance();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        ambil data dari koleksi User firestore
                db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list){
                                Customer c = new Customer();
                                c = d.toObject(Customer.class);
                                c.setId(d.getId());
                                if (etEmailUser.getText().toString().equals(c.getEmail())){
                                    c.setSaldo(c.getSaldo()+ Integer.parseInt(etNominal.getText().toString()));
                                    System.out.println(c.getSaldo());
                                    db.collection("users").document(c.getId()).set(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AdminActivity.this,"TopUp Success",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                });


            }
            public void updateSaldo(String email, String saldo){
                final EditText etNominal = (EditText) findViewById(R.id.etNominal);
            }
        });
    }
}


