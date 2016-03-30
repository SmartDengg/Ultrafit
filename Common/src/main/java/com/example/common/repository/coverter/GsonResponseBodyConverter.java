/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.common.repository.coverter;

import com.example.common.Constants;
import com.google.gson.TypeAdapter;
import com.orhanobut.logger.Logger;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
  private final TypeAdapter<T> adapter;

  GsonResponseBodyConverter(TypeAdapter<T> adapter) {
    this.adapter = adapter;
  }

  @Override public T convert(ResponseBody value) throws IOException {
    try {

      String content = value.string();

      if (Constants.isDebugJsonLog) Logger.t(Constants.OKHTTP_TAG).json(content);

      return adapter.fromJson(ResponseBody.create(value.contentType(), content).charStream());
    } finally {
      value.close();
    }
  }
}
