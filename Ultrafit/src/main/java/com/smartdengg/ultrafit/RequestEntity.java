package com.smartdengg.ultrafit;

import com.smartdengg.ultrafit.type.RestType;
import java.util.Map;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class RequestEntity {

    private RestType restType;
    private String url;
    private Map<String, String> paramMap;

    private boolean shouldOutputs;

    public RequestEntity() {
    }

    public RequestEntity(RestType restType, String url, Map<String, String> paramMap, boolean shouldOutputs) {
        this.restType = restType;
        this.url = url;
        this.paramMap = paramMap;
        this.shouldOutputs = shouldOutputs;
    }

    public RestType getRestType() {
        return restType;
    }

    public RequestEntity setRestType( RestType restType) {
        this.restType = restType;
        return RequestEntity.this;
    }

    public String getUrl() {
        return url;
    }

    public RequestEntity setUrl( String url) {
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

    /** Be care of ConnectableObservable */
    public T  asObservable(Class<T> obserable) {


        obserable.getGenericSuperclass()

       /* return Observable.fromCallable(new Func0<RequestEntity>() {
            @Override
            public RequestEntity call() {
                return RequestEntity.this;
            }
        });*/






        return null;
    }

   /* public Single<RequestEntity> asSingle() {
        return asObservable().toSingle();
    }*/

    //@formatter:off
    @Override
    public String toString() {
        return "Request entity !!!!" +
                "\n  ⇢ " + " Type    : " + RequestEntity.this.getRestType() +
                "\n  ⇢ " + " Url     : " + RequestEntity.this.getUrl() +
                "\n  ⇢ " + " Params  : " + RequestEntity.this.getParamMap();
    }
}
