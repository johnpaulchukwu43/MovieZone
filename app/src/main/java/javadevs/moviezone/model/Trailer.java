package javadevs.moviezone.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by CHUKWU JOHNPAUL on 14/05/17.
 */

@Data
@RequiredArgsConstructor
public class Trailer implements Parcelable {

    private final String trailerId;
    private final String key;
    private final String name;


    public Trailer(Parcel in) {
        trailerId = in.readString();
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trailerId);
        parcel.writeString(key);
        parcel.writeString(name);
    }
}
