package id.ac.umn.wisuh;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;


///**
// * A simple {@link Fragment} subclass.
// * Use the {@link RegisterCarWashActivity#newInstance} factory method to
// * create an instance of this fragment.
// */
public class RegisterCarWashActivity extends Fragment {
    EditText etnama, etalamat, ethargaMobil, ethargaMotor, etjamBuka, etjamTutup, etdesc;
    Button btnmaps1, btnRegister1;

    //Declaration SqliteHelper
    SqliteHelper sqliteHelper;

    //Firebase Authentication
    private FirebaseAuth mAuth;

    //Firebase Firestore
    private FirebaseFirestore db;

    //ProgressBar
    ProgressBar progressBar;

    public RegisterCarWashActivity() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_car_wash_activity, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sqliteHelper = new SqliteHelper(getActivity());
        etnama = (EditText) v.findViewById(R.id.etnama);
        etalamat = (EditText) v.findViewById(R.id.etalamat);
        ethargaMobil = (EditText) v.findViewById(R.id.ethargaMobil);
        ethargaMotor = (EditText) v.findViewById(R.id.ethargaMotor);
        etjamBuka = (EditText) v.findViewById(R.id.etjamBuka);
        etjamTutup = (EditText) v.findViewById(R.id.etjamTutup);
        etdesc = (EditText) v.findViewById(R.id.etdescription);
        btnmaps1 = (Button) v.findViewById(R.id.btnmaps1);
        btnRegister1 = (Button) v.findViewById(R.id.btnRegister1);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        btnRegister1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String nama = etnama.getText().toString();
                    String alamat = etalamat.getText().toString();
                    int hargaMobil = Integer.parseInt(ethargaMobil.getText().toString());
                    int hargaMotor = Integer.parseInt(ethargaMotor.getText().toString());
                    String jamBuka = etjamBuka.getText().toString();
                    String jamTutup = etjamTutup.getText().toString();
                    String desc = etdesc.getText().toString();


                    //loading progress Bar
                    progressBar.setVisibility(View.VISIBLE);
                    register(nama,alamat,hargaMobil,hargaMotor,jamBuka,jamTutup,desc);
                }
            }
        });
//        btnmaps1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),SelectLocationActivity.class);
//                startActivity(intent);
//            }
//        });
        return v;
    }
//    btnmaps.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//            Intent intent;
//            try {
//                intent = builder.build(getActivity());
//                startActivityForResult(intent,1);
//            } catch (GooglePlayServicesRepairableException e) {
//                e.printStackTrace();
//            } catch (GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }
//        }
//    });
//        return v;
//
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        if (requestCode == 1){
//            if (resultCode == RESULT_OK){
//                Place place  = PlacePicker.getPlace(data,getActivity());
//                Toast.makeText(getActivity(), place.getAddress(),Toast.LENGTH_SHORT).show();
//            }
//        }
//    }



    //validasi input user
    public boolean validate() {
        boolean valid = false;
        //ambil values et
        String nama = etnama.getText().toString();
        String alamat = etalamat.getText().toString();
        String hargaMobil = ethargaMobil.getText().toString();
        String hargaMotor = ethargaMotor.getText().toString();
        String jamBuka = etjamBuka.getText().toString();
        String jamTutup = etjamTutup.getText().toString();
        String desc = etdesc.getText().toString();
        //Handling validation for first name field
        if (nama.isEmpty()) {
            valid = false;
            Toast.makeText(getActivity(), "Insert Name CarWash!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }
        if (alamat.isEmpty()) {
            valid = false;
            Toast.makeText(getActivity(), "Insert Address!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }

        if (hargaMobil.isEmpty()) {
            valid = false;
            Toast.makeText(getActivity(), "Insert Price Car!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }

        if (hargaMotor.isEmpty()) {
            valid = false;
            Toast.makeText(getActivity(), "Insert Price Bike!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }
        if (jamBuka.isEmpty()) {
            valid = false;
            Toast.makeText(getActivity(), "Insert Jam Buka!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }
        if (jamTutup.isEmpty()) {
            valid = false;
            Toast.makeText(getActivity(), "Insert Jam Buka!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }

        if (desc.isEmpty()) {
            valid = false;
            Toast.makeText(getActivity(), "Insert Description!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }
        return valid;
    }
    public void register(String nama, String alamat, int hargaMobil, int hargaMotor, String jamBuka, String jamTutup, String desc) {

        // Sign in success, update UI with the signed-in user's information
//                    Log.d("register", "createUserWithEmail:success");
        GeoPoint geoPoint = new GeoPoint(-6.2563665,106.6115672);
        Carwash carwash = new Carwash(alamat,desc,hargaMobil,hargaMotor,jamBuka,jamTutup,geoPoint,nama,0);

        db.collection("Carwash").add(carwash).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(),"Berhasil register!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
