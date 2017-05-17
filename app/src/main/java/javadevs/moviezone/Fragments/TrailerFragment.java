package javadevs.moviezone.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import javadevs.moviezone.DetailActivity;
import javadevs.moviezone.Interface.TrailerAdapterCallback;
import javadevs.moviezone.Interface.TrailerCallBack;
import javadevs.moviezone.R;
import javadevs.moviezone.Util.FetchMovieTrailerAsync;
import javadevs.moviezone.adapters.MovieTrailerAdapter;
import javadevs.moviezone.model.Trailer;

/**
 * Created by CHUKWU JOHNPAUL on 13/05/17.
 */

public class TrailerFragment extends Fragment {
    private static final String YOUTUBE_APP_PACKAGE = "com.google.android.youtube";
    private static final String YOUTUBE_URL_APP = "vnd.youtube://";
    private static final String YOUTUBE_URL_BROWSER = "https://www.youtube.com/watch";
    private static final String VIDEO_PARAMETER = "v";
    private static final String MOVIE_KEY = "movie_key" ;
    private static final String KEY_TRAILER_LIST ="key_trailer";
    TextView trailersTitle,errorMessage;
    public static TextView no_trailer_message;
    RecyclerView recyclerView;
    MovieTrailerAdapter mAdapter;
    private ArrayList<Trailer> MovieTrailersList;
    int movieId;
    public static ProgressBar trailerProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            movieId = DetailActivity.id;
            MovieTrailersList = new ArrayList<>();
            updateTrailerAdapters(movieId);
        }
        else{
            movieId = savedInstanceState.getInt(MOVIE_KEY);
            MovieTrailersList = savedInstanceState.getParcelableArrayList(KEY_TRAILER_LIST);
            updateTrailerAdapters(movieId);
        }
    }

    public static TrailerFragment newInstance() {
        TrailerFragment fragment = new TrailerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_trailer,container,false);
        trailersTitle = (TextView) mView.findViewById(R.id.trailer_name);
        trailerProgressBar = (ProgressBar) mView.findViewById(R.id.pb_trailer_loader);
        no_trailer_message = (TextView)mView.findViewById(R.id.no_trailer);
        MovieTrailersList = new ArrayList<>();
        errorMessage = (TextView) mView.findViewById(R.id.error_message);
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_trailer);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MovieTrailerAdapter(MovieTrailersList,
                new TrailerAdapterCallback() {
                    @Override
                    public void onItemClickListener(String trailerKey) {
                        if (trailerKey != null ) {
                            PlayMovieTrailer(trailerKey);
                        }
                    }
                });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        return mView;
    }
    public void updateTrailerAdapters(int movieId){
        FetchMovieTrailerAsync fetchMovieTrailerAsync = new FetchMovieTrailerAsync(new TrailerCallBack() {
            @Override
            public void updateAdapter(Trailer[] trailers) {
                if (trailers != null) {
                    MovieTrailersList.clear();
                    Collections.addAll(MovieTrailersList, trailers);
                    mAdapter.notifyDataSetChanged();

                }
            }
        },getActivity());
        fetchMovieTrailerAsync.execute(movieId);
    }

    private void PlayMovieTrailer(String trailerKey) {
        Intent intent;
        if (checkForYouTubeApp()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL_APP + trailerKey));
        } else {
            Uri uri = Uri.parse(YOUTUBE_URL_BROWSER)
                    .buildUpon()
                    .appendQueryParameter(VIDEO_PARAMETER, trailerKey)
                    .build();
            intent = new Intent(Intent.ACTION_VIEW, uri);
        }
        startActivity(intent);
    }

    private boolean checkForYouTubeApp() {
        return getActivity().getPackageManager()
                .getLaunchIntentForPackage(YOUTUBE_APP_PACKAGE) != null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_TRAILER_LIST,MovieTrailersList);
        outState.putInt(MOVIE_KEY,movieId);

    }


}
