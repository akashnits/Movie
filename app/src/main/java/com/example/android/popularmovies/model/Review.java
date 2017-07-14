package com.example.android.popularmovies.model;



public class Review {
    private String mUsername;
    private String mReview;

    public Review() {
    }

    public Review(String mUsername, String mReview) {
        this.mUsername = mUsername;
        this.mReview = mReview;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String mReview) {
        this.mReview = mReview;
    }
}
