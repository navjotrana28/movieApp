package com.example.movieapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    ItemListFragment itemListFragment = new ItemListFragment();

    public static final String POPULAR="Popular";
    public static final String FAVOURITES="Favourites";


    private MutableLiveData<Boolean> isFavourites = new MutableLiveData<>();
    private MutableLiveData<String> movieCategory = new MutableLiveData<>();

    public void setMovieList(List<movie> movieList) {
        this.movieList = movieList;
    }

    public List<movie> getMovieList() {
        return movieList;
    }
    private List<movie>movieList;
    private MutableLiveData<List<movie>> mutableMovieList;

    public MutableLiveData<List<movie>> getMutableMovieList() {
        return mutableMovieList;
    }

    public LiveData<String> getType() {
        return this.movieCategory;
    }

    public List<movie> getMoviesFromModel() {
        return mutableMovieList.getValue();
    }

    public  movie movie;
    public LiveData<Boolean> getIsFavourites() {
        return this.isFavourites;
    }

    public MovieViewModel(Application application) {
        super(application);
        isFavourites.setValue(false);
        movieCategory.setValue(POPULAR);
    }

    public int moviePos;
    public void setTypeValue(String str) {
        movieCategory.setValue(str);
    }

    public void getMutableMovies(String category, int page, boolean isScroll) {
        if (mutableMovieList == null) {
            mutableMovieList = new MutableLiveData<>();
        }
       /* if (!isScroll && mutableMovieList.getValue() != null)
            mutableMovieList.getValue().clear();*/
        if (!category.equals(FAVOURITES)) {
            isFavourites.setValue(false);
            itemListFragment.loadJSON(category,page);
            /*DataProvider.loadMovies(category, page, isScroll, mutableMovieList.getValue(), new DataProvider.DataNotifier() {
                @Override
                public void publishResults(List<movie> movieList) {
                    mutableMovieList.setValue(movieList);
                }
            });*/
        } else {
            isFavourites.setValue(true);
        }
    }

}