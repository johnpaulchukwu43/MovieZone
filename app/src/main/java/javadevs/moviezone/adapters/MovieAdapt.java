package javadevs.moviezone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;


import java.util.List;

import javadevs.moviezone.R;
import javadevs.moviezone.model.Movie;

/**
 * Created by CHUKWU JOHNPAUL on 16/04/17.
 */

public class MovieAdapt extends BaseAdapter {
    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w300";

    ImageView mImg;
    private List<Movie> movieList;

    private Context ctx;

    public MovieAdapt(List<Movie> movieList, Context ctx) {
        this.movieList = movieList;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = (Movie) getItem(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row;
        row = inflater.inflate(R.layout.movie_item, parent, false);
        mImg = row.findViewById(R.id.cover_image);
        String url = BASE_POSTER_URL +
                IMAGE_SIZE +
                movie.getPosterPath().trim();
        Picasso.with(ctx)
                .load(url)
                .placeholder(R.mipmap.ic_load)
                .error(R.mipmap.ic_error)
                .into(mImg);
        return row;
    }

    public void cleanUp() {
        if (!movieList.isEmpty()) {
            movieList.clear();
        }
    }
}
