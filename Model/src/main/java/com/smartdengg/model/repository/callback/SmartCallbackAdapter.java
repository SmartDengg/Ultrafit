package com.smartdengg.model.repository.callback;

import com.smartdengg.common.Constants;
import com.orhanobut.logger.Logger;
import java.io.IOException;
import retrofit2.Response;

/**
 * Created by Joker on 2016/2/19.
 */
public class SmartCallbackAdapter<T> implements SmartCallback<T> {

  /**
   * Called for [200, 300) responses. But not include 204 or 205
   *
   * @param entity
   */
  @Override
  public void success(T entity) {

  }

  /**
   * Called for 204 and 205
   *
   * @param response
   */
  @Override
  public void noContent(Response<?> response) {

  }

  /**
   * Called for 401 responses.
   *
   * @param response
   */
  @Override
  public void unauthenticated(Response<?> response) {

  }

  /**
   * Called for [400, 500) responses, except 401.
   *
   * @param response
   */
  @Override
  public void clientError(Response<?> response) {

  }

  /**
   * Called for [500, 600) response.
   *
   * @param response
   */
  @Override
  public void serverError(Response<?> response) {

  }

  /**
   * Called for network errors while making the call.
   *
   * @param e
   */
  @Override
  public void networkError(IOException e) {
    Logger.t(Constants.OKHTTP_TAG).d(e.getMessage());
  }

  /**
   * Called for unexpected errors while making the call.
   *
   * @param t
   */
  @Override
  public void unexpectedError(Throwable t) {
    Logger.t(Constants.OKHTTP_TAG).d(t.getMessage());
  }
}
