package com.example.common.repository.adapter.callAdapter;

import com.example.common.repository.callback.SmartCallback;
import java.io.IOException;
import okhttp3.Request;
import retrofit2.Call;

/**
 * Created by Joker on 2016/2/19.
 */
public interface SmartCall<T> extends Cloneable {

  retrofit2.Response<T> execute() throws IOException;

  void enqueue(SmartCallback<T> callback);

  void cancel();

  boolean isExecuted();

  boolean isCanceled();

  Call<T> clone();

  Request request();
}
