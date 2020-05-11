package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    //Lokasi User buat nampilin yang terdekat
//    Location currentLocation;
    FusedLocationProviderClient fusedLocationProvideClient;
    private static final int REQUEST_CODE = 101;

    //From -> the first coordinate from where we need to calculate the distance
    LatLng currentLocation;
    ArrayList<LatLng> listPoints;

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
                currentLocation = new LatLng (location.getLatitude(),location.getLongitude());
                Log.d("onSuccess: ", String.valueOf(location));
                //Toast.makeText(getApplicationContext(),currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
            }
        });

        listPoints = new ArrayList<>();
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
        mMap.setMyLocationEnabled(true);

        // Naro marker dan gerakkin camera
        LatLng carwash = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(carwash).title("ini Carwash"));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(carwash).title("ini Carwash"));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(currentLocation).title("user"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(carwash));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);

        //reset kalo marker udah 2
        if(listPoints.size() == 2){
            listPoints.clear();
            mMap.clear();
        }
        //save 1 point
        listPoints.add(currentLocation);
        Log.d("Location user: ", String.valueOf(currentLocation));
        Log.d("Location carwash: ", String.valueOf(carwash));
        listPoints.add(carwash);
        Log.d("Size list p: ", String.valueOf(listPoints.size()));


        if(listPoints.size() == 2){
            //create URL
            String url = getRequestUrl(listPoints.get(0),listPoints.get(1));
            TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
            taskRequestDirections.execute(url);
        }

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                //reset kalo marker udah 2
//                if(listPoints.size() == 2){
//                    listPoints.clear();
//                    mMap.clear();
//                }
//                //save 1 point
//                listPoints.add(latLng);
//                //create marker
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//
//                if(listPoints.size() == 1){
//                    // add 1 mark
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                } else {
//                    // add 2 mark
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                }
//                mMap.addMarker(markerOptions);
//
//                if(listPoints.size() == 2){
//                    //create URL
//                    String url = getRequestUrl(listPoints.get(0),listPoints.get(1));
//                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
//                    taskRequestDirections.execute(url);
//                }
//            }
//        });
    }

    private String getRequestUrl(LatLng origin, LatLng destination) {
        //from value
        String str_origin = "origin=" + origin.latitude + ","+ destination.longitude;
        //to value
        String str_dest = "destination=" + origin.latitude + ","+ destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        //Build full param
        String param = str_origin + "&"+ str_dest + "&" + sensor+ "&"+ mode;
        //output
        String output = "json";
        //create URL
        String YOUR_API_KEY = "AIzaSyBBdtbStKs0Zlp8waxC5-04qhONaYoc94s";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param  + "&key=" + YOUR_API_KEY;
        Log.d( "getRequestUrl: ", url);
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ( (line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            //parse JSON
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
//                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
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
