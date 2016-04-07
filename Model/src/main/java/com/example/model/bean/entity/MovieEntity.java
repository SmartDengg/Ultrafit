package com.example.model.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class MovieEntity implements Cloneable, Parcelable {

  private String movieThumbUrl;
  private String movieName;
  private String movieSketch;

  private String movieWriters;
  private String movieDirector;
  private String movieActor;
  private String movieCategory;
  private String movieScore;

  private String movieReleaseTime;
  private String movieCountry;

  public String getMovieThumbUrl() {
    return movieThumbUrl;
  }

  public void setMovieThumbUrl(String movieThumbUrl) {
    this.movieThumbUrl = movieThumbUrl;
  }

  public String getMovieName() {
    return movieName;
  }

  public void setMovieName(String movieName) {
    this.movieName = movieName;
  }

  public String getMovieSketch() {
    return movieSketch;
  }

  public void setMovieSketch(String movieSketch) {
    this.movieSketch = movieSketch;
  }

  public String getMovieWriters() {
    return movieWriters;
  }

  public void setMovieWriters(String movieWriters) {
    this.movieWriters = movieWriters;
  }

  public String getMovieDirector() {
    return movieDirector;
  }

  public void setMovieDirector(String movieDirector) {
    this.movieDirector = movieDirector;
  }

  public String getMovieActor() {
    return movieActor;
  }

  public void setMovieActor(String movieActor) {
    this.movieActor = movieActor;
  }

  public String getMovieCategory() {
    return movieCategory;
  }

  public void setMovieCategory(String movieCategory) {
    this.movieCategory = movieCategory;
  }

  public String getMovieScore() {
    return movieScore;
  }

  public void setMovieScore(String movieScore) {
    this.movieScore = movieScore;
  }

  public String getMovieReleaseTime() {
    return movieReleaseTime;
  }

  public void setMovieReleaseTime(String movieReleaseTime) {
    this.movieReleaseTime = movieReleaseTime;
  }

  public String getMovieCountry() {
    return movieCountry;
  }

  public void setMovieCountry(String movieCountry) {
    this.movieCountry = movieCountry;
  }

  public MovieEntity newInstance() {
    try {
      return (MovieEntity) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public int describeContents() { return 0; }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.movieThumbUrl);
    dest.writeString(this.movieName);
    dest.writeString(this.movieSketch);
    dest.writeString(this.movieWriters);
    dest.writeString(this.movieDirector);
    dest.writeString(this.movieActor);
    dest.writeString(this.movieCategory);
    dest.writeString(this.movieScore);
    dest.writeString(this.movieReleaseTime);
    dest.writeString(this.movieCountry);
  }

  public MovieEntity() {}

  protected MovieEntity(Parcel in) {
    this.movieThumbUrl = in.readString();
    this.movieName = in.readString();
    this.movieSketch = in.readString();
    this.movieWriters = in.readString();
    this.movieDirector = in.readString();
    this.movieActor = in.readString();
    this.movieCategory = in.readString();
    this.movieScore = in.readString();
    this.movieReleaseTime = in.readString();
    this.movieCountry = in.readString();
  }

  public static final Creator<MovieEntity> CREATOR = new Creator<MovieEntity>() {
    public MovieEntity createFromParcel(Parcel source) {return new MovieEntity(source);}

    public MovieEntity[] newArray(int size) {return new MovieEntity[size];}
  };
}
