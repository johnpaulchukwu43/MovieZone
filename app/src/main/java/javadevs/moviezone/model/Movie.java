package javadevs.moviezone.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by CHUKWU JOHNPAUL on 15/04/17.
 */
@Data
@RequiredArgsConstructor
public class Movie implements Parcelable {
    private final int id;
    private final String title;
    private final String posterPath;
    private final String overview;
    private final Double voteAverage;
    private final String releaseDate;
    private final String originalLanguage;
    private final String backDropPath;
    private Boolean adult;
    private String originalTitle;
    private ArrayList<Integer> genreIds = null;
    private Double popularity;
    private int voteCount;
    private int favorite;
    private Boolean video;
    private String genreIdStrings = null;


    private Movie(Parcel in) {
        this.title = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.releaseDate = in.readString();
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.backDropPath = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.favorite = (Integer) in.readValue(Integer.class.getClassLoader());
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.genreIdStrings = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeList(this.genreIds);
        dest.writeValue(this.id);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.title);
        dest.writeString(this.backDropPath);
        dest.writeValue(this.popularity);
        dest.writeValue(this.voteCount);
        dest.writeValue(this.favorite);
        dest.writeValue(this.video);
        dest.writeString(this.genreIdStrings);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

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
