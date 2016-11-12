package com.smartdengg.domain.repository;

import android.support.annotation.NonNull;
import com.smartdengg.domain.response.CityResponse;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * 创建时间:  2016/11/10 15:32 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public interface CityRepository {

  Observable<List<CityResponse>> getCitiesResponse(@NonNull String url,
      @NonNull Map<String, String> params);
}
