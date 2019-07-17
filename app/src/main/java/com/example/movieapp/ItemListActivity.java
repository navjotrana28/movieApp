package com.example.movieapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


public class ItemListActivity extends AppCompatActivity implements ItemListFragment.OnHandleMovieClickListener,DetailListFragment.DataSending{


    public static final String MOVIEDATA = "movie_data";
    movie movieList;
  FavViewModel favViewModel;
  MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_result);

       favViewModel = ViewModelProviders.of(this).get(FavViewModel.class);
       movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .replace(R.id.item_list_fragment, new ItemListFragment())
                    .commit();

        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .replace(R.id.first_fragment, new ItemListFragment())
                    .commit();
        }
    }

    @Override
    public void onMovieClick(com.example.movieapp.movie movie) {
        this.movieList = movie;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, ItemDetailActivity.class);
            if(movieViewModel.getIsFavourites().getValue()){
                intent.putExtra(MOVIEDATA,favViewModel.getmovie(movie));
            }else {
                intent.putExtra(MOVIEDATA, movie);
            }
            startActivity(intent);
        } else {

            DetailListFragment detailListFragment = new DetailListFragment();
            detailListFragment.setMovie(movie);
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.second_fragment, detailListFragment).commit();
        }
    }

    @Override
    public movie dataStored() {
        return movieList;
    }
}
