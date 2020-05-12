package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;
import java.util.Map;

public class CarsalonDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    // Googlemap
    private GoogleMap mMap;

    // id carwash dari activity carwash
    private String idCarwash;

    //Firebase Firestore
    private FirebaseFirestore db;

    //simpen TextView
    private TextView tvNamaCarwash;
    //private TextView tvDescCarwash;
    private TextView tvjamCarwash;
    private TextView tvjrkCarwash;
    private TextView tvHargaCarwash;
    private TextView tvAlamatCarwash;
    private TextView tvHargaBikewash;
    private Button rsvCarwash;

    private GeoPoint geoPoint;

    //Radio Button dan Radio Group
    private RadioGroup rgMobilMotor;
    private RadioButton btnMobil;
    private RadioButton btnMotor;

    //Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //Lokasi User buat nampilin yang terdekat
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProvideClient;
    private static final int REQUEST_CODE = 101;

    //From -> the first coordinate from where we need to calculate the distance
    double fromLongitude;
    double fromLatitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwash_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        user = mAuth.getCurrentUser();

        //        toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_blue_24dp);
        toolbar.setTitle("Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarsalonDetailActivity.this, CarsalonActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        end of toolbar code

        //ambil loc user
        fusedLocationProvideClient = LocationServices.getFusedLocationProviderClient(this);
//        fetchLastLocation();
//        Log.d("onGagal: ", String.valueOf(currentLocation));

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },REQUEST_CODE
            );
            return ;
        }

        Task<Location> task = fusedLocationProvideClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLocation = location;
                fromLongitude = currentLocation.getLongitude();
                fromLatitude = currentLocation.getLatitude();
                Log.d("onSuccess: ", String.valueOf(location));
                //Toast.makeText(getApplicationContext(),currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
            }
        });

        //ambil sesuaiid
        tvNamaCarwash = findViewById(R.id.tvNamaCarwash);
        //tvDescCarwash = findViewById(R.id.tvDescCarwash);
        tvAlamatCarwash = findViewById(R.id.tvAlamatCarwash);
        tvHargaCarwash = findViewById(R.id.tvHargaCarwash);
        tvjamCarwash = findViewById(R.id.tvjamCarwash);
        tvjrkCarwash = findViewById(R.id.tvjrkCarwash);
        tvHargaBikewash = findViewById(R.id.tvHargaBikewash);
        rsvCarwash = findViewById(R.id.rsvcarwash);
        rgMobilMotor = findViewById(R.id.rgMobilMotor);
        btnMobil = findViewById(R.id.btnmobil);
        btnMotor = findViewById(R.id.btnmotor);

        //ambil database
        db = FirebaseFirestore.getInstance();

        // ambil id carwash dari carwash activity
        Intent intent = getIntent();
        idCarwash = intent.getStringExtra("idCarwash");
        Log.d("testingSenen2",idCarwash);

        rsvCarwash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected_id = rgMobilMotor.getCheckedRadioButtonId();

                // liat mana yang di checked
                if(selected_id == btnMobil.getId()){
                    // dari sini saya ubah
                    final Double hargamobil = Double.parseDouble(tvHargaCarwash.getText().toString());
                    Log.d("harga yang dipilih ", String.valueOf(hargamobil));
                    //cek saldo cukup gk
                    final DocumentReference docuser = db.collection("users").document(user.getUid());
                    docuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    final Double saldo = document.getDouble("saldo");
                                    Log.d("saldo masuk ", String.valueOf(saldo));
                                    if (hargamobil < saldo) { // sampai sini saya ubah
                                        Log.d("harga mobil < saldo", String.valueOf(saldo));
                                        //Masukkin ke database
                                        Map<String, Object> dataPencucian = new HashMap<>();
                                        dataPencucian.put("idCarwash", idCarwash);
                                        dataPencucian.put("idUser", user.getUid());
                                        dataPencucian.put("status", "ongoing");
                                        dataPencucian.put("tipeCarwash", "Carwash");
                                        dataPencucian.put("tipeKendaraan", "Mobil");
                                        dataPencucian.put("waktuMulai", Timestamp.now());
                                        dataPencucian.put("waktuSelesai", Timestamp.now());

                                        db.collection("pencucian")
                                                .add(dataPencucian)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                        Intent intent;
                                                        intent = new Intent(CarsalonDetailActivity.this, OngoingActivity.class);
                                                        intent.putExtra("idCarwash", idCarwash);
                                                        intent.putExtra("tipeKendaraan", "mobil");
                                                        intent.putExtra("tipeCarwash", "Carwash");
                                                        intent.putExtra("idPesanan", documentReference.getId());
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("GagalMasukkin Document", "Error adding document", e);
                                                    }
                                                });
                                        Log.d("testingJumat", "kamu milih mobil");
                                    } else {
                                        Toast.makeText(CarsalonDetailActivity.this, "saldo anda kurang", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                } else {
                    // dari sini saya ubah
                    final Double hargamotor = Double.parseDouble(tvHargaBikewash.getText().toString());
                    Log.d("harga yang dipilih ", String.valueOf(hargamotor));
                    //cek saldo cukup gk
                    final DocumentReference docuser = db.collection("users").document(user.getUid());
                    docuser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    final Double saldo = document.getDouble("saldo");
                                    Log.d("saldo masuk ", String.valueOf(saldo));
                                    if (hargamotor < saldo) {
                                        //Masukkin ke database
                                        Map<String, Object> dataPencucian = new HashMap<>();
                                        dataPencucian.put("idCarwash", idCarwash);
                                        dataPencucian.put("idUser", user.getUid());
                                        dataPencucian.put("status", "ongoing");
                                        dataPencucian.put("tipeCarwash", "Carwash");
                                        dataPencucian.put("tipeKendaraan", "Motor");
                                        dataPencucian.put("waktuMulai", Timestamp.now());
                                        dataPencucian.put("waktuSelesai", Timestamp.now());

                                        db.collection("pencucian")
                                                .add(dataPencucian)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                        Intent intent;
                                                        intent = new Intent(CarsalonDetailActivity.this, OngoingActivity.class);
                                                        intent.putExtra("idCarwash", idCarwash);
                                                        intent.putExtra("tipeKendaraan", "motor");
                                                        intent.putExtra("tipeCarwash", "Carwash");
                                                        intent.putExtra("idPesanan", documentReference.getId());
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("GagalMasukkin Document", "Error adding document", e);
                                                    }
                                                });
                                        Log.d("testingJumat", "kamu milih motor");
                                    } else {
                                        Toast.makeText(CarsalonDetailActivity.this, "saldo anda kurang", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
            }});

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
                                document.getDouble("jamBuka"),document.getDouble("jamTutup"), document.getDouble("hargaMobil"),document.getDouble("hargaMotor"),document.getGeoPoint("latLong"));
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


    public void isiData(String nama, String alamat, Double jamBuka, Double jamTutup, Double hargaMobil, Double hargaMotor, GeoPoint latLong){
        //buat ubah geopoint latLong ke latlong trus ngitung
        double toLatitude = latLong.getLatitude();
        double toLongitude = latLong.getLongitude();
        //Getting both the coordinates
        LatLng from = new LatLng(fromLatitude,fromLongitude);
        LatLng to = new LatLng(toLatitude,toLongitude);
        //Calculating the distance in km
        Double distance = SphericalUtil.computeDistanceBetween(from, to);
        Double dist = distance/1000;

        //masukkin masing-masing data
        //tvDescCarwash.setText(desc);
        tvNamaCarwash.setText(nama);
        tvAlamatCarwash.setText(alamat);
        tvHargaCarwash.setText(hargaMobil.toString());
        tvHargaBikewash.setText(hargaMotor.toString());
        tvjamCarwash.setText(jamBuka.toString() + " AM - " + jamTutup.toString() + " PM");
        tvjrkCarwash.setText(String.format("%.1f",dist)+" KM");
        Log.d("testingSenen3",latLong.toString());
        geoPoint = latLong;
        Log.d("testingSenen3",geoPoint.toString());
        //siapin map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
