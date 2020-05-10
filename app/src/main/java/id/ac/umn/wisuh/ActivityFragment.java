package id.ac.umn.wisuh;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActivityFragment extends androidx.fragment.app.Fragment {

    LinearLayout llayout;

    //Firebase Firestore
    private FirebaseFirestore db;

    ArrayList<String> listIdPesanan;
    ArrayList<String> listTipeCarwash;
    ArrayList<String> listTipeKendaraan;
    ArrayList<String> listNamaCarwash;
    ArrayList<String> listIdCarwash;
    ArrayList<String> listStatusPesanan;
    ArrayList<Timestamp> listWaktuSelesai;

    //Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_activity,container,false);

        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bikin arraylist
        listIdPesanan = new ArrayList<>();
        listTipeCarwash = new ArrayList<>();
        listTipeKendaraan = new ArrayList<>();
        listNamaCarwash = new ArrayList<>();
        listIdCarwash = new ArrayList<>();
        listStatusPesanan = new ArrayList<>();
        listWaktuSelesai = new ArrayList<>();

        //ambil database
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        user = mAuth.getCurrentUser();

        db.collection("pencucian").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    if(document.getString("idUser").equals(user.getUid())){
                        String tempIdCarwash = document.getString("idCarwash");
                        String tempStatusPesanan = document.getString("status");
                        String tempTipeCarwash = document.getString("tipeCarwash");
                        String tempTipeKendaraan = document.getString("tipeKendaraan");
                        Timestamp tempWaktuSelesai = document.getTimestamp("waktuSelesai");

                        listIdPesanan.add(document.getId());
                        listIdCarwash.add(tempIdCarwash);
                        listStatusPesanan.add(tempStatusPesanan);
                        listTipeCarwash.add(tempTipeCarwash);
                        listTipeKendaraan.add(tempTipeKendaraan);
                        listWaktuSelesai.add(tempWaktuSelesai);


                        DocumentReference docRef2 = db.collection(tempTipeCarwash).document(tempIdCarwash);
                        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentCarwash = task.getResult();
                                if(documentCarwash.exists()){
                                    String tempNamaCarwash = documentCarwash.getString("nama");
                                }
                            }
                        });
                    }
                }
                llayout = getView().findViewById(R.id.scrollview);
                for(int i = 1; i <= listIdPesanan.size(); i++){
                    // Bikin Relative Layout
                    final RelativeLayout rlayout = new RelativeLayout(getContext());
                    RelativeLayout.LayoutParams imageButtonParam = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rlayout.setBackgroundResource(R.drawable.rectangle_icon);
                    imageButtonParam.setMargins(0, 10, 0 , 10);
                    rlayout.setLayoutParams(imageButtonParam);

                    // Image View
                    final ImageView imgview = new ImageView(getContext());
                    imgview.setLayoutParams(imageButtonParam);
                    imgview.getLayoutParams().height = 310;
                    imgview.getLayoutParams().width = 1080;
                    imgview.setScaleType(ImageButton.ScaleType.FIT_START);
                    imgview.setImageResource(R.drawable.car_washing_icon);



                    if(listStatusPesanan.get(i-1).equals("ongoing")){
                        final String tempIdCarwash = listIdCarwash.get(i-1);
                        final String tempTipeKendaraan = listTipeKendaraan.get(i-1);
                        final String tempTipeCarwash = listTipeCarwash.get(i-1);
                        final String tempIdPesanan = listIdPesanan.get(i-1);
                        rlayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), OngoingActivity.class);
                                intent.putExtra("idCarwash",tempIdCarwash);
                                intent.putExtra("tipeKendaraan",tempTipeKendaraan);
                                intent.putExtra("tipeCarwash",tempTipeCarwash);
                                intent.putExtra("idPesanan",tempIdPesanan);
                                startActivity(intent);
                            }
                        });
                    }
                    else{
                        final String tempIdPesanan = listIdPesanan.get(i-1);
                        rlayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), CheckoutActivity.class);
                                intent.putExtra("idPesanan",tempIdPesanan);
                                startActivity(intent);
                            }
                        });
                    }

                    final TextView tvStatusPesanan = new TextView(getContext());
                    RelativeLayout.LayoutParams txtviewparam2 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    tvStatusPesanan.setText(listStatusPesanan.get(i-1));
                    tvStatusPesanan.setTextSize(15);
                    tvStatusPesanan.setTypeface(Typeface.DEFAULT_BOLD);
                    tvStatusPesanan.setTypeface(Typeface.SANS_SERIF);
                    tvStatusPesanan.setTextColor(Color.BLACK);
                    tvStatusPesanan.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    txtviewparam2.setMargins(300, 80, 0, 0);
                    tvStatusPesanan.setLayoutParams(txtviewparam2);

                    final TextView tvWaktuSelesai = new TextView(getContext());
                    RelativeLayout.LayoutParams txtviewparam3 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    tvWaktuSelesai.setText(listWaktuSelesai.get(i-1).toDate().toString());
                    tvWaktuSelesai.setTextSize(15);
                    tvWaktuSelesai.setTypeface(Typeface.DEFAULT_BOLD);
                    tvWaktuSelesai.setTypeface(Typeface.SANS_SERIF);
                    tvWaktuSelesai.setTextColor(Color.BLACK);
                    tvWaktuSelesai.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    txtviewparam3.setMargins(300, 150, 0, 0);
                    tvWaktuSelesai.setLayoutParams(txtviewparam3);

                    // Ambil namaCarwash terus pasang ke namanya
                    DocumentReference docRef2 = db.collection(listTipeCarwash.get(i-1)).document(listIdCarwash.get(i-1));
                    final TextView tview = new TextView(getContext());
                    docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentCarwash = task.getResult();
                            if(documentCarwash.exists()){
                                String tempNamaCarwash = documentCarwash.getString("nama");

                                RelativeLayout.LayoutParams txtviewparam = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                                tview.setText(tempNamaCarwash);
                                tview.setTextSize(15);
                                tview.setTypeface(Typeface.DEFAULT_BOLD);
                                tview.setTypeface(Typeface.SANS_SERIF);
                                tview.setTextColor(Color.BLACK);
                                tview.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                                txtviewparam.setMargins(300, 20, 0, 0);
                                tview.setLayoutParams(txtviewparam);

                                llayout.addView(rlayout);
                                rlayout.addView(imgview);
                                rlayout.addView(tview);
                                rlayout.addView(tvStatusPesanan);
                                rlayout.addView(tvWaktuSelesai);
                            }
                        }
                    });
                }
                return;
            }
        });

    }
}
