package javadevs.moviezone.adapters;

import android.content.Context;
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
    private List<Review> itemlist;
    Context context;


    public MovieReviewAdapter(Context context, List<Review> itemlist) {
        this.context = context;
        this.itemlist = itemlist;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView review_author, review_content;


        public ViewHolder(View view) {
            super(view);
            review_author = (TextView) view.findViewById(R.id.review_author);
            review_content = (TextView) view.findViewById(R.id.review_content);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_review_item, viewGroup, false);
        return new ViewHolder(view);


    }


    boolean isOdd(int val)

    {
        return (val & 0x01) != 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.review_author.setText(itemlist.get(position).getAuthor());
        viewHolder.review_content.setText(itemlist.get(position).getContent());

    }


    @Override
    public int getItemCount() {
        if (itemlist != null) {
            return itemlist.size();
        } else return 0;
    }

}



