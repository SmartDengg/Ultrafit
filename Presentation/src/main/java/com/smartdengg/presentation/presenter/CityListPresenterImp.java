package com.smartdengg.presentation.presenter;

import com.smartdengg.domain.UseCase;
import com.smartdengg.domain.entity.CityEntity;
import com.smartdengg.domain.interactor.CityListUseCase;
import com.smartdengg.domain.request.CityListRequest;
import com.smartdengg.model.SimpleSubscriber;
import com.smartdengg.domain.errors.WebServiceException;
import com.smartdengg.model.service.city.CityService;
import com.smartdengg.presentation.views.ViewInterface;
import java.util.List;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class CityListPresenterImp implements CityListPresenter<List<CityEntity>> {

  private ViewInterface<List<CityEntity>> viewInterface;
  private UseCase<CityListRequest, List<CityEntity>> listUseCase;

  private CityListPresenterImp() {
    this.listUseCase = CityListUseCase.create(CityService.create());
  }

  public static CityListPresenterImp created() {
    return new CityListPresenterImp();
  }

  @Override public void attachView(ViewInterface<List<CityEntity>> view) {
    this.viewInterface = view;
  }

  @Override public void loadData() {
    this.listUseCase.subscribe(new CityListRequest(), new ListSubscriber());
  }

  @Override public void detachView() {
    this.listUseCase.unsubscribe();
  }

  private void showContent(final List<CityEntity> cityEntities) {
    this.viewInterface.showData(Observable.just(cityEntities));
  }

  private void showError(String errorMessage) {
    this.viewInterface.showError(errorMessage);
  }

  private final class ListSubscriber extends SimpleSubscriber<List<CityEntity>> {

    @Override public void onError(Throwable e) {
      super.onError(e);
      if (e instanceof WebServiceException) {
        CityListPresenterImp.this.showError(e.getMessage());
      } else {
        CityListPresenterImp.this.showError(null);
      }
    }

    @Override public void onNext(List<CityEntity> cityEntities) {
      CityListPresenterImp.this.showContent(cityEntities);
    }
  }
}
