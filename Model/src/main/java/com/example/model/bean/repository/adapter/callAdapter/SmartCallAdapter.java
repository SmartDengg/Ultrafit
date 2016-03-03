package com.example.model.bean.repository.adapter.callAdapter;

import com.example.common.ExecutorsManager;
import com.example.model.bean.repository.callback.SmartCallback;
import java.io.IOException;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Joker on 2016/2/19.
 * 自定义CallAdapter，过滤结果与异常，根据状态码增加部分回调接口，提高系统弹性.
 */
public class SmartCallAdapter<T> implements SmartCall<T> {

  private static int CODE_200 = 200;
  public static int CODE_204 = 204;
  public static int CODE_205 = 205;
  private static int CODE_300 = 300;
  private static int CODE_400 = 400;
  private static int CODE_401 = 401;
  private static int CODE_500 = 500;
  private static int CODE_600 = 600;

  private Call<T> delegate;
  private ExecutorsManager.MainThreadExecutor callbackExecutor;

  public SmartCallAdapter(Call<T> delegate, ExecutorsManager.MainThreadExecutor mainThreadExecutor) {
    this.delegate = delegate;
    this.callbackExecutor = mainThreadExecutor;
  }

  @Override public Response<T> execute() throws IOException {
    return delegate.execute();
  }

  @Override public void enqueue(final SmartCallback<T> callback) {

    delegate.enqueue(new Callback<T>() {
      @Override public void onResponse(Call<T> call, final Response<T> response) {

        callbackExecutor.execute(new Runnable() {
          @Override public void run() {
            int code = response.code();
            if (code >= CODE_200 && code < CODE_300) {
              T body = response.body();
              if (code == CODE_204 || code == CODE_205 || body == null) {
                callback.noContent(response);
              } else {
                callback.success(response.body());
              }
            } else if (code == CODE_401) {
              callback.unauthenticated(response);
            } else if (code >= CODE_400 && code < CODE_500) {
              callback.clientError(response);
            } else if (code >= CODE_500 && code < CODE_600) {
              callback.serverError(response);
            } else {
              callback.unexpectedError(new RuntimeException("Unexpected response " + response));
            }
          }
        });
      }

      @Override public void onFailure(Call<T> call, final Throwable t) {
        callbackExecutor.execute(new Runnable() {
          @Override public void run() {
            if (t instanceof IOException) {
              callback.networkError((IOException) t);
            } else {
              callback.unexpectedError(t);
            }
          }
        });
      }
    });
  }

  @Override public void cancel() {
    delegate.cancel();
  }

  @Override public boolean isExecuted() {
    return delegate.isExecuted();
  }

  @Override public boolean isCanceled() {
    return delegate.isCanceled();
  }

  @Override public Call<T> clone() {
    return delegate.clone();
  }

  @Override public Request request() {
    return delegate.request();
  }
}
