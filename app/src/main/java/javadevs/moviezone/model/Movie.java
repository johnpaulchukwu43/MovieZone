package javadevs.moviezone.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by CHUKWU JOHNPAUL on 15/04/17.
 */

public class Movie implements Parcelable {
    private  String title;
    private  String posterPath;
    private  String overview;
    private  Double voteAverage;
    private  String releaseDate;

    public Movie(int id,String title, String posterPath, String overview, Double voteAverage, String releaseDate,  String original_language,String backdrop_path) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.original_language = original_language;
        this.backdrop_path = backdrop_path;


    }

    private int id;
    private Boolean adult;
    private String original_title;
    private ArrayList<Integer> genre_ids = null;
    private  String original_language;
    private  String backdrop_path;
    private  Double popularity;
    private int voteCount;
    private int favorite;
    private Boolean video;
    private String genre_id_strings = null;


    private Movie(Parcel in){
        this.title=in.readString();
        this.posterPath=in.readString();
        this.overview=in.readString();
        this.voteAverage= (Double) in.readValue(Double.class.getClassLoader());
        this.releaseDate=in.readString();
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.genre_ids = new ArrayList<Integer>();
        in.readList(this.genre_ids, Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.original_title = in.readString();
        this.original_language = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.favorite = (Integer) in.readValue(Integer.class.getClassLoader());
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.genre_id_strings = in.readString();

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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public String getGenreIdStrings() {
        return genre_id_strings;
    }

    public void setGenreIdStrings(String genre_id_strings) {
        this.genre_id_strings = genre_id_strings;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath( String posterpath) {
        this.posterPath = posterpath;
    }
    public void setReleaseDate(String Relesse){
        this.releaseDate = Relesse;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(Double vote_average) {
        this.voteAverage = vote_average;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }
    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public ArrayList<Integer> getGenreIds() {
        return genre_ids;
    }

    public void setGenreIds(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public void setOriginalLanguage(String original_language) {
        this.original_language = original_language;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public void setBackdropPath(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Movie() {
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeValue(this.adult);
        dest.writeString(this.overview);
        dest.writeList(this.genre_ids);
        dest.writeValue(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.original_language);
        dest.writeString(this.title);
        dest.writeString(this.backdrop_path);
        dest.writeValue(this.popularity);
        dest.writeValue(this.voteCount);
        dest.writeValue(this.favorite);
        dest.writeValue(this.video);
        dest.writeString(this.genre_id_strings);
    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
