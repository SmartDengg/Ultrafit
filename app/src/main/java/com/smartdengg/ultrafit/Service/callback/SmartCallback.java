package com.smartdengg.ultrafit.service.callback;

import java.io.IOException;
import retrofit2.Response;

/**
 * Created by Joker on 2016/2/19.
 */
public interface SmartCallback<T> {

  /** Called for [200, 300) responses. But not include 204 or 205 */
  void success(T entity);

  /** Called for 204 and 205 */
  void noContent(Response<?> response);

  /** Called for 401 responses. */
  void unauthenticated(Response<?> response);

  /** Called for [400, 500) responses, except 401. */
  void clientError(Response<?> response);

  /** Called for [500, 600) response. */
  void serverError(Response<?> response);

  /** Called for network errors while making the call. */
  void networkError(IOException e);

  /** Called for unexpected errors while making the call. */
  void unexpectedError(Throwable t);
}
