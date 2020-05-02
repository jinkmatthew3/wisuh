package id.ac.umn.wisuh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends androidx.fragment.app.Fragment {
    TextView tvuname, tvFname,tvLname, tvnoHp,tvemail;
    Button btnEditProfil;
    ImageButton btnEditfotoProfil;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    CircleImageView fotoprofil;
    StorageReference storageReference;
    //Firebase Firestore
    private FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvuname = view.findViewById(R.id.tvuname);
        tvnoHp = view.findViewById(R.id.tvnoHp);
        tvemail = view.findViewById(R.id.tvemail);
        tvFname = view.findViewById(R.id.tvFname);
        tvLname = view.findViewById(R.id.tvLname);
        fotoprofil = view.findViewById(R.id.fotoProfil);
        btnEditfotoProfil = view.findViewById(R.id.btnEditfotoProfil);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();
        DocumentReference docRef = db.collection("users").document(user.getUid());


        StorageReference profilRef = storageReference.child("users/"+user.getUid()+"/profil.jpg");
        profilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(fotoprofil);
            }
        });
//        btnEditProfil = view.findViewById(R.id.btnEditProfil);

//        String fName = getArguments().getString("fname");
//        String lName = getArguments().getString("lname");
//        String email = getArguments().getString("email");
//        String pNumber = getArguments().getString("nomorHp");
//        Log.d("profil name",fName);



        if(user != null){
//            docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String email = document.getString("email");
                            String pNumber = document.getString("pNumber");
                            String fName = document.getString("fName");
                            String lName = document.getString("lName");
                            tvuname.setText("Hi, "+fName+" "+lName);
                            tvFname.setText(fName);
                            tvLname.setText(lName);
                            tvemail.setText(email);
                            tvnoHp.setText(pNumber);
                        } else {
                            Log.d("signIn", "No such document");
                        }
                    } else {
                        Log.d("signIn", "get failed with ", task.getException());
                    }
                }
            });
        }

//        BUAT GANTI FOTO
        btnEditfotoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);


            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                fotoprofil.setImageURI(imageUri);
                uploadImagetoFirebase(imageUri);
            }
        }
    }
    private void uploadImagetoFirebase(Uri imageUri){
        final StorageReference fileRef = storageReference.child("users/"+user.getUid()+"/profil.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(fotoprofil);
                    }
                });
//                Toast.makeText(getActivity(),"Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void buttonClickedEditFName(View view) {
//        DocumentReference docRef = db.collection("users").document(user.getUid());
//        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.editfnamelayout, null);
//        final EditText etUsername = alertLayout.findViewById(R.id.et_username);
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("Name Edit");
//        // this is set the view from XML inside AlertDialog
//        alert.setView(alertLayout);
//        // disallow cancel of AlertDialog on click of back button and outside touch
//        alert.setCancelable(false);
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String fname = tvFname.getText().toString();
//                String lname = tvLname.getText().toString();
//                String nohp =  tvnoHp.getText().toString();
//                String email = tvemail.getText().toString();
//                Customer userinformation = new Customer(null,fname,lname,nohp,email,null);
//                FirebaseUser user = mAuth.getCurrentUser();
//                docRef.child(user.getUid()).setValue(userinformation);
//                docRef.child(user.getUid()).setValue(userinformation);
//                etUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
//            }
//        });
//        AlertDialog dialog = alert.create();
//        dialog.show();
    }

    public void buttonClickedEditLName(View view) {

    }
    public void buttonClickedEditNohp(View view) {

    }
    public void buttonClickedEditEmail(View view) {

    }
}
