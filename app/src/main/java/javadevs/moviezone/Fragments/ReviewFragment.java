package javadevs.moviezone.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import javadevs.moviezone.DetailActivity;
import javadevs.moviezone.R;
import javadevs.moviezone.Util.FetchMovieReviewAsync;
import javadevs.moviezone.adapters.MovieReviewAdapter;
import javadevs.moviezone.model.Review;


/**
 * Created by CHUKWU JOHNPAUL on 13/05/17.
 */

public class ReviewFragment extends Fragment implements View.OnClickListener {
    private int movieId;
    private static final String KEY_REVIEW_LIST = "keyReviewList";
    private static final String KEY_MOVIE_ID = "movie_id";
    private ImageView refreshImage;
    private TextView errorMessage;
    private MovieReviewAdapter reviewAdapter;
    private ArrayList<Review> reviewItemList ;


    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            movieId = DetailActivity.id;
            reviewItemList = new ArrayList<>();
        }
        else {
            movieId = savedInstanceState.getInt(KEY_MOVIE_ID);
            reviewItemList = savedInstanceState.getParcelableArrayList(KEY_REVIEW_LIST);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_REVIEW_LIST, reviewItemList);
        outState.putInt(KEY_MOVIE_ID, movieId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_review, container, false);

        refreshImage = (ImageView) v.findViewById(R.id.refresh);
        refreshImage.setOnClickListener(this);
        errorMessage = (TextView) v.findViewById(R.id.error_message);
        RecyclerView reviewRecyclerView = (RecyclerView) v.findViewById(R.id.rv_reviews);
        reviewRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(mlayoutManager);
        reviewAdapter = new MovieReviewAdapter(reviewItemList);
        reviewRecyclerView.setAdapter(reviewAdapter);
        loadView();

        return v;
    }

    private void loadView() {
        refreshImage.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        fetchReviews(movieId);
    }

    private void fetchReviews(int id) {
        FetchMovieReviewAsync reviewTask = new FetchMovieReviewAsync(review -> {
            if (review != null) {
                reviewItemList.clear();
                Collections.addAll(reviewItemList, review);
                reviewAdapter.notifyDataSetChanged();

            }
        },getContext());
        reviewTask.execute(id);
    }

    @Override
    public void onClick(View v) {
        Log.i(getTag(), "onClick: Review fragment");
    }
}
