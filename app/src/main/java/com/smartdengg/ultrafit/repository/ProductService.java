package com.smartdengg.ultrafit.repository;

import com.smartdengg.ultrafit.bean.response.ProductResponse;
import java.util.Map;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public interface ProductService {

  @FormUrlEncoded @POST() Observable<ProductResponse> getProductList(@Url String url,
                                                                               @FieldMap Map<String, String> params);
}
