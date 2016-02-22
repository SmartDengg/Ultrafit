package com.example.model.bean.entity;

import java.io.Serializable;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class MovieEntity implements Cloneable, Serializable {

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
}
