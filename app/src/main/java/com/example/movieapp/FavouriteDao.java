package com.example.movieapp;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Query("SELECT * FROM favouritetable")
    LiveData<List<movie>> loadAllFavourites();

    @Insert
    void insertFavourite(movie favouriteEntry);

    @Delete
    void deleteFavourite(movie favouriteEntry);

}