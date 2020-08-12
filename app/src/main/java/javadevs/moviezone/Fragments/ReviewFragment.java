package javadevs.moviezone.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import javadevs.moviezone.DetailActivity;
import javadevs.moviezone.Interface.ReviewCallBack;
import javadevs.moviezone.R;
import javadevs.moviezone.Util.FetchMovieReviewAsync;
import javadevs.moviezone.adapters.MovieReviewAdapter;
import javadevs.moviezone.model.Review;



/**
 * Created by CHUKWU JOHNPAUL on 13/05/17.
 */

public class ReviewFragment extends Fragment implements View.OnClickListener {
    private int movie_id;
    private static final String KEY_REVIEW_LIST = "keyReviewList";
    private static final String KEY_MOVIE_ID = "movie_id";
    ImageView refreshImage;
    TextView errorMessage;
    RecyclerView recyclerView;
    MovieReviewAdapter reviewAdapter;
    ArrayList<Review> reviewItemList ;
    public static TextView no_review_message;



    public static ReviewFragment newInstance() {
        ReviewFragment fragment = new ReviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            movie_id = DetailActivity.id;
            reviewItemList = new ArrayList<>();
        }
        else {
            movie_id = savedInstanceState.getInt(KEY_MOVIE_ID);
            reviewItemList = savedInstanceState.getParcelableArrayList(KEY_REVIEW_LIST);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_REVIEW_LIST, reviewItemList);
        outState.putInt(KEY_MOVIE_ID,movie_id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_review, container, false);

        refreshImage = (ImageView) v.findViewById(R.id.refresh);
        refreshImage.setOnClickListener(this);
        errorMessage = (TextView) v.findViewById(R.id.error_message);
        no_review_message = (TextView)v.findViewById(R.id.no_reviews);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_reviews);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mlayoutManager);
        reviewAdapter = new MovieReviewAdapter(getContext(), reviewItemList);
        recyclerView.setAdapter(reviewAdapter);
        LoadView();

        return v;
    }

    private void LoadView() {

        refreshImage.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        fetchReviews(movie_id);
    }

    private void fetchReviews(int id) {
        FetchMovieReviewAsync reviewTask = new FetchMovieReviewAsync(new ReviewCallBack() {

            @Override
            public void updateAdapter(Review[] review) {
                if (review != null) {
                    reviewItemList.clear();
                    Collections.addAll(reviewItemList, review);
                    reviewAdapter.notifyDataSetChanged();

                }
            }
        },getContext());
        reviewTask.execute(id);

    }

    @Override
    public void onClick(View v) {

    }
}
