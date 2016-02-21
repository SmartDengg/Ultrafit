package com.smartdengg.ultrafit;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.smartdengg.ultrafit.bean.response.BaseResponse;
import java.lang.reflect.Type;

/**
 * Created by SmartDengg on 2016/2/14.
 */
public class Internals {

  private static final GsonBuilder gsonBuilder =
      new GsonBuilder().excludeFieldsWithoutExposeAnnotation().enableComplexMapKeySerialization().serializeNulls();

  public static SmartDeserializer JsonDeserializer() {
    return SmartDeserializer.instance;
  }

  public static GsonBuilder GsonBuilder() {
    return gsonBuilder;
  }

  static class SmartDeserializer implements JsonDeserializer<BaseResponse> {
    static final SmartDeserializer instance = new SmartDeserializer();

    @Override public BaseResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

      JsonElement results =
          json.getAsJsonObject().get("data").getAsJsonObject().get("results").getAsJsonObject().get("");

      return GsonBuilder().create().fromJson(results, typeOfT);
    }
  }
}
