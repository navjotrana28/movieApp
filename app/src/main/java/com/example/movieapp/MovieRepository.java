package com.example.movieapp;
import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

class MovieRepository {
//add variables for Dao and list of words
    private FavouriteDao mFavouriteDao;
    private LiveData<List<movie>> mAllMovies;
//add constructor that gets a handle to the database
    MovieRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mFavouriteDao = db.favouriteDao();
        mAllMovies = mFavouriteDao.loadAllFavourites();
    }
  //returns teh cached movies as livedata
    LiveData<List<movie>> getAllMovies() {
        return mAllMovies;
    }

    @SuppressLint("LongLogTag")
   public  void insertMovieToFavourites(movie movieObject) {
        Log.d("Insert Movie To Favourites", movieObject.getTitle());
        new insertAsyncTask(mFavouriteDao).execute(movieObject);
    }

    private static class insertAsyncTask extends AsyncTask<movie, Void, Void> {

        private FavouriteDao mAsyncTaskDao;

        insertAsyncTask(FavouriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final movie... params) {
            mAsyncTaskDao.insertFavourite(params[0]);
            return null;
        }
    }

    @SuppressLint("LongLogTag")
    void deleteMovieFromFavourites(movie movie) {
        Log.d("Delete Movie From Favourites", movie.getTitle());
        new deleteAsyncTask(mFavouriteDao).execute(movie);
    }

    private static class deleteAsyncTask extends AsyncTask<movie, Void, Void> {

        private FavouriteDao mAsyncTaskDao;

        deleteAsyncTask(FavouriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final movie... params) {
            mAsyncTaskDao.deleteFavourite(params[0]);
            return null;
        }
    }
}
