package com.smartdengg.ultrafit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.smartdengg.ultrafit.Ultrafit.Utils;
import com.smartdengg.ultrafit.Ultrafit.annotation.RestMethod;
import com.smartdengg.ultrafit.Ultrafit.type.RestType;
import com.smartdengg.ultrafit.bean.request.RequestEntity;
import java.lang.annotation.Annotation;

public class MainActivity extends AppCompatActivity {

  private RestType restType;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    RequestEntity requestEntity = new RequestEntity();

    MainActivity.this.parseUrl(requestEntity);
  }

  private void parseUrl(RequestEntity requestEntity) {

    Class<? extends RequestEntity> clazz = requestEntity.getClass();
    Annotation[] annotations = clazz.getAnnotations();

    for (Annotation classAnnotation : annotations) {

      Class<? extends Annotation> annotationType = classAnnotation.annotationType();
      RestMethod restMethod = null;

      for (Annotation innerAnnotation : annotationType.getAnnotations()) {
        if (innerAnnotation instanceof RestMethod) {
          restMethod = (RestMethod) innerAnnotation;
          break;
        }
      }

      if (restMethod != null) {
        if (restType != null) {
          throw Utils.methodError(clazz,
                                  "Only one HTTP method is allowed.Found: %s and %s.You should choose one from these.",
                                  restType.name(), restMethod.type());
        }

        String url;
        try {
          url = (String) annotationType.getMethod("stringUrl").invoke(classAnnotation);
        } catch (Exception ignored) {
          throw Utils.methodError(clazz, "Failed to extract String 'value' from @%s annotation.",
                                  annotationType.getSimpleName());
        }
        restType = restMethod.type();
        System.out.println("restType = " + restType.name());
        System.out.println("url = " + url);
      }
    }

    if (restType == null) {
      throw Utils.methodError(clazz, "HTTP method annotation is required (e.g., @GET, @POST, etc.).");
    }
  }
}
