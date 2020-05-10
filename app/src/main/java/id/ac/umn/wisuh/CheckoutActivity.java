package id.ac.umn.wisuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;

public class CheckoutActivity extends AppCompatActivity {

    //Firebase Firestore
    private FirebaseFirestore db;

    private String idPesanan;
    private String idCarwash;
    private String tipeCarwash;
    private String tipeKendaraan;
    private String namaCarwash;
    private String alamatCarwash;
    private Double harga;
    private Double total;
    private Timestamp waktuSelesai;

    private TextView tvNamaCarwash;
    private TextView tvTipeCarwash;
    private TextView tvTipeKendaraan;
    private TextView tvAlamatCarwash;
    private TextView tvHargaCarwash;
    private TextView tvWaktuSelesai;

    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tvNamaCarwash = findViewById(R.id.tvNamaCarwash);
        tvTipeCarwash = findViewById(R.id.tvTipeCarwash);
        tvTipeKendaraan = findViewById(R.id.tvTipeKendaraan);
        tvAlamatCarwash = findViewById(R.id.tvAlamatCarwash);
        tvHargaCarwash = findViewById(R.id.tvHargaCarwash);
        tvWaktuSelesai = findViewById(R.id.tvWaktuSelesai);

        btnBackHome = findViewById(R.id.btnBackHome);

        //ambil database
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        idPesanan = intent.getStringExtra("idPesanan");
        Log.d("idPesanan",idPesanan);

        DocumentReference docRef = db.collection("pencucian").document(idPesanan);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tipeCarwash = document.getString("tipeCarwash");
                        waktuSelesai = document.getTimestamp("waktuSelesai");
                        tipeKendaraan = document.getString("tipeKendaraan");
                        idCarwash = document.getString("idCarwash");
                        DocumentReference docRef2 = db.collection(tipeCarwash).document(idCarwash);
                        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentCarwash = task.getResult();
                                if(documentCarwash.exists()){
                                    namaCarwash = documentCarwash.getString("nama");
                                    alamatCarwash = documentCarwash.getString("alamat");
                                    if(tipeKendaraan.equals("Mobil")){
                                        harga = documentCarwash.getDouble("hargaMobil");
                                    }
                                    else{
                                        harga = documentCarwash.getDouble("hargaMotor");
                                    }
                                }
                                isiData(namaCarwash,tipeCarwash,tipeKendaraan,alamatCarwash,harga,waktuSelesai);
                            }
                        });
                    } else {
                        Log.d("CheckoutActivity", "No such document");
                    }
                } else {
                    Log.d("CheckoutActivity", "get failed with ", task.getException());
                }
            }
        });

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

    public void isiData(String namaCarwash,String tipeCarwash, String tipeKendaraan, String alamatCarwash, Double harga, Timestamp waktuSelesai){
        tvNamaCarwash.setText(namaCarwash);
        tvTipeCarwash.setText(tipeCarwash);
        tvTipeKendaraan.setText(tipeKendaraan);
        tvAlamatCarwash.setText(alamatCarwash);
        tvHargaCarwash.setText(harga.toString());
        tvWaktuSelesai.setText(String.valueOf(waktuSelesai.toDate()));
    }
}
