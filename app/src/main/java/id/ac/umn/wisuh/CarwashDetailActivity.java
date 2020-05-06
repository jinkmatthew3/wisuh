package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
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

public class CarwashDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    // Googlemap
    private GoogleMap mMap;

    // id carwash dari activity carwash
    private String idCarwash;

    //Firebase Firestore
    private FirebaseFirestore db;

    //simpen TextView
    private TextView tvNamaCarwash;
    private TextView tvDescCarwash;
    private TextView tvjamCarwash;
    private TextView tvHargaCarwash;
    private TextView tvAlamatCarwash;
    private TextView tvHargaBikewash;

    private GeoPoint geoPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwash_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        //ambil sesuaiid
        tvNamaCarwash = findViewById(R.id.tvNamaCarwash);
        tvDescCarwash = findViewById(R.id.tvDescCarwash);
        tvAlamatCarwash = findViewById(R.id.tvAlamatCarwash);
        tvHargaCarwash = findViewById(R.id.tvHargaCarwash);
        tvjamCarwash = findViewById(R.id.tvjamCarwash);
        tvHargaBikewash = findViewById(R.id.tvHargaBikewash);

        //ambil database
        db = FirebaseFirestore.getInstance();

        // ambil id carwash dari carwash activity
        Intent intent = getIntent();
        idCarwash = intent.getStringExtra("idCarwash");
        Log.d("testingSenen2",idCarwash);

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
                        isiData(document.getString("nama"), document.getString("alamat"),document.getString("desc"),
                                document.getDouble("rating"), document.getDouble("hargaMobil"),document.getDouble("hargaMotor"),document.getGeoPoint("latLong"));
                        /*String email = document.getString("email");
                        String pNumber = document.getString("pNumber");
                        String fName = document.getString("fName");
                        String lName = document.getString("lName");*/
                        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email",email);
                        intent.putExtra("nomorHp",pNumber);
                        intent.putExtra("fname",fName);
                        intent.putExtra("lname",lName);
                        startActivity(intent);
                        finish();*/

                    } else {
                        Log.d("signIn", "No such document");
                    }
                } else {
                    Log.d("signIn", "get failed with ", task.getException());
                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng carwash = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
        mMap.addMarker(new MarkerOptions().position(carwash).title("ini Carwash"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(carwash));
        //mMap.getMaxZoomLevel();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);
    }


    public void isiData(String nama, String alamat, String desc, Double rating, Double hargaMobil, Double hargaMotor, GeoPoint latLong){
        //masukkin masing-masing data
        tvDescCarwash.setText(desc);
        tvNamaCarwash.setText(nama);
        tvAlamatCarwash.setText(alamat);
        tvHargaCarwash.setText(hargaMobil.toString());
        tvHargaBikewash.setText(hargaMotor.toString());
        tvjamCarwash.setText(rating.toString());
        Log.d("testingSenen3",latLong.toString());
        geoPoint = latLong;
        Log.d("testingSenen3",geoPoint.toString());
        //siapin map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
