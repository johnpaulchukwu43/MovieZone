package javadevs.moviezone.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by CHUKWU JOHNPAUL on 13/05/17.
 */

@Data
@RequiredArgsConstructor
public class Review implements Parcelable {

    private final String reviewId;
    private final String author;
    private final String content;


    public Review(Parcel in) {
        reviewId = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reviewId);
        parcel.writeString(author);
        parcel.writeString(content);
    }
}
