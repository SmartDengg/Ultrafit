package com.smartdengg.ultra;

import com.smartdengg.ultra.annotation.Http;
import rx.Observable;
import rx.Single;
import rx.plugins.RxJavaHooks;

/**
 * 创建时间: 2017/03/23 12:13 <br>
 * 作者: dengwei <br>
 * 描述:
 */
public class UltraParser<Request> {

  private Request request;

  public static <Request> UltraParser<Request> createWith(Request request) {

    Utils.checkNotNull(request, "request == null");

    Class<?> clazz = request.getClass();
    Http http = clazz.getAnnotation(Http.class);
    if (http == null) {
      throw Utils.classError(null, clazz, "%s lack of @Http annotation", clazz.getName());
    }

    return new UltraParser<>(request);
  }

  private UltraParser(Request request) {
    this.request = request;
  }

  public Observable<RequestEntity<Request>> parseAsObservable() {
    try {
      return new RequestEntityBuilder<>(request).build().asObservable();
    } catch (Exception e) {
      RxJavaHooks.onError(e);
      return Observable.error(e);
    }
  }

  public Single<RequestEntity<Request>> parseAsSingle() {
    try {
      return new RequestEntityBuilder<>(request).build().asSingle();
    } catch (Exception e) {
      RxJavaHooks.onError(e);
      return Single.error(e);
    }
  }
}
