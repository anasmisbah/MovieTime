package com.example.movietime.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.movietime.fragment.MovieFavFragment;
import com.example.movietime.fragment.TvShowFavFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private int tabCount;
    public ViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount=tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment current = null;
        switch (i){
            case 0:
                current = new MovieFavFragment();
                break;
            case 1:
                current = new TvShowFavFragment();
                break;
        }
        return current;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position){
            case 0:
                title = "movie";
                break;
            case 1:
                title = "tv show";
                break;
        }
        return title;
    }
}
