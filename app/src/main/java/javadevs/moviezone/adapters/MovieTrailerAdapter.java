package javadevs.moviezone.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javadevs.moviezone.Interface.TrailerAdapterCallback;
import javadevs.moviezone.R;
import javadevs.moviezone.model.Trailer;

/**
 * Created by CHUKWU JOHNPAUL on 14/05/17.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {



    private final List<Trailer> trailers;
    private final TrailerAdapterCallback adapterCallback;

    public MovieTrailerAdapter(List<Trailer> trailers, TrailerAdapterCallback callback) {
        this.trailers = trailers;
        this.adapterCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_trailer_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.trailer = trailer;
        holder.trailerName.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Trailer trailer;
        final TextView trailerName;

        ViewHolder(View itemView) {
            super(itemView);
            trailerName = itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(view -> adapterCallback.onItemClickListener(trailer.getKey()));
        }

    }
}
