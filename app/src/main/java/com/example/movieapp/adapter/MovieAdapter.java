package com.example.movieapp.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movieapp.AdapterInterface;
import com.example.movieapp.R;
import com.example.movieapp.movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w500";
    private static final String YOU_CLICKED = "you clicked:";
    private AdapterInterface adapterCallback;
    private movie clickedDataItem;
    private Context mcontext;

    public List<movie> getMovieList() {
        return movieList;
    }

    private List<movie> movieList;

    public MovieAdapter(Context mcontext, List<movie> movieList, AdapterInterface adapterCallback) {
        this.mcontext = mcontext;
        this.movieList = movieList;
        this.adapterCallback = adapterCallback;
    }

    @NonNull
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);
        MyViewHolder mHolder = new MyViewHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.MyViewHolder viewHolder, int i) {
        Picasso.with(mcontext)
                .load(IMAGE_PATH + movieList.get(i).getPoster_path())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(viewHolder.thumbnail);
        viewHolder.movieName.setText(movieList.get(i).getOriginal_title());
    }

    public void setMovies(List<movie>movieTemp){
        movieList=movieTemp;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(movieList!=null) {
            return movieList.size();
        }else{
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView movieName;

        MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            movieName = view.findViewById(R.id.movie_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        clickedDataItem = movieList.get(pos);
                        Toast.makeText(v.getContext(), YOU_CLICKED + clickedDataItem.getTitle(), Toast.LENGTH_SHORT).show();
                        adapterCallback.onMethodCallback(movieList.get(pos));
                    }
                }
            });
        }
    }
}