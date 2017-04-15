package javadevs.moviezone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.media.CamcorderProfile.get;

/**
 * Created by CHUKWU JOHNPAUL on 15/04/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private ArrayList<Movie> myMovie;// generic list that only accepts type of Movie class
    private Context ctx;

    public MovieAdapter(ArrayList<Movie> myMovie, Context ctx) {
        this.myMovie = myMovie;
        this.ctx = ctx;
    }

    @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.movie_item,parent,false);
            return new MovieAdapter.ViewHolder(mView,ctx,myMovie);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Movie movieItem = myMovie.get(position);
            Picasso.with(ctx).load(Uri.parse("http://image.tmdb.org/t/p/w300/"+movieItem.getPosterPath()))
                    .placeholder(R.drawable.avatar)
//                    .error(R.drawable.ic_no_poster_available)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return myMovie.size();
        }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        ArrayList<Movie> myMovies = new ArrayList<Movie>();
        Context ctx;
        
        public ViewHolder(View itemView, Context ctx, ArrayList<Movie>myMovies) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.myMovies = myMovies;
            this.ctx = ctx;
            imageView = (ImageView)itemView.findViewById(R.id.moviePoster);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie mymovie = this.myMovies.get(position);

            Intent intent = new Intent(this.ctx, DetailActivity.class);


            intent.putExtra("title",mymovie.getTitle());
            intent.putExtra("posterpath",mymovie.getPosterPath());
            intent.putExtra("overview",mymovie.getOverview());
            intent.putExtra("releasedate",mymovie.getReleaseDate());
            intent.putExtra("voteAverage",mymovie.getVoteAverage());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            this.ctx.startActivity(intent);
        }
    }
}
