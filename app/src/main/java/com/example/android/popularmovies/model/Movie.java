package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;



public class Movie implements Parcelable {
    private String mImageUrl;
    private String mDate;
    private String mRatings;
    private String mMovieTitle;
    private String mReview;
    private int mMovieId;

    public Movie(String mImageUrl, String mDate, String mRatings, String mMovieTitle, String mReview, int mMovieId) {
        this.mImageUrl = mImageUrl;
        this.mDate = mDate;
        this.mRatings = mRatings;
        this.mMovieTitle = mMovieTitle;
        this.mReview = mReview;
        this.mMovieId= mMovieId;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmDate() {
        return mDate;
    }


    public String getmRatings() {
        return mRatings;
    }

    public String getmMovieTitle() {
        return mMovieTitle;
    }

    public String getmReview() {
        return mReview;
    }

    public int getmMovieId() {
        return mMovieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageUrl);
        dest.writeString(this.mDate);
        dest.writeString(this.mRatings);
        dest.writeString(this.mMovieTitle);
        dest.writeString(this.mReview);
        dest.writeInt(this.mMovieId);
    }

    protected Movie(Parcel in) {
        this.mImageUrl = in.readString();
        this.mDate = in.readString();
        this.mRatings = in.readString();
        this.mMovieTitle = in.readString();
        this.mReview = in.readString();
        this.mMovieId = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
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
