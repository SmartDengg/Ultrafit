package com.smartdengg.ultrafit.bean.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class MovieResponse extends BaseResponse {

  /* "movies": [
      {
        "late": false,
        "showInfo": "今天132家影院放映1857场",
        "cnms": 0,
        "sn": 0,
        "imax": false,
        "snum": 1345604,
        "scm": "美人随风去，宁泡一条鱼",
        "nm": "美人鱼",
        "sc": 9.1,
        "ver": "3D/中国巨幕",
        "rt": "2016-02-08上映",
        "img": "http://p0.meituan.net/165.220/movie/3fb88bd1248e1aa85712047aa34ab1d0587282.jpg",
        "dur": 93,
        "pn": 17,
        "preSale": 0,
        "vd": "",
        "dir": "周星驰",
        "star": "邓超,罗志祥,张雨绮",
        "cat": "爱情,科幻,喜剧",
        "wish": 593641,
        "3d": true,
        "showDate": "",
        "src": "",
        "id": 246063,
        "time": ""
      }*/

  @Expose @SerializedName("data") private Data data;

  public Data getData() {
    return data;
  }

  public class Data {

    @Expose @SerializedName("hasNext") public boolean isEndless;

    @Expose @SerializedName("movies") private List<Movie> movieList;

    public List<Movie> getMovieList() {
      return movieList;
    }

    public class Movie {

      @Expose @SerializedName("nm") public String movieName;
      @Expose @SerializedName("scm") public String movieDescription;
      @Expose @SerializedName("img") public String movieThumbUrl;

      @Expose @SerializedName("dir") public String movieDirector;
      @Expose @SerializedName("star") public String movieActor;
      @Expose @SerializedName("cat") public String movieCategory;

      @Override public String toString() {
        return "Movie{" +
            "movieName='" + movieName + '\'' +
            ", movieDescription='" + movieDescription + '\'' +
            ", movieThumbUrl='" + movieThumbUrl + '\'' +
            ", movieDirector='" + movieDirector + '\'' +
            ", movieActor='" + movieActor + '\'' +
            ", movieCategory='" + movieCategory + '\'' +
            '}';
      }
    }

    @Override public String toString() {
      return "Data{" +
          "isEndless=" + isEndless +
          ", movieList=" + movieList +
          '}';
    }
  }

  @Override public String toString() {
    return "MovieResponse{" +
        "data=" + data +
        '}';
  }
}
