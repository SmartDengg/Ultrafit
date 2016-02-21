package com.smartdengg.ultrafit.domain;

import com.orhanobut.logger.Logger;
import com.smartdengg.ultrafit.SchedulersCompat;
import com.smartdengg.ultrafit.bean.entity.ProductEntity;
import com.smartdengg.ultrafit.bean.request.ProductRequest;
import com.smartdengg.ultrafit.bean.response.ProductResponse;
import com.smartdengg.ultrafit.repository.ProductService;
import com.smartdengg.ultrafit.service.ServiceGenerator;
import com.smartdengg.ultrafit.ultrafit.RequestEntity;
import com.smartdengg.ultrafit.ultrafit.UltraParser;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class ProductUsecase {

  private static ProductService productService;

  static {
    productService = ServiceGenerator.createService(ProductService.class);
  }

  @SuppressWarnings("unchecked")
  public static Observable<List<ProductEntity>> getProductList(ProductRequest productRequest) {

    final ProductEntity productInstance = new ProductEntity();

    return Observable.just(productRequest).concatMap(new Func1<ProductRequest, Observable<ProductResponse>>() {
      @Override public Observable<ProductResponse> call(ProductRequest productRequest) {

        RequestEntity requestEntity = UltraParser.createParser(productRequest).parseRequestEntity();
        Logger.d("Begin Request:RestType %s \n" + "URL is %s \n" + "Params is %s \n",
                 requestEntity.getRestType().name(), requestEntity.getUrl(), requestEntity.getQueryMap());

        return productService
            .getProductList(requestEntity.getUrl(), requestEntity.getQueryMap())
            .flatMap(new Func1<ProductResponse, Observable<ProductResponse>>() {
              @Override public Observable<ProductResponse> call(ProductResponse productResponse) {

                /** whether the code == 200*/
                return productResponse.filterWebServiceErrors();
              }
            });
      }
    }).concatMap(new Func1<ProductResponse, Observable<ProductResponse.Product>>() {
      @Override public Observable<ProductResponse.Product> call(ProductResponse productResponse) {
        return Observable.from(productResponse.getProducts());
      }
    }).map(new Func1<ProductResponse.Product, ProductEntity>() {
      @Override public ProductEntity call(ProductResponse.Product product) {

        ProductEntity clone = productInstance.newInstance();

        clone.setGoodThumbUrl(product.goodThumb);
        clone.setGoodPrice(product.goodPrice);
        clone.setGoodName(product.goodName);

        return clone;
      }
    }).toList().compose(SchedulersCompat.<List<ProductEntity>>applyExecutorSchedulers());
  }
}
