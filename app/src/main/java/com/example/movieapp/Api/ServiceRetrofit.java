package com.example.movieapp.Api;

import com.example.movieapp.ReviewResults;
import com.example.movieapp.TrailerResults;
import com.example.movieapp.movieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceRetrofit {

    String CATEGORY = "category";
    String API_KEY = "api_key";
    String MOVIE_ID = "movie_id";

    @GET("/3/movie/{category}")
    Call<movieResults> popularMovies(
            @Path(CATEGORY) String category,
            @Query(API_KEY) String apiKey,
            @Query("page") int PAGE);

    @GET("/3/movie/{movie_id}/videos")
    Call<TrailerResults> getMovieTrailer(
            @Path(MOVIE_ID) int id,
            @Query(API_KEY) String apiKey
    );

    @GET("/3/movie/{movie_id}/reviews")
    Call<ReviewResults> getReviews(
            @Path(MOVIE_ID) int id,
            @Query(API_KEY) String apiKey
    );
}