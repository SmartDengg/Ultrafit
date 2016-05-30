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
package com.smartdengg.model.repository.coverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.orhanobut.logger.Logger;
import com.smartdengg.common.Constants;
import com.smartdengg.model.BuildConfig;
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

@SuppressWarnings("all")
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Gson gson;
    private final TypeAdapter<T> adapter;
    private final boolean enable;

    private static final Charset UTF8 = Charset.forName("UTF-8");

    GsonResponseBodyConverter(Gson gson, TypeAdapter adapter, boolean enable) {
        this.gson = gson;
        this.adapter = adapter;
        this.enable = enable;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        InputStreamReader reader = null;
        BufferedSource source = null;
        JsonReader jsonReader = null;

        try {
            if (enable) {
                source = value.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = value.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (value.contentLength() != 0 && enable) {
                    if (!BuildConfig.RELEASE) {
                        Logger.t(Constants.OKHTTP_TAG, 0)
                              .json(buffer.clone()
                                          .readString(charset));
                    }
                }
                /**①*/
                /*String content = value.string();
                if (Constants.isDebugJsonLog) Logger.t(Constants.OKHTTP_TAG, 0).json(content);
                ResponseBody responseBody = ResponseBody.create(value.contentType(), content);
                return adapter.fromJson(responseBody.charStream());*/
                /**②*/
                /*return adapter.fromJson(ResponseBody.create(contentType,value.contentLength(),source).charStream());*/
                /**③ Only require Reader,ResponseBody is unnecessary,so i choose this approach :)*/
                reader = new InputStreamReader(Okio.buffer(source)
                                                   .inputStream(), charset);
                return adapter.fromJson(reader);
            } else {
                jsonReader = gson.newJsonReader(value.charStream());
                return adapter.read(jsonReader);
            }
        } finally {
            Util.closeQuietly(source);
            Util.closeQuietly(reader);
            Util.closeQuietly(jsonReader);
            Util.closeQuietly(value);
        }
    }
}
