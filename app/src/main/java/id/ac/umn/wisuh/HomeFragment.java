package id.ac.umn.wisuh;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private ImageButton btnnearby;
    private ImageButton btnsalon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        btnnearby = view.findViewById(R.id.btn_carwash);
        btnnearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindahnearby = new Intent(getActivity().getApplication(), CarwashActivity.class);
                startActivity(pindahnearby);
            }
        });

        btnsalon = view.findViewById(R.id.btn_salon);
        btnsalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindahsalon = new Intent(getActivity().getApplication(), CarsalonActivity.class);
                startActivity(pindahsalon);
            }
        });

        return view;
    }

}
