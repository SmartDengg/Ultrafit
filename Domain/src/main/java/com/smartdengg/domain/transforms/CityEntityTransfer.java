package com.smartdengg.domain.transforms;

import com.smartdengg.domain.entity.CityEntity;
import com.smartdengg.domain.response.CityResponse;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/7/2.
 */
public class CityEntityTransfer
    implements Observable.Transformer<List<CityResponse>, List<CityEntity>> {

  private final CityEntity cityEntityInstance = new CityEntity();

  private CityEntityTransfer() {
  }

  public static CityEntityTransfer newInstance() {
    return new CityEntityTransfer();
  }

  @Override
  public Observable<List<CityEntity>> call(Observable<List<CityResponse>> listObservable) {

    return listObservable.concatMap(new Func1<List<CityResponse>, Observable<CityResponse>>() {
      @Override public Observable<CityResponse> call(List<CityResponse> cityResponses) {
        return Observable.from(cityResponses);
      }
    }).map(new Func1<CityResponse, CityEntity>() {
      @Override public CityEntity call(CityResponse cityListResponse) {

        CityEntity clone = cityEntityInstance.newInstance();

        clone.setCityId(cityListResponse.id);
        clone.setCityName(cityListResponse.cityName);

        return clone;
      }
    }).toList();
  }
}
