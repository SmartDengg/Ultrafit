package com.smartdengg.model.service.city;

import com.smartdengg.model.entity.CityEntity;
import com.smartdengg.model.response.CityListResponse;
import com.smartdengg.model.response.base.ResponseS;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/7/2.
 */
public class CityEntityTransfer implements Observable.Transformer<ResponseS<CityListResponse>, List<CityEntity>> {

    private final CityEntity cityEntityInstance = new CityEntity();

    @Override
    public Observable<List<CityEntity>> call(Observable<ResponseS<CityListResponse>> responseSObservable) {

        return responseSObservable.concatMap(new Func1<ResponseS<CityListResponse>, Observable<List<CityListResponse>>>() {
            @Override
            public Observable<List<CityListResponse>> call(ResponseS<CityListResponse> responseS) {
                return responseS.filterWebServiceErrors();
            }
        }).concatMap(new Func1<List<CityListResponse>, Observable<CityListResponse>>() {
            @Override
            public Observable<CityListResponse> call(List<CityListResponse> cityListResponses) {
                return Observable.from(cityListResponses);
            }
        }).map(new Func1<CityListResponse, CityEntity>() {
            @Override
            public CityEntity call(CityListResponse cityListResponse) {

                CityEntity clone = cityEntityInstance.newInstance();

                clone.setCityId(cityListResponse.cityId);
                clone.setCityName(cityListResponse.cityName);

                return clone;
            }
        }).toList();
    }
}
