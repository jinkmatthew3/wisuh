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

import org.w3c.dom.Text;

public class ProfileFragment extends androidx.fragment.app.Fragment {
    TextView tvuname, tvnohp,tvemail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvuname = view.findViewById(R.id.tvuname);
        tvnohp = view.findViewById(R.id.tvnoHp);
        tvemail = view.findViewById(R.id.tvemail);

        String fName = getArguments().getString("fname");
        String lName = getArguments().getString("lname");
        String email = getArguments().getString("email");
        String pNumber = getArguments().getString("nomorHp");
//        Intent intent= new getIntent();
//
//        final String email = intent.getStringExtra("email");
//        final String pNumber = intent.getStringExtra("nomorHp");
//        final String fName = intent.getStringExtra("fname");
//        final String lName = intent.getStringExtra("lname");
        tvuname.setText(fName+" "+lName);
        tvnohp.setText(pNumber);
        tvemail.setText(email);
        return view;
    }
}
