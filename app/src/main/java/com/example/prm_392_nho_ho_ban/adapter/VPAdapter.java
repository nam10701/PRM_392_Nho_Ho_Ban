package com.example.prm_392_nho_ho_ban.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.prm_392_nho_ho_ban.fragment.FragmentAllNote;
import com.example.prm_392_nho_ho_ban.fragment.FragmentTodayNote;
import com.example.prm_392_nho_ho_ban.fragment.FragmentUpcomingNote;

public class VPAdapter extends FragmentStateAdapter {
    FragmentAllNote a;
    FragmentUpcomingNote b;
    FragmentTodayNote c;
    public VPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return a = new FragmentAllNote();
            case 2:
                return b = new FragmentUpcomingNote();
            default:
                return c = new FragmentTodayNote();
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }

    public Fragment getItemByPosition(int position) {
        switch (position){
            case 1:
                return a;
            case 2:
                return b;
            default:
                return c;
        }
    }
}
