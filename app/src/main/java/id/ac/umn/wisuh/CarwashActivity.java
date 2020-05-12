package id.ac.umn.wisuh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

public class CarwashActivity extends AppCompatActivity {
    LinearLayout llayout;
    Toolbar toolbar;
    private int imgv = 34;

    //Firebase Firestore
    private FirebaseFirestore db;
    ArrayList<String> listCarwash;
    ArrayList<String> listIdCarwash;
    ArrayList<Image> listFotoCarwash;
    ArrayList<Double> listRatingCarwash;

    StorageReference storageReference;

    //Lokasi User buat nampilin yang terdekat
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProvideClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwash);

//        toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_blue_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Car Wash");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        end of toolbar code

        //bikin arraylist
        listCarwash = new ArrayList<>();
        listFotoCarwash = new ArrayList<>();
        listIdCarwash = new ArrayList<>();
        listRatingCarwash = new ArrayList<>();

        fusedLocationProvideClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        //ambil database
        db = FirebaseFirestore.getInstance();

        //ambil data dari koleksi Carwash firestore
        db.collection("Carwash").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("testingCarwash", document.getId() + " => " + document.getData());
                                String tempString = document.getString("nama");
                                Double tempRating = document.getDouble("rating");
                                //Log.d("testingCarwash",tempString);
                                listRatingCarwash.add(tempRating);
                                listCarwash.add(tempString);
                                listIdCarwash.add(document.getId());
                                /*storageReference = FirebaseStorage.getInstance().getReference();
                                StorageReference profilRef = storageReference.child("carwash/"+document.getId()+"/profil.jpg");
                                profilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Image tempImage = null;
                                        Picasso.get().load(uri).into((Target) tempImage);
                                        listFotoCarwash.add(tempImage);
                                    }
                                });*/
                            }
                            //bikin imageButton buat ditampilin
                            makeButton();
                        } else {
                            Log.d("testingCarwash", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void makeButton(){
        llayout = findViewById(R.id.scrollview);
        Log.d("testingCarwash3",listCarwash.size()+" ");
        //Adding secara dynamic
        for (int i = 1; i <= listCarwash.size(); i++) {
            RelativeLayout rlayout = new RelativeLayout(this);
            LayoutParams imageButtonParam = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            rlayout.setBackgroundResource(R.drawable.rectangle_icon);
            imageButtonParam.setMargins(0, 10, 0 , 10);
            rlayout.setLayoutParams(imageButtonParam);


            ImageView imgview = new ImageView(this);
            imgview.setLayoutParams(imageButtonParam);
            imgview.getLayoutParams().height = 310;
            imgview.getLayoutParams().width = 1080;
            imgview.setScaleType(ImageButton.ScaleType.FIT_START);
            imgview.setImageResource(R.drawable.car_washing_icon);

            final String idCarwash = listIdCarwash.get(i-1);
            Log.d("testingSenen",idCarwash);
            rlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CarwashActivity.this, CarwashDetailActivity.class);
                    intent.putExtra("idCarwash",idCarwash);
                    Log.d("testingSenen2",idCarwash);
                    startActivity(intent);
                    finish();
                }
            });

            TextView tview = new TextView(this);
            LayoutParams txtviewparam = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tview.setText(listCarwash.get(i-1));
            tview.setTextSize(15);
            tview.setTypeface(Typeface.DEFAULT_BOLD);
            tview.setTypeface(Typeface.SANS_SERIF);
            tview.setTextColor(Color.BLACK);
            tview.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            txtviewparam.setMargins(300, 20, 0, 0);
            tview.setLayoutParams(txtviewparam);

//          rating
            ImageView imgrating = new ImageView(this);
//            imgrating.setLayoutParams(imageButtonParam);
            LayoutParams imgrtng = new LayoutParams(50, 50);
            imgrtng.setMargins(300, 120, 0, 0);
            imgrating.setImageResource(R.drawable.car_washing_icon);
            imgrating.setLayoutParams(imgrtng);

            TextView ratingtext = new TextView(this);
            LayoutParams ratingtxt = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            ratingtext.setText("Rating : " + listRatingCarwash.get(i-1));
            ratingtext.setTextColor(Color.BLACK);
            ratingtext.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            ratingtxt.setMargins(360, 120, 0, 5);
            ratingtext.setLayoutParams(ratingtxt);

//            waktu
            ImageView imgwaktu = new ImageView(this);
//            imgrating.setLayoutParams(imageButtonParam);
            LayoutParams imgwkt = new LayoutParams(50, 50);
            imgwkt.setMargins(520, 120, 0, 0);
            imgwaktu.setImageResource(R.drawable.car_repair_icon);
            imgwaktu.setLayoutParams(imgwkt);

            TextView textjam = new TextView(this);
            LayoutParams txtj = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            textjam.setText("Waktu");
            textjam.setTextColor(Color.BLACK);
            textjam.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            txtj.setMargins(580, 120, 0, 0);
            textjam.setLayoutParams(txtj);

//            jarak
            TextView textjarak = new TextView(this);
            LayoutParams txtjrk = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            textjarak.setText("1.1 KM");
            textjarak.setTextColor(Color.BLACK);
            textjarak.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            txtjrk.setMargins(740, 120, 0, 0);
            textjarak.setLayoutParams(txtjrk);



            llayout.addView(rlayout);
            rlayout.addView(imgview);
            rlayout.addView(tview);
//          rating
            rlayout.addView(imgrating);
            rlayout.addView(ratingtext);
//          jam
            rlayout.addView(imgwaktu);
            rlayout.addView(textjam);
//          jarak
            rlayout.addView(textjarak);
        }
    }

    private void fetchLastLocation(){
        //minta lokasi sekarang di mana ini buat ngurutin berdasarkan jarak
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProvideClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLocation = location;
                //Toast.makeText(getApplicationContext(),currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
            }
        });
    }


}


