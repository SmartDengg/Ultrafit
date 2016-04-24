package com.smartdengg.model.request;

import com.smartdengg.common.Constants;
import com.smartdengg.ultra.annotation.Argument;
import com.smartdengg.ultra.annotation.HttpGet;

/**
 * Created by SmartDengg on 2016/2/22.
 */
@HttpGet(stringUrl = Constants.MOVIE_DETAIL_URL)
public class MovieDetailRequest extends BaseRequest {

    @Argument(parameter = "movieid") private String movieId;

    public MovieDetailRequest(String movieId) {
        this.movieId = movieId;
    }
}
