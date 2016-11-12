package com.smartdengg.domain.interactor;

import android.support.annotation.NonNull;
import com.smartdengg.domain.UseCase;
import com.smartdengg.domain.entity.CityEntity;
import com.smartdengg.domain.repository.CityRepository;
import com.smartdengg.domain.request.CityListRequest;
import com.smartdengg.domain.transforms.CityEntityTransfer;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class CityListUseCase extends UseCase<CityListRequest, List<CityEntity>> {

  private CityRepository mCityRepository;

  private CityListUseCase(CityRepository cityRepository) {
    this.mCityRepository = cityRepository;
  }

  public static CityListUseCase create(CityRepository cityRepository) {
    return new CityListUseCase(cityRepository);
  }

  @Override protected Observable<List<CityEntity>> interactor(@NonNull String url,
      @NonNull Map<String, String> params) {
    return mCityRepository.getCitiesResponse(url, params).compose(CityEntityTransfer.newInstance());
  }
}
