package com.example.common.repository.adapter.callAdapter;

import com.example.common.Executors;
import com.example.common.ultrafit.type.Types;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Created by Joker on 2016/2/19.
 */
public class SmartCallAdapterFactory extends CallAdapter.Factory {

  private Executors.MainThreadExecutor mainThreadExecutor;

  private SmartCallAdapterFactory() {
    this.mainThreadExecutor = Executors.getInstance().mainThreadExecutor();
  }

  public static SmartCallAdapterFactory create() {
    return new SmartCallAdapterFactory();
  }

  @Override public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

    if (Types.getRawType(returnType) != SmartCall.class) {
      return null;
    }

    if (!(returnType instanceof ParameterizedType)) {
      /*返回结果应该指定一个泛型，最起码也需要一个ResponseBody作为泛型*/
      throw new IllegalStateException("SmartCall must have generic type (e.g., SmartCall<ResponseBody>)");
    }

    final Type responseType = Types.getParameterUpperBound(0, (ParameterizedType) returnType);

    return new CallAdapter<SmartCall<?>>() {
      @Override public Type responseType() {
        return responseType;
      }

      @Override public <R> SmartCall<?> adapt(Call<R> call) {
        return new SmartCallAdapter<>(call,mainThreadExecutor);
      }
    };
  }
}
