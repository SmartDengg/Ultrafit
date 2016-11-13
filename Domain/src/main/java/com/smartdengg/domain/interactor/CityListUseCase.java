package com.smartdengg.domain.interactor;

import com.smartdengg.domain.UseCase;
import com.smartdengg.domain.entity.CityEntity;
import com.smartdengg.domain.repository.CityRepository;
import com.smartdengg.domain.request.CityListRequest;
import com.smartdengg.domain.transforms.CityEntityTransfer;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class CityListUseCase extends UseCase<CityListRequest, List<CityEntity>> {

  private CityRepository mCityRepository;

  private CityListUseCase(CityRepository cityRepository,
      Observable.Transformer<List<CityEntity>, List<CityEntity>> transformer) {
    super(transformer);
    this.mCityRepository = cityRepository;
  }

  public static CityListUseCase create(CityRepository cityRepository,
      Observable.Transformer<List<CityEntity>, List<CityEntity>> transformer) {
    return new CityListUseCase(cityRepository, transformer);
  }

  @Override protected Observable<List<CityEntity>> interactor(@NotNull String url,
      @NotNull Map<String, String> params) {
    return mCityRepository.getCitiesResponse(url, params).compose(CityEntityTransfer.newInstance());
  }
}
