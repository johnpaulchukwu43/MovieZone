package javadevs.moviezone;

/**
 * Created by CHUKWU JOHNPAUL on 15/04/17.
 */

public class Movie {
    private final String title;
    private final String posterPath;
    private final String overview;
    private final String voteAverage;
    private final String releaseDate;

    public Movie(String title, String posterPath, String overview, String voteAverage, String releaseDate) {
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
