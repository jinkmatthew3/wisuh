package id.ac.umn.wisuh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProfileFragment extends androidx.fragment.app.Fragment {
    TextView tvuname, tvFname,tvLname, tvnoHp,tvemail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvuname = view.findViewById(R.id.tvuname);
        tvnoHp = view.findViewById(R.id.tvnoHp);
        tvemail = view.findViewById(R.id.tvemail);
        tvFname = view.findViewById(R.id.tvFname);
        tvLname = view.findViewById(R.id.tvLname);

        String fName = getArguments().getString("fname");
        String lName = getArguments().getString("lname");
        String email = getArguments().getString("email");
        String pNumber = getArguments().getString("nomorHp");
        Log.d("profil name",fName);

        tvuname.setText("Hi, "+fName+" "+lName);
        tvFname.setText(fName);
        tvLname.setText(lName);
        tvemail.setText(email);
        tvnoHp.setText(pNumber);
        return view;
    }
}
