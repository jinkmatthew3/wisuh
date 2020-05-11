package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import android.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class CarwashDetailActivity extends FragmentActivity implements OnMapReadyCallback {

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
                Intent intent = new Intent(CarwashDetailActivity.this, CarwashActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        end of toolbar code

        //ambil sesuaiid
        tvNamaCarwash = findViewById(R.id.tvNamaCarwash);
        //tvDescCarwash = findViewById(R.id.tvDescCarwash);
        tvAlamatCarwash = findViewById(R.id.tvAlamatCarwash);
        tvHargaCarwash = findViewById(R.id.tvHargaCarwash);
        tvjamCarwash = findViewById(R.id.tvjamCarwash);
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

        //ambi data shared preference notif
        SharedPreferences sharedPrefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String cookieName = sharedPrefs.getString("cookieName", "missing");
        final boolean notif = sharedPrefs.getBoolean("notif", true);


        rsvCarwash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected_id = rgMobilMotor.getCheckedRadioButtonId();
                // liat mana yang di checked
                if (selected_id == btnMobil.getId()) {
                    if(notif){
                        // notif
                        displayNotification();
                    }
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
                                                        intent = new Intent(CarwashDetailActivity.this, OngoingActivity.class);
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
                                        Toast.makeText(CarwashDetailActivity.this, "saldo anda kurang", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                } else {
                    if(notif){
                        // notif
                        displayNotification();
                    }
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
                                                        intent = new Intent(CarwashDetailActivity.this, OngoingActivity.class);
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
                                        Toast.makeText(CarwashDetailActivity.this, "saldo anda kurang", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
            }});


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
                                document.getDouble("jamBuka"),document.getDouble("jamTutup"), document.getDouble("hargaMobil"),document.getDouble("hargaMotor"),document.getGeoPoint("latLong"));

                    } else {
                        Log.d("signIn", "No such document");
                    }
                } else {
                    Log.d("signIn", "get failed with ", task.getException());
                }
            }
        });

    }

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
        //masukkin masing-masing data
        //tvDescCarwash.setText(desc);
        tvNamaCarwash.setText(nama);
        tvAlamatCarwash.setText(alamat);
        tvHargaCarwash.setText(hargaMobil.toString());
        tvHargaBikewash.setText(hargaMotor.toString());
        tvjamCarwash.setText(jamBuka.toString() + " - " + jamTutup.toString());
        Log.d("testingSenen3",latLong.toString());
        geoPoint = latLong;
        Log.d("testingSenen3",geoPoint.toString());
        //siapin map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//  ini munculin notif
    public void displayNotification(){

        String CHANNEL_ID = "0";
        String channel_name = "Wisuh";
        String channel_description = "Your Order have been placed!";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(channel_name)
                .setContentText(channel_description)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager)getSystemService(
                Context.NOTIFICATION_SERVICE
        );

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.app_name);
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel("0", name, importance);
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(mChannel);
//        }
//        notificationManager.notify(0, builder.build());

        //kalo OS diatas atau sama dengan oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
            channel.setDescription(channel_description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationMgr = getSystemService(NotificationManager.class);
            notificationMgr.createNotificationChannel(channel);
        }

        //show notification
        notificationManager.notify(0, builder.build());
    }
}

