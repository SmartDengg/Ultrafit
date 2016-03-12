package com.example.ultrafit.presenter;

import com.example.domain.CityListUseCase;
import com.example.domain.UseCase;
import com.example.model.bean.entity.CityEntity;
import com.example.model.bean.request.CityListRequest;
import com.example.ultrafit.views.ListView;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class CityListPresenterImp implements CityListPresenter<CityEntity> {

  private ListView listView;
  private UseCase<CityListRequest, List<CityEntity>> listUseCase;

  private CityListPresenterImp() {
    this.listUseCase = CityListUseCase.<CityListRequest>createdUseCase();
  }

  public static CityListPresenterImp createdPresenter() {
    return new CityListPresenterImp();
  }

  @Override public void attachView(ListView<CityEntity> view) {
    this.listView = view;
  }

  @Override public void loadData() {
    this.listUseCase.subscribe(new CityListRequest(), new ListSubscriber());
  }

  @Override public void detachView() {
    this.listUseCase.unsubscribe();
  }

  @SuppressWarnings("unchecked") private void showContent(final List<CityEntity> cityEntities) {

    this.listView.showDataList(Observable.fromCallable(new Func0<List<CityEntity>>() {
      @Override public List call() {
        return cityEntities;
      }
    }));
  }

  private void showError(String errorMessage) {
    this.listView.showError(errorMessage);
  }

  private final class ListSubscriber extends Subscriber<List<CityEntity>> {

    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable e) {
      CityListPresenterImp.this.showError(e.getMessage());
    }

    @Override public void onNext(List<CityEntity> cityEntities) {
      CityListPresenterImp.this.showContent(cityEntities);
    }
  }
}
