package com.smartdengg.domain.repository;

import com.smartdengg.domain.response.CityResponse;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import rx.Observable;

/**
 * 创建时间:  2016/11/10 15:32 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public interface CityRepository {

  Observable<List<CityResponse>> getCitiesResponse(@NotNull String url,
      @NotNull Map<String, String> params);
}
