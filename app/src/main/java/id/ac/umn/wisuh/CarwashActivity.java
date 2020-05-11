package id.ac.umn.wisuh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

//joti coba nambahin implements filterable
//public class CarwashActivity extends AppCompatActivity implements Filterable {
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
    ArrayList<Double> listDistance;


    StorageReference storageReference;

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
        listDistance = new ArrayList<>();

        fusedLocationProvideClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        Log.d("onGagal: ", String.valueOf(currentLocation));

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
                                GeoPoint templatLong = document.getGeoPoint("latLong");
                                //buat ubah latLong trus ngitung
                                double toLatitude = templatLong.getLatitude();
                                double toLongitude = templatLong.getLongitude();
                                //Getting both the coordinates
                                LatLng from = new LatLng(fromLatitude,fromLongitude);
                                LatLng to = new LatLng(toLatitude,toLongitude);
                                //Calculating the distance in meters
                                Double tempdistance = SphericalUtil.computeDistanceBetween(from, to);

                                //Log.d("testingCarwash",tempString);
                                listRatingCarwash.add(tempRating);
                                listCarwash.add(tempString);
                                listIdCarwash.add(document.getId());
                                listDistance.add(tempdistance/1000);
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
            imgview.setImageResource(R.drawable.carwash);

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
            tview.setTextSize(20);
//            tview.setTypeface(Typeface.DEFAULT_BOLD);
//            tview.setTypeface(Typeface.SANS_SERIF);
            tview.setTextColor(Color.BLUE);
//            tview.setTypeface(fontstyle);
            tview.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
//            tview.setTypeface(Typeface.createFromAsset(getAssets(), "bold.tff"));
            txtviewparam.setMargins(300, 20, 0, 0);
            tview.setLayoutParams(txtviewparam);

//          rating
            ImageView imgrating = new ImageView(this);
//            imgrating.setLayoutParams(imageButtonParam);
            LayoutParams imgrtng = new LayoutParams(50, 50);
            imgrtng.setMargins(305, 120, 0, 0);
            imgrating.setImageResource(R.drawable.rating);
            imgrating.setLayoutParams(imgrtng);

            TextView ratingtext = new TextView(this);
            LayoutParams ratingtxt = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            ratingtext.setText(String.valueOf(listRatingCarwash.get(i-1)));
            ratingtext.setTextColor(Color.BLACK);
            ratingtext.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            ratingtxt.setMargins(365, 120, 0, 5);
            ratingtext.setLayoutParams(ratingtxt);

//            waktu
            ImageView imgwaktu = new ImageView(this);
//            imgrating.setLayoutParams(imageButtonParam);
            LayoutParams imgwkt = new LayoutParams(50, 50);
            imgwkt.setMargins(470, 120, 0, 0);
            imgwaktu.setImageResource(R.drawable.time);
            imgwaktu.setLayoutParams(imgwkt);

            TextView textjam = new TextView(this);
            LayoutParams txtj = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            textjam.setText("20 Menit");
            textjam.setTextColor(Color.BLACK);
            textjam.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            txtj.setMargins(520, 120, 0, 0);
            textjam.setLayoutParams(txtj);

//            jarak
            ImageView imgjarak = new ImageView(this);
//            imgrating.setLayoutParams(imageButtonParam);
            LayoutParams imgjrk = new LayoutParams(50, 50);
            imgjrk.setMargins(720, 120, 0, 0);
            imgjarak.setImageResource(R.drawable.distance);
            imgjarak.setLayoutParams(imgjrk);

            TextView textjarak = new TextView(this);
            LayoutParams txtjrk = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            Log.d("onSuccess: ", String.valueOf(listDistance.get(i-1)));
            Double dist = listDistance.get(i-1);
            textjarak.setText(String.format("%.1f",dist)+" KM");
            textjarak.setTextColor(Color.BLACK);
            textjarak.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            txtjrk.setMargins(770, 120, 0, 0);
//            txtjrk.setMargins(580, 120, 0, 0);
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
            rlayout.addView(imgjarak);
            rlayout.addView(textjarak);
        }
    }

    private void fetchLastLocation(){
        //minta lokasi sekarang di mana ini buat ngurutin berdasarkan jarak
//        kodingan roni
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
                Log.d("onSuccess: ", String.valueOf(location));
            //Toast.makeText(getApplicationContext(),currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
            }
        });
    }

//    joti coba nambahin search

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu,menu);
//        MenuItem item = menu.findItem(R.id.searchMenu);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                getFilter();
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public Filter getFilter() {
//        return filter;
//    }
//    Filter filter = new Filter() {
////        run on backgorund thread
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<String> filteredList = new ArrayList<>();
//            if (constraint.toString().isEmpty()){
//                filteredList.addAll(listCarwash);
//            }
//            else{
//                for (String movie: listCarwash){
//                    if(movie.toLowerCase().contains(constraint.toString().toLowerCase())){
//                        filteredList.add(movie);
//                    }
//                }
//            }
//            FilterResults filterResults = new FilterResults();
//            filterResults.values=filteredList;
//
//            return filterResults;
//        }
////      run on a ui  thread
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            listCarwash.clear();
//            listCarwash.addAll((Collection<? extends String>) results.values);
//
//        }
//    };
}


