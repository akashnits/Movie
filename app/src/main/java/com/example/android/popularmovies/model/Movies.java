package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Movies implements Parcelable {

  @SerializedName("overview")
  private String overview;

  @SerializedName("original_language")
  private String originalLanguage;

  @SerializedName("original_title")
  private String originalTitle;

  @SerializedName("video")
  private boolean video;

  @SerializedName("title")
  private String title;

  @SerializedName("genre_ids")
  private List<Integer> genreIds;

  @SerializedName("poster_path")
  private String posterPath;

  @SerializedName("backdrop_path")
  private String backdropPath;

  @SerializedName("release_date")
  private String releaseDate;

  @SerializedName("vote_average")
  private double voteAverage;

  @SerializedName("popularity")
  private double popularity;

  @SerializedName("id")
  private int id;

  @SerializedName("adult")
  private boolean adult;

  @SerializedName("vote_count")
  private int voteCount;

  public void setOverview(String overview){
    this.overview = overview;
  }

  public String getOverview(){
    return overview;
  }

  public void setOriginalLanguage(String originalLanguage){
    this.originalLanguage = originalLanguage;
  }

  public String getOriginalLanguage(){
    return originalLanguage;
  }

  public void setOriginalTitle(String originalTitle){
    this.originalTitle = originalTitle;
  }

  public String getOriginalTitle(){
    return originalTitle;
  }

  public void setVideo(boolean video){
    this.video = video;
  }

  public boolean isVideo(){
    return video;
  }

  public void setTitle(String title){
    this.title = title;
  }

  public String getTitle(){
    return title;
  }

  public void setGenreIds(List<Integer> genreIds){
    this.genreIds = genreIds;
  }

  public List<Integer> getGenreIds(){
    return genreIds;
  }

  public void setPosterPath(String posterPath){
    this.posterPath = posterPath;
  }

  public String getPosterPath(){
    return posterPath;
  }

  public void setBackdropPath(String backdropPath){
    this.backdropPath = backdropPath;
  }

  public String getBackdropPath(){
    return backdropPath;
  }

  public void setReleaseDate(String releaseDate){
    this.releaseDate = releaseDate;
  }

  public String getReleaseDate(){
    return releaseDate;
  }

  public void setVoteAverage(double voteAverage){
    this.voteAverage = voteAverage;
  }

  public double getVoteAverage(){
    return voteAverage;
  }

  public void setPopularity(double popularity){
    this.popularity = popularity;
  }

  public double getPopularity(){
    return popularity;
  }

  public void setId(int id){
    this.id = id;
  }

  public int getId(){
    return id;
  }

  public void setAdult(boolean adult){
    this.adult = adult;
  }

  public boolean isAdult(){
    return adult;
  }

  public void setVoteCount(int voteCount){
    this.voteCount = voteCount;
  }

  public int getVoteCount(){
    return voteCount;
  }

  @Override
  public String toString(){
    return
        "Movies{" +
            "overview = '" + overview + '\'' +
            ",original_language = '" + originalLanguage + '\'' +
            ",original_title = '" + originalTitle + '\'' +
            ",video = '" + video + '\'' +
            ",title = '" + title + '\'' +
            ",genre_ids = '" + genreIds + '\'' +
            ",poster_path = '" + posterPath + '\'' +
            ",backdrop_path = '" + backdropPath + '\'' +
            ",release_date = '" + releaseDate + '\'' +
            ",vote_average = '" + voteAverage + '\'' +
            ",popularity = '" + popularity + '\'' +
            ",id = '" + id + '\'' +
            ",adult = '" + adult + '\'' +
            ",vote_count = '" + voteCount + '\'' +
            "}";
  }

  public Movies(int mMovieId, String mImageUrl, String mDate, String mRatings, String mMovieTitle, String mOverview) {
    this.id = mMovieId;
    this.posterPath = mImageUrl;
    this.releaseDate = mDate;
    this.voteAverage = Double.parseDouble(mRatings);
    this.title = mMovieTitle;
    this.overview = mOverview;
  }

  protected Movies(Parcel in) {
    overview = in.readString();
    originalLanguage = in.readString();
    originalTitle = in.readString();
    video = in.readByte() != 0x00;
    title = in.readString();
    if (in.readByte() == 0x01) {
      genreIds = new ArrayList<Integer>();
      in.readList(genreIds, Integer.class.getClassLoader());
    } else {
      genreIds = null;
    }
    posterPath = in.readString();
    backdropPath = in.readString();
    releaseDate = in.readString();
    voteAverage = in.readDouble();
    popularity = in.readDouble();
    id = in.readInt();
    adult = in.readByte() != 0x00;
    voteCount = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(overview);
    dest.writeString(originalLanguage);
    dest.writeString(originalTitle);
    dest.writeByte((byte) (video ? 0x01 : 0x00));
    dest.writeString(title);
    if (genreIds == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(genreIds);
    }
    dest.writeString(posterPath);
    dest.writeString(backdropPath);
    dest.writeString(releaseDate);
    dest.writeDouble(voteAverage);
    dest.writeDouble(popularity);
    dest.writeInt(id);
    dest.writeByte((byte) (adult ? 0x01 : 0x00));
    dest.writeInt(voteCount);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
    @Override
    public Movies createFromParcel(Parcel in) {
      return new Movies(in);
    }

    @Override
    public Movies[] newArray(int size) {
      return new Movies[size];
    }
  };
}