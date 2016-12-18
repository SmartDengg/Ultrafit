package com.smartdengg.common.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 创建时间: 2016/10/28 15:00 <br>
 * 作者: dengwei <br>
 * 描述: UTF-8格式编码工具类
 */
class LibUtf8Util {

  private static final String NAME = "UTF-8";
  static final Charset INSTANCE = Charset.forName(NAME);

  private LibUtf8Util() {
    throw new AssertionError("no instance");
  }

  static byte[] encodeUTF8(String str) {
    try {
      return str.getBytes(NAME);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  static String decodeUTF8(byte[] bytes) {
    return new String(bytes, INSTANCE);
  }
}
