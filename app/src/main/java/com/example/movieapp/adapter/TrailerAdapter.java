package com.example.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.Trailers;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {
    private static final String HTTP_IMG_YOUTUBE_COM = "http://img.youtube.com/vi/";
    private static final String VIDEOID = "videoid";
    private static final String HTTPS_WWW_YOUTUBE_COM_WATCH = "https://www.youtube.com/watch?v=";
    private static final String YOUCLICKED = "youclicked";
    private static final String JPG = "/0.jpg";
    private Context context;
    private List<Trailers> trailersList;

    public TrailerAdapter(Context context, List<Trailers> listOfTrailers) {
        this.context = context;
        this.trailersList = listOfTrailers;
    }

    @NonNull
    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int i) {
        View view = LayoutInflater.from(viewgroup.getContext())
                .inflate(R.layout.trailer_card, viewgroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.MyViewHolder viewHolder, int i) {
        Picasso.with(context)
                .load(HTTP_IMG_YOUTUBE_COM + trailersList.get(i).getKey() + JPG)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail_trailer);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Trailers clickedDataItem = trailersList.get(pos);
                        String videoId = trailersList.get(pos).getKey();
                        Log.d(VIDEOID, videoId);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(HTTPS_WWW_YOUTUBE_COM_WATCH + videoId));
                        context.startActivity(intent);
                        Toast.makeText(v.getContext(), YOUCLICKED + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
