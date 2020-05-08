package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class OngoingActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView tvNamaCarwash;
    private TextView tvAlamatCarwash;
    private TextView tvHargaCarwash;
    private TextView tvStatus;

    private GeoPoint geoPoint;

    private String idCarwash;
    private String tipeKendaraan;
    private String tipeCarwash;

    private GoogleMap mMap;

    //Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing);

        //ambil database
        db = FirebaseFirestore.getInstance();

        tvNamaCarwash = findViewById(R.id.tvNamaCarwash);
        tvAlamatCarwash = findViewById(R.id.tvAlamatCarwash);
        tvHargaCarwash = findViewById(R.id.tvHargaCarwash);
        tvStatus = findViewById(R.id.tvStatus);

        //Buat dapetin dari intent CarwashDetailActivity
        Intent intent = getIntent();
        idCarwash = intent.getStringExtra("idCarwash");
        tipeKendaraan = intent.getStringExtra("tipeKendaraan");
        tipeCarwash = intent.getStringExtra("tipeCarwash");
        Log.d("idCarwash",idCarwash);
        Log.d("tipeKendaraan",tipeKendaraan);
        Log.d("tipeCarwash",tipeCarwash);

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
