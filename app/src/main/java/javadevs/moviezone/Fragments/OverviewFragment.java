package javadevs.moviezone.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javadevs.moviezone.DetailActivity;
import javadevs.moviezone.R;

/**
 * Created by CHUKWU JOHNPAUL on 13/05/17.
 */

public class OverviewFragment extends Fragment implements View.OnClickListener {
    private static final String OVERVIEW_LIST = "Overview";
    private String movie_overview;
    TextView overviewtxt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            movie_overview = DetailActivity.movie_overview;
        }
        else {
            movie_overview = savedInstanceState.getString(OVERVIEW_LIST);
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(OVERVIEW_LIST,movie_overview);
    }


    @Override
    public void onClick(View v) {

    }


    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_overview, container, false);
        overviewtxt = (TextView) mView.findViewById(R.id.tv_movie_overview);

        fetchMovieOverview();
        return mView;
    }

    private void fetchMovieOverview() {
        overviewtxt.setText(movie_overview);

    }
}
