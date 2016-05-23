package com.smartdengg.ultra;

import com.smartdengg.ultra.annotation.RestType;
import com.smartdengg.ultra.util.RxJavaFactory;
import com.smartdengg.ultra.util.Types;
import com.smartdengg.ultra.util.Utils;
import java.util.Map;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class RequestEntity {

    private RestType restType;
    private String url;
    private Map<String, String> paramMap;

    private boolean shouldOutputs;

    RequestEntity(RestType restType, String url, Map<String, String> paramMap, boolean shouldOutputs) {
        this.restType = restType;
        this.url = url;
        this.paramMap = paramMap;
        this.shouldOutputs = shouldOutputs;
    }

    public RequestEntity() {}

    public RestType getRestType() {
        return restType;
    }

    public RequestEntity setRestType(RestType restType) {
        this.restType = restType;
        return RequestEntity.this;
    }

    public String getUrl() {
        return url;
    }

    public RequestEntity setUrl(String url) {
        this.url = url;
        return RequestEntity.this;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public RequestEntity setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
        return RequestEntity.this;
    }

    public boolean isShouldOutputs() {
        return shouldOutputs;
    }

    public RequestEntity setShouldOutputs(boolean shouldOutputs) {
        this.shouldOutputs = shouldOutputs;
        return RequestEntity.this;
    }

    /**
     * Be care of ConnectableObservable
     *
     * @param rx
     *         rx.Observable or rx.Single
     * @param <T>
     * @return
     */
    public <T> T as(Class<T> rx) {

        Utils.checkNotNull(rx, "observable == null");
        if (rx == Void.class) throw Utils.methodError(RequestEntity.class, "Observable cannot be void.");

        Class<?> rawType = Types.getRawType(rx);
        boolean isObservable = "rx.Observable".equals(rawType.getCanonicalName());
        boolean isSingle = "rx.Single".equals(rawType.getCanonicalName());

        if (!isObservable && !isSingle) return null;

        if (isObservable) {
            return RxJavaFactory.createdObservable(RequestEntity.this);
        } else {
            return RxJavaFactory.createdSingle(RequestEntity.this);
        }
    }

    @Override
    public String toString() {
        return "Request entity !!!!" +
                "\n  --> " + " Type    : " + RequestEntity.this.getRestType() +
                "\n  --> " + " Url     : " + RequestEntity.this.getUrl() +
                "\n  --> " + " Params  : " + RequestEntity.this.getParamMap();
    }
}
