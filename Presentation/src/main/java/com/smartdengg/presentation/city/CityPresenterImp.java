package com.smartdengg.presentation.city;

import com.smartdengg.domain.UseCase;
import com.smartdengg.domain.entity.CityEntity;
import com.smartdengg.domain.errors.WebServiceException;
import com.smartdengg.domain.interactor.CityUseCase;
import com.smartdengg.domain.request.CityListRequest;
import com.smartdengg.httpservice.lib.adapter.rxadapter.rxcompat.SchedulersCompat;
import com.smartdengg.model.SimpleSubscriber;
import com.smartdengg.model.service.city.CityService;
import java.util.List;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
class CityPresenterImp implements CityContract.Presenter<List<CityEntity>> {

  private CityContract.View<List<CityEntity>> view;
  private UseCase<CityListRequest, List<CityEntity>> cityUseCase;

  @SuppressWarnings("unchecked") private static final UseCase.Executor<List<CityEntity>> executor =
      new UseCase.Executor() {
        @Override public Observable.Transformer<List<CityEntity>, List<CityEntity>> scheduler() {
          return SchedulersCompat.applyExecutorSchedulers();
        }
      };

  private CityPresenterImp() {
    this.cityUseCase = CityUseCase.create(CityService.create(), executor);
  }

  public static CityPresenterImp created() {
    return new CityPresenterImp();
  }

  @Override public void loadData() {
    this.cityUseCase.subscribe(new CityListRequest(), new Subscriber());
  }

  @Override public void attachView(CityContract.View<List<CityEntity>> view) {
    this.view = view;
  }

  @Override public void detachView() {
    this.cityUseCase.unsubscribe();
  }

  private void showContent(List<CityEntity> cityEntities) {
    this.view.showData(Observable.just(cityEntities));
  }

  private void showError(String errorMessage) {
    this.view.showError(errorMessage);
  }

  private final class Subscriber extends SimpleSubscriber<List<CityEntity>> {

    @Override public void onError(Throwable e) {
      super.onError(e);
      if (e instanceof WebServiceException) {
        CityPresenterImp.this.showError(e.getMessage());
      } else {
        CityPresenterImp.this.showError(null);
      }
    }

    @Override public void onNext(List<CityEntity> cityEntities) {
      CityPresenterImp.this.showContent(cityEntities);
    }
  }
}
