package com.smartdengg.domain.request;

import com.smartdengg.ultra.annotation.Argument;
import com.smartdengg.ultra.annotation.Headers;
import com.smartdengg.ultra.annotation.Http;
import com.smartdengg.ultra.annotation.Url;

/**
 * Created by SmartDengg on 2016/2/24.
 */
@Headers({ "headerName:headerValue" }) @Http(url = "movie/citys/test", value = Http.Type.GET)
public class CityListRequest extends BaseRequest {

  @Argument String[] strings = { "string1", "string2" };
  @Argument int[] ints = { 1, 2 };
  @Argument Integer[] ingegers = { 11, 22 };
  @Argument Foo foo = new Foo("1");
  @Argument Foo[] foos = { new Foo("111"), new Foo("222") };
  @Argument Juice juice = new Juice();
  @Argument Juice[] juices = { new Juice(), new Juice() };

  @Url String url = "movie/citys";

  private static class Foo {

    private String index;

    Foo(String index) {
      this.index = index;
    }

    @Override public String toString() {
      return "Foo{" + "index='" + index + '\'' + '}';
    }
  }

  private static class Juice {
  }
}
