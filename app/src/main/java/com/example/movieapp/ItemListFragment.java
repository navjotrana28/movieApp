package com.example.movieapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movieapp.Api.ClientRetrofit;
import com.example.movieapp.Api.ServiceRetrofit;
import com.example.movieapp.adapter.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import androidx.annotation.NonNull;

public class ItemListFragment extends Fragment implements AdapterInterface {

    public static String API_KEY = "c8abbd6f5eae017e93c2f0e0e009b255";
    public static String CATEGORY = "popular";
    private static List<movie> movieList = new ArrayList<>();
    public int PAGE = 1;
    boolean flag = false;
    ProgressBar progressBar;
    ProgressDialog pd;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    GridLayoutManager layoutManager;
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private int pos1;
    private OnHandleMovieClickListener onHandleMovieClickListener;
    MovieViewModel movieViewModel;
    FavViewModel favViewModel;


    public ItemListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movieViewModel.setMovieList(adapter.getMovieList());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onHandleMovieClickListener = (OnHandleMovieClickListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmednt
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);

        setHasOptionsMenu(true);

        toolbar = v.findViewById(R.id.toolbar);
        progressBar = v.findViewById(R.id.progress_bar);
        recyclerView = v.findViewById(R.id.recycler_view_01);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        favViewModel = ViewModelProviders.of(this).get(FavViewModel.class);

        movieViewModel.getIsFavourites().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    favViewModel.getAllmovies().observe(ItemListFragment.this, new Observer<List<movie>>() {
                        @Override
                        public void onChanged(List<movie> movieList) {
                            if(movieList != null) {
                                adapter.setMovies(movieList);
                            }

                        }
                    });
                }
            }
        });

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        initViews();
        //drag layout down
        swipeContainer = v.findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag = false;

                initViews();
                Toast.makeText(getActivity(), "movies refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    //progress dialog load json data
    private void initViews() {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Fetching Movies...");
        pd.setCancelable(false);
        pd.show();

        adapter = new MovieAdapter(getActivity(), movieList, this);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        if (!flag) {
            flag = true;
            loadJSON("popular", 1);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged( RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrollOutItems) == totalItems) {
                    isScrolling = false;
                    PAGE = PAGE + 1;
                    progressBar.setVisibility(View.VISIBLE);
                    loadJSON(CATEGORY, PAGE);
                }
            }
        });
    }

    //call client and serviceretrofit-
    public void loadJSON(String category, int page) {
        try {
            ClientRetrofit clientRetrofit = new ClientRetrofit();
            ServiceRetrofit apiService = ClientRetrofit.getClient().create(ServiceRetrofit.class);

            Call<movieResults> call = apiService.popularMovies(category, API_KEY, page);
            call.enqueue(new Callback<movieResults>() {

                @Override
                public void onResponse(Call<movieResults> call, Response<movieResults> response) {
                    movieResults movieresults = response.body();
                    movieList.addAll(movieresults.getResults());
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
//                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<movieResults> call, Throwable t) {
                    Toast.makeText(getActivity(), "error Fetching data", Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movies: {
                movieList.clear();
                CATEGORY = "popular";
                movieViewModel.setTypeValue(CATEGORY);
                loadJSON(CATEGORY, 1);
                return true;
            }
            case R.id.top_rated: {
                movieList.clear();
                CATEGORY = "";
                CATEGORY += "top_rated";
                movieViewModel.setTypeValue(CATEGORY);
                loadJSON(CATEGORY, 1);
                return true;
            }
            case R.id.favourites:{
                movieList.clear();
                CATEGORY="";
                CATEGORY+="favourite";
                movieViewModel.setTypeValue(CATEGORY);
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
//    private void getAllFavourite(){
//        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        viewModel.getFavourite().observe(this, new Observer<List<movie>>() {
//            @Override
//            public void onChanged(List<movie> imageEntry) {
//                List<movie> movies = new ArrayList<>();
//                for(movie entry : imageEntry){
//                    movie movieTemp = new movie();
//                    movieTemp.setId(entry.getId());
//                    movieTemp.setPoster_path(entry.getPoster_path());
//                    movieTemp.setOriginal_title(entry.getOriginal_title());
//                    movies.add(movieTemp);
//                }
//                adapter.setMovies(movies);
//            }
//        });


//    }


    @Override
    public void onMethodCallback(movie movie) {
        onHandleMovieClickListener.onMovieClick(movie);
    }

    public interface OnHandleMovieClickListener {
        void onMovieClick(movie movie);
    }
}
