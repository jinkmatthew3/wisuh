package id.ac.umn.wisuh;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


///**
// * A simple {@link Fragment} subclass.
// * Use the {@link TopUpActivity#newInstance} factory method to
// * create an instance of this fragment.
// */
public class TopUpActivity extends Fragment {
    EditText etEmailUser, etNominal;
    Button btnConfirm;
    private FirebaseFirestore db;
//    public TopUpActivity(){
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_up_activity, container, false);

        etEmailUser = (EditText) v.findViewById(R.id.etEmailUser);
        etNominal = (EditText) v.findViewById(R.id.etNominal);
        btnConfirm = (Button) v.findViewById(R.id.btnConfirm);
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
                                            Toast.makeText(getActivity(),"TopUp Success",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(getActivity(),"Email Customer Salah",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
            }
        });

        return v;
    }
}
