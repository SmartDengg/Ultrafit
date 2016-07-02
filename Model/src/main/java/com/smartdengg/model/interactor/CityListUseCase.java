package com.smartdengg.model.interactor;

import android.support.annotation.NonNull;
import com.smartdengg.domain.UseCase;
import com.smartdengg.model.entity.CityEntity;
import com.smartdengg.model.request.CityListRequest;
import com.smartdengg.model.service.city.CityService;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class CityListUseCase extends UseCase<CityListRequest, List<CityEntity>> {

  private CityService cityService;

  private CityListUseCase() {
    this.cityService = CityService.createdService();
  }

  public static CityListUseCase createdUseCase() {
    return new CityListUseCase();
  }

  @Override protected Observable<List<CityEntity>> interactor(@NonNull String url,
      @NonNull Map<String, String> params) {
    return this.cityService.getCityEntities(url, params);
  }
}
