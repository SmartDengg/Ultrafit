package com.smartdengg.model.repository.adapter.callAdapter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.smartdengg.common.utils.Types;
import com.smartdengg.model.repository.annotation.MaxConnect;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Created by Joker on 2016/2/19.
 */
public class SmartCallAdapterFactory extends CallAdapter.Factory {

    private MainThreadExecutor mainThreadExecutor;

    private SmartCallAdapterFactory() {
        this.mainThreadExecutor = new MainThreadExecutor();
    }

    public static SmartCallAdapterFactory create() {
        return new SmartCallAdapterFactory();
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        int maxConnect = 1;

        if (Types.getRawType(returnType) != SmartCall.class) {
            return null;
        }

        if (!(returnType instanceof ParameterizedType)) {
            /*返回结果应该指定一个泛型，最起码也需要一个ResponseBody作为泛型*/
            throw new IllegalStateException("SmartCall must have generic type (e.g., SmartCall<ResponseBody>)");
        }

        for (Annotation annotation : annotations) {
            if (!MaxConnect.class.isAssignableFrom(annotation.getClass())) continue;
            maxConnect = ((MaxConnect) annotation).count();
            if (maxConnect < 1) throw new IllegalArgumentException("@MaxConnect must not be less than 1");
        }

        final Type responseType = Types.getParameterUpperBound(0, (ParameterizedType) returnType);

        final int finalMaxConnect = maxConnect;
        return new CallAdapter<SmartCall<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public <R> SmartCall<?> adapt(Call<R> call) {
                return new SmartCallAdapter<>(call, mainThreadExecutor, finalMaxConnect);
            }
        };
    }

    public class MainThreadExecutor implements Executor {

        private Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainHandler.post(command);
        }
    }
}
