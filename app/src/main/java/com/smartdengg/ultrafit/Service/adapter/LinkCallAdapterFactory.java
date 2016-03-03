package com.smartdengg.ultrafit.Service.adapter;

import com.homelink.Service.utils.Types;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Created by Joker on 2016/2/19.
 */
public class LinkCallAdapterFactory extends CallAdapter.Factory {

  @Override public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

    if (Types.getRawType(returnType) != LinkCall.class) {
      return null;
    }

    if (!(returnType instanceof ParameterizedType)) {
      /*返回结果应该指定一个泛型，最起码也需要一个ResponseBody作为泛型*/
      throw new IllegalStateException("LinkCall must have generic type (e.g., LinkCall<ResponseBody>)");
    }

    final Type responseType = Types.getParameterUpperBound(0, (ParameterizedType) returnType);

    return new CallAdapter<LinkCall<?>>() {
      @Override public Type responseType() {
        return responseType;
      }

      @Override public <R> LinkCall<?> adapt(Call<R> call) {
        return new LinkCallAdapter<>(call);
      }
    };
  }
}
