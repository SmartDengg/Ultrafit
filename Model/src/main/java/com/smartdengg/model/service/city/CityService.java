package com.smartdengg.model.service.city;

import android.support.annotation.NonNull;
import com.smartdengg.httpservice.lib.annotation.LogResult;
import com.smartdengg.httpservice.lib.annotation.RetryCount;
import com.smartdengg.model.entity.CityEntity;
import com.smartdengg.model.injector.generator.ServiceGenerator;
import com.smartdengg.model.response.CityListResponse;
import com.smartdengg.model.response.base.ResponseS;
import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class CityService {

  private static final int MAX_CONNECT = 5;

  private final InternalService service;

  interface InternalService {

    @LogResult(enable = false) @RetryCount(count = MAX_CONNECT) @GET
    Observable<ResponseS<CityListResponse>> getCityList(@Url String url,
        @QueryMap Map<String, String> params);
  }

  private CityService() {
    this.service = ServiceGenerator.createService(InternalService.class);
  }

  public static CityService createdService() {
    return new CityService();
  }

  public Observable<List<CityEntity>> getCityEntities(@NonNull String url,
      @NonNull Map<String, String> params) {
    return CityService.this.service.getCityList(url, params).compose(new CityEntityTransfer());
  }
}
