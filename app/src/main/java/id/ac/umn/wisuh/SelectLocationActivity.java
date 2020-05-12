package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    Location currentlocation;
    Button btnconfirm;
    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SelectLocationActivity.this);
        fetchLastLocation();
        btnconfirm = (Button) findViewById(R.id.btnConfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLocationActivity.this,RegisterCarSalonActivity.class);
                startActivity(intent);
//                Intent a = new Intent(SelectLocationActivity.this,RegisterCarWashActivity.class);
//                startActivity(a);
            }
        });
    }
    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(SelectLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SelectLocationActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){
                    currentlocation = location;
                    Toast.makeText(SelectLocationActivity.this,currentlocation.getLatitude()+""+currentlocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(SelectLocationActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng carsalon = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(carsalon).title("I am here");
        mMap.animateCamera(CameraUpdateFactory.newLatLng(carsalon));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(carsalon,5));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }
}
