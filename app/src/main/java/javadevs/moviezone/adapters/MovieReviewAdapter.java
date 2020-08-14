package javadevs.moviezone.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javadevs.moviezone.R;
import javadevs.moviezone.model.Review;

/**
 * Created by CHUKWU JOHNPAUL on 13/05/17.
 * Recycler View Adapter
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {
    private List<Review> reviewList;


    public MovieReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;


        public ViewHolder(View view) {
            super(view);
            reviewAuthor = view.findViewById(R.id.review_author);
            reviewContent = view.findViewById(R.id.review_content);
        }
    }


    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_review_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.reviewAuthor.setText(reviewList.get(position).getAuthor());
        viewHolder.reviewContent.setText(reviewList.get(position).getContent());
    }


    @Override
    public int getItemCount() {
        if (reviewList != null) {
            return reviewList.size();
        } else return 0;
    }

}



