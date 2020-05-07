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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    ImageButton btnEditfotoProfil, buttonClickedEditFName,buttonClickedEditLName,buttonClickedEditNohp,buttonClickedEditEmail;
    AlertDialog.Builder dialog;

//    Dialog buat edit informasi data diri
    EditText DETfname,DETlname,DETnohp,DETemail;
    String fname,lname,nohp,Eemail,email,fName,lName,pNumber;
    DatabaseReference databaseReference;

    View dialogView;
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
        buttonClickedEditFName = view.findViewById(R.id.editFname);
        buttonClickedEditLName = view.findViewById(R.id.editLname);
        buttonClickedEditNohp = view.findViewById(R.id.editNohp);
        buttonClickedEditEmail = view.findViewById(R.id.editEmail);



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
//        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());


        final DocumentReference docRef = db.collection("users").document(user.getUid());

//        buat ambil data foto profil dari firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profilRef = storageReference.child("users/"+user.getUid()+"/profil.jpg");
        profilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(fotoprofil);
            }
        });


        if(user != null){
//            docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            email = document.getString("email");
                            pNumber = document.getString("pNumber");
                            fName = document.getString("fName");
                            lName = document.getString("lName");
                            tvuname.setText("Hi, "+fName+" "+lName);
                            tvFname.setText(fName);
                            tvLname.setText(lName);
                            tvemail.setText(email);
                            tvnoHp.setText(pNumber);
                        }
//                        TINGGAL DI BENERIN LOGHNYA
                        else {
                            Log.d("signIn", "No such document");
                        }
                    } else {
                        Log.d("signIn", "get failed with ", task.getException());
                    }
                }
            });
        }

//      buat ganti foto profil ambil dari media store
        btnEditfotoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);


            }
        });
        buttonClickedEditFName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.editfnamelayout, null);

                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Edit First Name");
                DETfname = dialogView.findViewById(R.id.DETfname);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fname = DETfname.getText().toString();

                        if(!fname.isEmpty()){
                            docRef.update("fName",fname);
                            tvuname.setText("Hi, "+fname+" "+lName);
                            tvFname.setText(fname);
                        }

                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        buttonClickedEditLName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.editlnamelayout, null);

                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Edit Laast Name");
                DETlname = dialogView.findViewById(R.id.DETlname);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lname = DETlname.getText().toString();

                        if(!lname.isEmpty()){
                            docRef.update("lName",lname);
                            tvuname.setText("Hi, "+fname+" "+lname);
                            tvLname.setText(lname);
                        }

                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        buttonClickedEditNohp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.editnohplayout, null);

                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Edit Number Phone");
                DETnohp = dialogView.findViewById(R.id.DETnohp);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nohp = DETnohp.getText().toString();

                        if(!nohp.isEmpty()){
                            docRef.update("pNumber",nohp);
                            tvnoHp.setText(nohp);
                        }

                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        buttonClickedEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.editemaillayout, null);

                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Edit Email");
                DETemail = dialogView.findViewById(R.id.DETemail);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Eemail = DETemail.getText().toString();

                        if(!Eemail.isEmpty()){
                            docRef.update("email",Eemail);
                            tvemail.setText(Eemail);
                        }

                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
    
}
