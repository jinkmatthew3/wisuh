package id.ac.umn.wisuh;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class MyAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TopUpActivity fragment_top_up_activity = new TopUpActivity();
                return fragment_top_up_activity;
            case 1:
                RegisterCarWashActivity fragment_register_car_wash_activity = new RegisterCarWashActivity();
                return fragment_register_car_wash_activity;
            case 2:
                RegisterCarSalonActivity fragment_register_car_salon_activity = new RegisterCarSalonActivity();
                return fragment_register_car_salon_activity;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}