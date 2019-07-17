package com.example.movieapp.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movieapp.R;
import com.example.movieapp.Reviews;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private Context context;
    private List<Reviews> reviewsList;

    public ReviewAdapter(Context context, List<Reviews> reviewList) {
        this.context = context;
        this.reviewsList = reviewList;
    }

    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewgroup, int i) {
        View view = LayoutInflater.from(viewgroup.getContext())
                .inflate(R.layout.reviews_card, viewgroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int i) {
        viewHolder.authorName.setText(reviewsList.get(i).getAuthor());
        viewHolder.authorReviews.setText(reviewsList.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView authorName, authorReviews;

        MyViewHolder(View view) {
            super(view);
            authorName = view.findViewById(R.id.author_name);
            authorReviews = view.findViewById(R.id.author_reviews);
        }

    }
}

