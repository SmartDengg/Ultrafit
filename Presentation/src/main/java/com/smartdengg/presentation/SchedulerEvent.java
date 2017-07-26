package com.smartdengg.presentation;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 创建时间:  2016/11/29 13:24 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class SchedulerEvent {

  public static void main(String[] args) throws InterruptedException {

    Data memoryData = Data.generateMemoryData();// 创建后两秒过期
    Data diskData = Data.generateDiskData();// 创建后四秒过期
    Data networkData = Data.generateNetworkData();// 网络数据

    Observable<Data> memoryObservable = Observable.just(memoryData);
    Observable<Data> diskObservable = Observable.just(diskData);
    Observable<Data> networkObservable = Observable.just(networkData);

    /*制造数据过期条件*/
    Thread.sleep(9_000);

    Observable.concat(memoryObservable, diskObservable, networkObservable)
        .first(
            new Func1<Data, Boolean>() {//如果没有任何数据则通过检查，则抛出异常 java.util.NoSuchElementException: Sequence contains no elements
              @Override public Boolean call(Data data) {
                long currentTimestamp = System.nanoTime();
                return data != null && !data.isExpiring(currentTimestamp);
              }
            })
        .subscribe(new Subscriber<Data>() {
          @Override public void onCompleted() {
            System.out.println("SchedulerEvent.onCompleted");
          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
          }

          @Override public void onNext(Data data) {
            System.out.println(data.data);
          }
        });

    Observable.just(1).publish(new Func1<Observable<Integer>, Observable<Integer>>() {
      @Override public Observable<Integer> call(Observable<Integer> integerObservable) {
        return null;
      }
    });

    for (; ; ) ;
  }

  private static class Data {

    private String data;
    private long expirationTime;

    private Data(String data, long aliveTime) {
      this.data = data;
      this.expirationTime = System.nanoTime() + TimeUnit.SECONDS.toNanos(aliveTime);
    }

    private static Data generateMemoryData() {
      return new Data("from memory", 2);
    }

    private static Data generateDiskData() {
      return new Data("from disk", 4);
    }

    private static Data generateNetworkData() {
      return new Data("from net", 1_000_000);
    }

    private boolean isExpiring(long currentTimestamp) {
      return expirationTime < currentTimestamp;
    }
  }
}
