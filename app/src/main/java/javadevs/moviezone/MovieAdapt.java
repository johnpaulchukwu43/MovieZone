package javadevs.moviezone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by CHUKWU JOHNPAUL on 16/04/17.
 */

public class MovieAdapt extends BaseAdapter{
    private final static String BASE_POSTER_URL="https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w300" ;
    private ArrayList<Movie> myMovie;
    // generic list that only accepts type of Movie class
    private Context ctx;

    //Movie Adapt Constructor
    public MovieAdapt(ArrayList<Movie> myMovie, Context ctx) {
        this.myMovie = myMovie;
        this.ctx = ctx;
    }
    //Get total Count
    @Override
    public int getCount() {
        return myMovie.size();
    }
    //Get item at specified position
    @Override
    public Object getItem(int position) {
        return myMovie.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //return the Image view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie mymovie= (Movie) getItem(position);
        ImageView mImg;
        if(convertView == null ){
            LayoutInflater inflater= (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mImg=(ImageView) inflater.inflate(R.layout.movie_item,parent,false);
        }else{
            mImg=(ImageView) convertView;
        }

        String url= new StringBuilder()
                .append(BASE_POSTER_URL)
                .append(IMAGE_SIZE)
                .append(mymovie.getPosterPath().trim())
                .toString();

        Picasso.with(ctx)
                .load(url)
                .placeholder(R.mipmap.ic_load)
                .error(R.mipmap.ic_error)
                .into(mImg);
        return mImg;
    }

    public void cleanUp(){
        if(myMovie.size() > 0){
           myMovie.clear();
        }
    }
}
