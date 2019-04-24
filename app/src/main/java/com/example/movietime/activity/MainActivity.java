package com.example.movietime.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.movietime.R;
import com.example.movietime.fragment.FavoriteFragment;
import com.example.movietime.fragment.MoviesFragment;
import com.example.movietime.fragment.TvShowFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    private Fragment fragmentCurrent;
    private String title;

    //final variabel
    private final String CURRENT_FRAGMENT = "current_fragment";
    private final String CURRENT_TITLE = "current_title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentCurrent = new MoviesFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);

        if (savedInstanceState == null) {
            setCurrentFragment(fragmentCurrent);
            setTitle(getString(R.string.movies));
        } else {
            setTitle(savedInstanceState.getString(CURRENT_TITLE));
            setCurrentFragment(getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_TITLE, this.title);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT, this.fragmentCurrent);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.setting_language:
                Intent Localintent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(Localintent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_movies:
                    setCurrentFragment(new MoviesFragment());
                    setTitle(getString(R.string.movies));
                    break;
                case R.id.nav_tvshow:
                    setCurrentFragment(new TvShowFragment());
                    setTitle(getString(R.string.tv_show));
                    break;
                case R.id.nav_favorite:
                    setCurrentFragment(new FavoriteFragment());
                    setTitle(getString(R.string.favorite));
            }
            return true;
        }
    };

    public void setCurrentFragment(Fragment fragmentCurrent) {
        this.fragmentCurrent = fragmentCurrent;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentCurrent).commit();
    }

    public void setTitle(String title) {
        this.title = title;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
