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
public class CityUseCase extends UseCase<CityListRequest, List<CityEntity>> {

  private CityRepository cityRepository;

  private CityUseCase(CityRepository cityRepository,
      Executor<List<CityEntity>> transformer) {
    super(transformer);
    this.cityRepository = cityRepository;
  }

  public static CityUseCase create(CityRepository cityRepository,
      Executor<List<CityEntity>> transformer) {
    return new CityUseCase(cityRepository, transformer);
  }

  @Override protected Observable<List<CityEntity>> interactor(@NonNull String url,
      @NonNull Map<String, String> params) {
    return cityRepository.getCitiesResponse(url, params).compose(CityEntityTransfer.newInstance());
  }
}
