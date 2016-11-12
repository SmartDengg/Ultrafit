package com.smartdengg.model.service.city;

import android.support.annotation.NonNull;
import com.smartdengg.domain.repository.CityRepository;
import com.smartdengg.domain.response.CityResponse;
import com.smartdengg.domain.response.base.ResponseS;
import com.smartdengg.domain.rxcompat.SchedulersCompat;
import com.smartdengg.httpservice.lib.annotation.LogResult;
import com.smartdengg.httpservice.lib.annotation.RetryCount;
import com.smartdengg.model.injector.generator.ServiceGenerator;
import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class CityService implements CityRepository {

  private static final int RETRY_COUNT = 3;

  private final InternalService service;

  interface InternalService {

    @LogResult(enable = false) @RetryCount(count = RETRY_COUNT) @GET
    Observable<ResponseS<CityResponse>> getCityList(@Url String url,
        @QueryMap Map<String, String> params);
  }

  private CityService() {
    this.service = ServiceGenerator.createService(InternalService.class);
  }

  public static CityService create() {
    return new CityService();
  }

  public Observable<List<CityResponse>> getCitiesResponse(@NonNull String url,
      @NonNull Map<String, String> params) {

    return CityService.this.service.getCityList(url, params)
        .concatMap(new Func1<ResponseS<CityResponse>, Observable<? extends List<CityResponse>>>() {
          @Override
          public Observable<? extends List<CityResponse>> call(ResponseS<CityResponse> responseS) {
            return responseS.filterWebServiceErrors();
          }
        })
        .compose(SchedulersCompat.<List<CityResponse>>applyExecutorSchedulers());
  }
}
