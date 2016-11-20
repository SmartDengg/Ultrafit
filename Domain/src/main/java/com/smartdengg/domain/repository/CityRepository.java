package com.smartdengg.domain.repository;

import android.support.annotation.NonNull;
import com.smartdengg.domain.response.CityResponse;
import java.util.List;
import java.util.Map;
import rx.Observable;

public interface CityRepository {
  Observable<List<CityResponse>> getCitiesResponse(@NonNull String url,
      @NonNull Map<String, String> params);
}
