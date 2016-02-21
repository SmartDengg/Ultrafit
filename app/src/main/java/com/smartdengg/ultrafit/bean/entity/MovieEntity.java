package com.smartdengg.ultrafit.bean.entity;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class MovieEntity implements Cloneable {

  private String movieName;
  private String movieDescription;
  private String movieThumbUrl;

  private String movieDirector;
  private String movieActor;
  private String movieCategory;

  public MovieEntity newInstance() {
    try {
      return (MovieEntity) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getMovieName() {
    return movieName;
  }

  public void setMovieName(String movieName) {
    this.movieName = movieName;
  }

  public String getMovieDescription() {
    return movieDescription;
  }

  public void setMovieDescription(String movieDescription) {
    this.movieDescription = movieDescription;
  }

  public String getMovieThumbUrl() {
    return movieThumbUrl;
  }

  public void setMovieThumbUrl(String movieThumbUrl) {
    this.movieThumbUrl = movieThumbUrl;
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
}
