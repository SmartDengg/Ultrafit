package com.smartdengg.ultrafit.Service.callback;

import java.io.IOException;
import retrofit2.Response;

/**
 * Created by Joker on 2016/2/19.
 */
public class LinkCallbackAdapter<T> implements LinkCallback<T> {

  @Override public void success(T entity) {

  }

  @Override public void unauthenticated(Response<?> response) {

  }

  @Override public void clientError(Response<?> response) {

  }

  @Override public void serverError(Response<?> response) {

  }

  @Override public void networkError(IOException e) {

  }

  @Override public void unexpectedError(Throwable t) {

  }
}
