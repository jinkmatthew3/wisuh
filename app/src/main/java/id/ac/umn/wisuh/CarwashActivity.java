package id.ac.umn.wisuh;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
    private String imgv;

    //Firebase Firestore
    private FirebaseFirestore db;
    ArrayList<String> listCarwash;
    ArrayList<String> listIdCarwash;
    ArrayList<Image> listFotoCarwash;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwash);

        //bikin arraylist
        listCarwash = new ArrayList<>();
        listFotoCarwash = new ArrayList<>();
        listIdCarwash = new ArrayList<>();

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
        Log.d("testingCarwash3",listCarwash.size()+" ");
        //Adding secara dynamic
        for (int i = 1; i <= listCarwash.size(); i++) {
            RelativeLayout rlayout = new RelativeLayout(this);
            LayoutParams imageButtonParam = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            rlayout.setBackgroundResource(R.drawable.input_field);
            rlayout.setPadding(20, 20, 20, 20);


            ImageView imgview = new ImageView(this);
            imgview.setLayoutParams(imageButtonParam);
//            imgview.setId(Integer.parseInt("imgv"));
            imgview.getLayoutParams().height = 410;
            imgview.getLayoutParams().width = 1100;
//            android:layout_weigth = "0.11"
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
            tview.setLayoutParams(txtviewparam);
            tview.setText(listCarwash.get(i-1));
            tview.setTextSize(15);
            tview.setTypeface(Typeface.DEFAULT_BOLD);
            tview.setTypeface(Typeface.SANS_SERIF);
            tview.setTextColor(Color.BLACK);
//            txtviewparam.addRule(RelativeLayout.RIGHT_OF, Integer.parseInt(imgv));
////            txtviewparam.addRule(RelativeLayout.END_OF, Integer.parseInt(imgv));
////            txtviewparam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            tview.setLayoutParams(txtviewparam);
//            tview.getMarginStart
//


//            TextView desctv = new TextView(this);
//            desctv.setId(Integer.parseInt("desccarwash"));
//            LayoutParams desctvparams = new LayoutParams(
//                    LayoutParams.WRAP_CONTENT,
//                    LayoutParams.WRAP_CONTENT);
//            desctvparams.addRule(RelativeLayout.RIGHT_OF, Integer.parseInt(imgv));
//            desctvparams.addRule(RelativeLayout.END_OF, Integer.parseInt(imgv));
//            desctvparams.addRule(RelativeLayout.BELOW, Integer.parseInt("desccarwash"));
//<!--            android:layout_weight="1"-->
//<!--            android:paddingLeft="5dp"/>-->


            llayout.addView(rlayout);
            rlayout.addView(imgview);
            rlayout.addView(tview);

        }
    }
}


