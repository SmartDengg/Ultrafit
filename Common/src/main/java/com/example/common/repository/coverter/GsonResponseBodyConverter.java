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
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
  private final TypeAdapter<T> adapter;

  private static final Charset UTF8 = Charset.forName("UTF-8");

  GsonResponseBodyConverter(TypeAdapter<T> adapter) {
    this.adapter = adapter;
  }

  @Override
  public T convert(ResponseBody value) throws IOException {

    BufferedSource source = null;
    InputStreamReader reader = null;

    try {
      source = value.source();
      source.request(Long.MAX_VALUE);
      Buffer buffer = source.buffer();

      Charset charset = UTF8;
      MediaType contentType = value.contentType();
      if (contentType != null) {
        charset = contentType.charset(UTF8);
      }

      if (value.contentLength() != 0) {
        if (Constants.isDebugJsonLog) Logger.t(Constants.OKHTTP_TAG, 0).json(buffer.clone().readString(charset));
      }

      /**①*/
      /*String content = value.string();
      if (Constants.isDebugJsonLog) Logger.t(Constants.OKHTTP_TAG, 0).json(content);
      ResponseBody responseBody = ResponseBody.create(value.contentType(), content);
      return adapter.fromJson(responseBody.charStream());*/
      /**②*/
      /*return adapter.fromJson(ResponseBody.create(contentType,value.contentLength(),source).charStream());*/

      /**③ Only require Reader,ResponseBody is unnecessary,so i choose this approach :)
       * 因为我并不特别了解OKio，所以我相信以上三种写法或许都不够表现完美，你唯一需要注意的是，不要忘记关闭Closeable对象*/
      reader = new InputStreamReader(Okio.buffer(source).inputStream(), charset);
      return adapter.fromJson(reader);
    } finally {
      Util.closeQuietly(value);
      Util.closeQuietly(source);
      Util.closeQuietly(reader);
    }
  }
}
