package com.example.model.bean.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MovieDetailResponse {

  /*result": [
        {
           "movieid": "137742",
            "rating": "-1",
            "genres": "动作/冒险/科幻",
            "runtime": "null",
            "language": "英语",
            "title": "哥斯拉",
            "poster": "http://v.juhe.cn/movie/img?55370",
            "writers": "麦克斯·鲍任斯坦,达夫·卡拉汉姆",
            "film_locations": "美国|日本",
            "directors": "加里斯·爱德华斯",
            "rating_count": "288",
            "actors": "亚伦·泰勒-约翰逊 Aaron Taylor-Johnson,布莱恩·科兰斯顿 Bryan Cranston,伊丽莎白·奥尔森 Elizabeth Olsen,渡边谦 Ken Watanabe",
            "plot_simple": "改编自1954年日本电影《哥斯拉》，围绕一位人类大兵的生活展开。讲述了沉睡的古代巨型怪兽被人们意外唤醒，醒来后的怪兽展现出强大的破坏能力，它的存在也震惊了世界。影片同时强调了原子弹对生物变异带来的直接影响。",
            "year": "2014",
            "country": "美国|日本",
            "type": "null",
            "release_date": "20140101",
            "also_known_as": ""
        },
       ]*/

  @Expose
  @SerializedName("poster")
  public String movieThumbUrl;
  @Expose
  @SerializedName("title")
  public String movieName;
  @Expose
  @SerializedName("plot_simple")
  public String movieSketch;

  @Expose
  @SerializedName("writers")
  public String movieWriters;
  @Expose
  @SerializedName("directors")
  public String movieDirectors;
  @Expose
  @SerializedName("actors")
  public String movieActors;
  @Expose
  @SerializedName("genres")
  public String movieCategory;
  @Expose
  @SerializedName("rating")
  public String movieScore;

  @Expose
  @SerializedName("release_date")
  public String movieReleaseTime;
  @Expose
  @SerializedName("country")
  public String movieCountry;
}
