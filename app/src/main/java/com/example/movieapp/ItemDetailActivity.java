package com.example.movieapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity implements DetailListFragment.DataSending {

     movie Movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_new);
        Intent intentthatstartedthisAActivity = getIntent();
        if (intentthatstartedthisAActivity.hasExtra("movie_data")) {
             Movie = (com.example.movieapp.movie) intentthatstartedthisAActivity.getExtras().get("movie_data");

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailListFragment fragment = new DetailListFragment();
            fragmentTransaction
                    .replace(R.id.Detail_list_fragment, fragment)
                    .commit();
        }
    }
    @Override
    public movie dataStored() {
        return  Movie;
    }
}
