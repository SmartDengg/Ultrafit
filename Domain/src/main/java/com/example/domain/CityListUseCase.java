package com.example.domain;

import android.support.annotation.NonNull;
import com.example.model.bean.MovieService;
import com.example.model.bean.entity.CityEntity;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
@SuppressWarnings("unchecked")
public class CityListUseCase<R> extends UseCase<R, List<CityEntity>> {

    private MovieService movieService;

    private CityListUseCase() {
        this.movieService = MovieService.createdService();
    }

    public static <R> CityListUseCase<R> createdUseCase() {
        return (CityListUseCase<R>) new CityListUseCase();
    }

    @Override
    protected Observable<List<CityEntity>> interactor(@NonNull String url, @NonNull Map<String, String> params) {
        return movieService.getCityEntities(url, params);
    }
}
