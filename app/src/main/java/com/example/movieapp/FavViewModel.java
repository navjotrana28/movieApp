package com.example.movieapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private LiveData<List<movie>> mAllmovies;

    public FavViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        mAllmovies = movieRepository.getAllMovies();
    }

    public LiveData<List<movie>> getAllmovies() {
        return mAllmovies;
    }

    public movie getmovie(movie movie) {
        for (int i = 0; i < mAllmovies.getValue().size(); i++) {
            if (movie.getTitle() == mAllmovies.getValue().get(i).getTitle())
                return mAllmovies.getValue().get(i);
        }
        return null;
    }

    public void insertmovie(movie movieObject) {
        movieRepository.insertMovieToFavourites(movieObject);
    }

    public void deletemovie(movie movie) {
        movieRepository.deleteMovieFromFavourites(movie);
    }
}
