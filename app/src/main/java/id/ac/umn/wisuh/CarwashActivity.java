package id.ac.umn.wisuh;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

public class CarwashActivity extends AppCompatActivity {
    LinearLayout llayout;
    private int btni;

    //Firebase Firestore
    private FirebaseFirestore db;
    ArrayList<String> listCarwash;
    ArrayList<Image> listFotoCarwash;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwash);

        //bikin arraylist
        listCarwash = new ArrayList<>();
        listFotoCarwash = new ArrayList<>();

        //ambil database
        db = FirebaseFirestore.getInstance();

        //ambil data dari koleksi Carwash firestore
        db.collection("Carwash")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("testingCarwash", document.getId() + " => " + document.getData());
                                String tempString = document.getString("nama");
                                //Log.d("testingCarwash",tempString);
                                listCarwash.add(tempString);
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
                            /*for (String namaMobil : listCarwash) {
                                Log.d("testingCarwash2",listCarwash.size()+" ");
                                Log.d("testingCarwash2", namaMobil);
                            }*/
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
        btni = 1;
        Log.d("testingCarwash3",listCarwash.size()+" ");
        //Adding secara dynamic
        for (int i = 1; i <= listCarwash.size(); i++) {
            RelativeLayout rlayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams imageButtonParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlayout.setId(i);
            rlayout.setPadding(20, 20, 20, 20);


            ImageButton imgbtn = new ImageButton(this);
            imgbtn.setLayoutParams(imageButtonParam);
            imgbtn.setId(btni);
            imgbtn.getLayoutParams().height = 510;
            imgbtn.getLayoutParams().width = 1100;
            imgbtn.setBackgroundResource(R.drawable.input_field);
            imgbtn.setPadding(16,16,16,16);
            imgbtn.setScaleType(ImageButton.ScaleType.FIT_START);
            imgbtn.setImageResource(R.drawable.car_washing_icon);

            TextView tview = new TextView(this);
            tview.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            tview.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            tview.setLayoutParams(imageButtonParam);
            tview.setText(listCarwash.get(i-1));
            tview.setTextSize(15);
            tview.setTypeface(Typeface.DEFAULT_BOLD);
            tview.setTypeface(Typeface.SANS_SERIF);
            tview.setTextColor(Color.BLACK);
            tview.setGravity(Gravity.BOTTOM);
//            tview.getMarginStart
//
//            android:layout_marginStart="-370dp"
//            android:layout_marginTop="150dp"
//            android:layout_toEndOf="@+id/btn_carwash"

            //TextView textView = new TextView(this);
            //textView.setText("TextView " + String.valueOf(i));
            llayout.addView(rlayout);
            rlayout.addView(imgbtn);
            rlayout.addView(tview);
            btni = btni + 1;
        }
    }
}


