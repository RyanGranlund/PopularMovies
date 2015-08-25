package com.digitalinfomesh.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ryangranlund on 8/24/15.
 */

public class Movie implements Parcelable {

    private String mSPosterPath;
    private String mSTitle;
    private String mSRelease;
    private String mSRating;
    private String mSPlot;

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mSPosterPath);
        parcel.writeString(mSTitle);
        parcel.writeString(mSRelease);
        parcel.writeString(mSRating);
        parcel.writeString(mSPlot);
    }

    public Movie(String sPosterPath, String sTitle, String sRelease, String sRating, String sPlot) {
        this.mSPosterPath = sPosterPath;
        this.mSTitle = sTitle;
        this.mSRelease = sRelease;
        this.mSRating = sRating;
        this.mSPlot = sPlot;
    }

    private Movie(Parcel in) {
        this.mSPosterPath = in.readString();
        this.mSTitle = in.readString();
        this.mSRelease = in.readString();
        this.mSRating = in.readString();
        this.mSPlot = in.readString();
    }

    //poster path methods
    public String getPosterPath() {
        return mSPosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.mSPosterPath = posterPath;
    }

    //title methods
    public String getTitle() {
        return mSTitle;
    }

    public void setTitle(String title) {
        this.mSTitle = title;
    }

    //release methods
    public String getRelease() {
        return mSRelease;
    }

    public void setRelease(String release) {
        this.mSRelease = release;
    }

    //rating methods
    public String getRating() {
        return mSRating;
    }

    public void setRating(String rating) {
        this.mSRating = rating;
    }

    //plot methods
    public String getPlot() {
        return mSPlot;
    }

    public void setPlot(String plot) {
        this.mSPlot = plot;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}