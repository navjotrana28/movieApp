package com.example.movieapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Api.ClientRetrofit;
import com.example.movieapp.Api.ServiceRetrofit;
import com.example.movieapp.adapter.ReviewAdapter;
import com.example.movieapp.adapter.TrailerAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.ContactsContract.Intents.Insert.ACTION;
import static com.example.movieapp.ItemListFragment.API_KEY;

public class DetailListFragment extends Fragment {

    public void setMovie(com.example.movieapp.movie movie) {
        this.movie = movie;
    }

    public interface DataSending {
        movie dataStored();
    }

    private ImageView imageView;
    private TextView original_title, release_date, synopsis;
    private RatingBar ratingBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private int movie_id;
    private RecyclerView recyclerView, recyclerView2;
    private TrailerAdapter trailerAdapter;
    private List<Trailers> trailersList;
    private List<Reviews> reviewsList;
    private TextView reviews;
    private TextView trailersText;
    private ReviewAdapter reviewAdapter;
    private DataSending dataSending;
    private movie movie;
    private FavViewModel favoriteViewModel;
    private boolean floatingFlag = false;
    int movieId;
    String originalTitle;
    String imageview;


    public DetailListFragment() {
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            dataSending = (DataSending) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ENTERED", "FRAGMENT");
        View v = inflater.inflate(R.layout.fragment_detail_list, container, false);

        movie = dataSending.dataStored();
//        Log.d("ENTERED2","FRAGMENT");

        favoriteViewModel = ViewModelProviders.of(this).get(FavViewModel.class);

        toolbar = v.findViewById(R.id.detail_toolbar);
        collapsingToolbarLayout = v.findViewById(R.id.collapsing_toolbar);
        recyclerView = v.findViewById(R.id.recycler_view_trailer);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageView = v.findViewById(R.id.thumbnail_image_header);
        original_title = v.findViewById(R.id.movie_title);
        release_date = v.findViewById(R.id.releaseDate);
        synopsis = v.findViewById(R.id.plotsynopsis);
        ratingBar = v.findViewById(R.id.ratingBar);
        reviews = v.findViewById(R.id.reviews_text);
        final FloatingActionButton floatingActionButton = v.findViewById(R.id.fab);

        trailersText = v.findViewById(R.id.trailers);
        recyclerView2 = v.findViewById(R.id.recycler_view_reviews);

        if (movie != null) {
            collapsingToolbarLayout.setTitle(movie.getOriginal_title());
            release_date.setText(movie.getRelease_date());
            ratingBar.setRating((float) movie.getVote_average());
            original_title.setText(movie.getOriginal_title());
            synopsis.setText(movie.getOverview());
            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w500" + movie.getBackdrop_path())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView);
            movie_id = movie.getId();
        } else {
            Toast.makeText(getActivity(), "No API Data", Toast.LENGTH_SHORT).show();
        }

        initViewsTrailers();

        initViewsReviews();

        if (movie != null) {
            favoriteViewModel.getAllmovies().observe(this, new Observer<List<movie>>() {
                @Override
                public void onChanged(List<movie> movieList) {
                    for (movie m : movieList) {
                        if (m.getId() == movie.getId()) {
                            floatingFlag = true;

                        }
                    }
                }
            });
        }

        if (movie != null) {
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("FAVVVV ","On");

                    if (floatingFlag) {
                        favoriteViewModel.deletemovie(movie);
                        floatingFlag = false;
                        Snackbar.make(view, "Removed", Snackbar.LENGTH_LONG)
                                .setAction(ACTION, null).show();
                    } else {
                        favoriteViewModel.insertmovie(movie);
                        floatingFlag = true;
                        Snackbar.make(view, "Added", Snackbar.LENGTH_LONG)
                                .setAction(ACTION, null).show();
                    }
                }
            });
        }

        return v;
    }

    private void initViewsTrailers() {
        trailersList = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(getActivity(), trailersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager
                (getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(trailerAdapter);
//        trailerAdapter.notifyDataSetChanged();
        loadJSON2();
    }

    private void loadJSON2() {

        try {
            ClientRetrofit clientRetrofit = new ClientRetrofit();
            ServiceRetrofit apiService = ClientRetrofit.getClient().create(ServiceRetrofit.class);

            Call<TrailerResults> call = apiService.getMovieTrailer(movie_id, API_KEY);
            call.enqueue(new Callback<TrailerResults>() {

                @Override
                public void onResponse(Call<TrailerResults> call, Response<TrailerResults> response) {
                    if(response.body() != null) {
                        if (response.body().getResults() != null) {
                            List<Trailers> listofTrailers = response.body().getResults();
                            recyclerView.setAdapter(new TrailerAdapter(getContext(), listofTrailers));
                            recyclerView.smoothScrollToPosition(0);
                            if (listofTrailers.size() == 0)
                                trailersText.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<TrailerResults> call, Throwable t) {
                    Toast.makeText(getActivity(), "error Fetching Trailer data", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initViewsReviews() {
        reviewsList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(getActivity(), reviewsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
        loadJSONreviews();
    }

    private void loadJSONreviews() {

        try {
            ClientRetrofit clientRetrofit = new ClientRetrofit();
            ServiceRetrofit apiService = ClientRetrofit.getClient().create(ServiceRetrofit.class);
            Call<ReviewResults> call = apiService.getReviews(movie_id, API_KEY);
            call.enqueue(new Callback<ReviewResults>() {

                @Override
                public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                    if(response.body() != null) {
                        if (response.body().getResults() != null) {
                            List<Reviews> listofreviews = response.body().getResults();
                            recyclerView2.setAdapter(new ReviewAdapter(getActivity(), listofreviews));
                            recyclerView2.smoothScrollToPosition(0);
                            if (listofreviews.size() == 0) {

                                reviews.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReviewResults> call, Throwable t) {
                    Toast.makeText(getActivity(), "error Fetching Trailer data", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
    }


//    AppDatabase mDb;
//
//    public void savedFavourite() {
//        final movie favouriteEntry = new movie(movieId, originalTitle, imageview);
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                mDb.favouriteDao().insertFavourite(favouriteEntry);
//            }
//        });
//    }
//
//    public void deleteFavourite(final int movieid) {
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                mDb.favouriteDao().deleteFavouriteWithId(movieid);
//            }
//        });
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share: {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "body here";
                String shareSubject = "Your Subject here";

                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(intent, "ShareVia"));
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }
}

//    @SuppressLint("StaticFieldLeak")
//    private void checkStatus(final String movieName) {
//      //        new AsyncTask<Void, Void, Void>() {
//            List<movie> entries = new ArrayList<>();
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                entries.clear();
//                entries = mDb.favouriteDao().loadAll(movieName);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                if (entries.size() > 0) {
//                    floatingFlag = true;
//                    floatingActionButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (floatingFlag == true) {
//                                        savedFavourite();
//                                        Snackbar.make(view, "Added to Favourite", Snackbar.LENGTH_LONG)
//                                                .setAction("Action", null).show();
//                                    } else {
//                                        deleteFavourite(movie_id);
//                                        Snackbar.make(view, "Removed from Favourite", Snackbar.LENGTH_LONG)
//                                                .setAction("Action", null).show();
//
//                                    }
//                                }
//                            });
//                } else {
//                    floatingActionButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (floatingFlag == true) {
//                                savedFavourite();
//                                Snackbar.make(view, "Added to favourite", Snackbar.LENGTH_LONG)
//                                        .setAction("Action", null).show();
//
//                            }
//                        }
//                    });
//
//                }
//            }
//        }.execute();
//    }
//}

