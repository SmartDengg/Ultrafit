package com.smartdengg.ultra.core;

/**
 * Created by Joker on 2016/6/28.
 */
abstract class UltraHandler<T> {

  abstract void apply(RequestBuilder builder, T value);
}
