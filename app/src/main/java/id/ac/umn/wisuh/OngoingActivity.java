package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.model.Values;

public class OngoingActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView tvNamaCarwash;
    private TextView tvAlamatCarwash;
    private TextView tvHargaCarwash;
    private TextView tvStatus;
    private Button btnSelesai;

    private GeoPoint geoPoint;

    private String idCarwash;
    private String tipeKendaraan;
    private String tipeCarwash;
    private String idPesanan;
    public Double saldoUser;

    private GoogleMap mMap;

    //Firebase Firestore
    private FirebaseFirestore db;

    //Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing);

        //ambil database
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        user = mAuth.getCurrentUser();

        tvNamaCarwash = findViewById(R.id.tvNamaCarwash);
        tvAlamatCarwash = findViewById(R.id.tvAlamatCarwash);
        tvHargaCarwash = findViewById(R.id.tvHargaCarwash);
        tvStatus = findViewById(R.id.tvStatus);
        btnSelesai = findViewById(R.id.btnSelesai);

        //Buat dapetin dari intent CarwashDetailActivity
        Intent intent = getIntent();
        idCarwash = intent.getStringExtra("idCarwash");
        tipeKendaraan = intent.getStringExtra("tipeKendaraan");
        tipeCarwash = intent.getStringExtra("tipeCarwash");
        idPesanan = intent.getStringExtra("idPesanan");
        Log.d("idCarwash",idCarwash);
        Log.d("tipeKendaraan",tipeKendaraan);
        Log.d("tipeCarwash",tipeCarwash);
        Log.d("idPesanan",idPesanan);

        if(tipeCarwash.equals("Carwash")){
            if(tipeKendaraan.equals("mobil")){
                //ambil data-datanya dari database
                DocumentReference docRef = db.collection("Carwash").document(idCarwash);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //Log.d("testingSenen3",document.getString("nama"));
                                //tvNamaCarwash.setText(document.getString("nama"));
                                isiData(document.getString("nama"), document.getString("alamat"),
                                        document.getDouble("hargaMobil"),document.getGeoPoint("latLong"));

                            } else {
                                Log.d("signIn", "No such document");
                            }
                        } else {
                            Log.d("signIn", "get failed with ", task.getException());
                        }
                    }
                });
            }
            else{
                //ambil data-datanya dari database
                Log.d("tipeKendaraan",tipeKendaraan);
                DocumentReference docRef = db.collection("Carwash").document(idCarwash);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //Log.d("testingSenen3",document.getString("nama"));
                                //tvNamaCarwash.setText(document.getString("nama"));
                                isiData(document.getString("nama"), document.getString("alamat"),
                                        document.getDouble("hargaMotor"),document.getGeoPoint("latLong"));

                            } else {
                                Log.d("signIn", "No such document");
                            }
                        } else {
                            Log.d("signIn", "get failed with ", task.getException());
                        }
                    }
                });
            }
        }else {
            if(tipeKendaraan.equals("mobil")){
                //ambil data-datanya dari database
                DocumentReference docRef = db.collection("salon").document(idCarwash);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //Log.d("testingSenen3",document.getString("nama"));
                                //tvNamaCarwash.setText(document.getString("nama"));
                                isiData(document.getString("nama"), document.getString("alamat"),
                                        document.getDouble("hargaMobil"),document.getGeoPoint("latLong"));

                            } else {
                                Log.d("signIn", "No such document");
                            }
                        } else {
                            Log.d("signIn", "get failed with ", task.getException());
                        }
                    }
                });
            }
            else{
                //ambil data-datanya dari database
                Log.d("tipeKendaraan",tipeKendaraan);
                DocumentReference docRef = db.collection("salon").document(idCarwash);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //Log.d("testingSenen3",document.getString("nama"));
                                //tvNamaCarwash.setText(document.getString("nama"));
                                isiData(document.getString("nama"), document.getString("alamat"),
                                        document.getDouble("hargaMotor"),document.getGeoPoint("latLong"));

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

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference updateCarwash;


                updateCarwash = db.collection("pencucian").document(idPesanan);

                // update status dan waktuSelesai
                updateCarwash.update("status", "completed", "waktuSelesai", Timestamp.now()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateSukses", "Update dokumen sukses");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("updateFail", "Update dokumen sukses");
                    }
                });

                final DocumentReference updateSaldo;

                updateSaldo = db.collection("users").document(user.getUid());
//                joti coba edit biar pas saldo kurang ga bisa finish
                updateSaldo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                saldoUser = document.getDouble("saldo");
                                Double harga = Double.parseDouble(tvHargaCarwash.getText().toString());
                                Log.d("saldo user", String.valueOf(saldoUser));
                                Log.d("harga", String.valueOf(harga));
                                if (saldoUser > harga) {

//                Bagian ini ga di edit ya
                                    updateSaldo.update("saldo", FieldValue.increment(-Double.valueOf((String) tvHargaCarwash.getText()))).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("updateSukses", "Update dokumen sukses");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("updateFail", "Update dokumen sukses");
                                        }
                                    });

                                    Intent intent;
                                    intent = new Intent(OngoingActivity.this, CheckoutActivity.class);
                                    intent.putExtra("idPesanan", idPesanan);
                                    startActivity(intent);
                                    finish();
//               Sampai sini tidak di utak atik
                                } else {
                                    Toast.makeText(OngoingActivity.this, "saldo anda kurang", Toast.LENGTH_SHORT).show();
                                }
//                                Log.d("saldo User : ", String.valueOf(saldoUser));
                            }
//
                        } else {
                            Log.d("signIn", "get failed with ", task.getException());
                        }
                    }
                });

            }

                

//            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Naro marker dan gerakkin camera
        LatLng carwash = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
        mMap.addMarker(new MarkerOptions().position(carwash).title("ini Carwash"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(carwash));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);
    }

    public void isiData(String nama, String alamat, Double harga, GeoPoint latLong){
        //masukkin masing-masing data
        tvNamaCarwash.setText(nama);
        tvAlamatCarwash.setText(alamat);
        tvHargaCarwash.setText(harga.toString());
        tvStatus.setText("Ongoing");
        Log.d("testingSenen3",latLong.toString());
        geoPoint = latLong;
        Log.d("testingSenen3",geoPoint.toString());
        //siapin map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
