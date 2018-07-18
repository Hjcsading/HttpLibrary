package org.hjc.httplibrary;

import com.socks.library.KLog;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.hjc.httplibrary.parent.BaseRequest;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpManager {

    private static HttpManager mManager;

    private HttpManager() {
    }

    public static HttpManager getInstance(){
        if(mManager == null){
            synchronized (HttpManager.class){
                if(mManager == null){
                    mManager = new HttpManager();
                }
            }
        }
        return mManager;
    }

    public void request(BaseRequest request){
        //OkHttpClient对象构建，设置超时时间， 配置连接池、日志
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(request.mParamsUtil.mTimeout, TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(request.mParamsUtil.mMaxConnectionNum, request.mParamsUtil.mConnectionRetain, TimeUnit.MILLISECONDS));
        if(ParamsUtil.IS_DEBUG){
            client.addInterceptor(getHttpLoggingInterceptor());
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(client.build())
                .baseUrl(request.mParamsUtil.mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Observable observable = request.call(retrofit)
                .retryWhen(new RetryFunc(request.mParamsUtil.mCount, request.mParamsUtil.mDelay, request.mParamsUtil.mIncreaseDelay))
                .compose(request.getActivity().bindUntilEvent(ActivityEvent.STOP))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(request.mFunc1);
        observable.subscribe(new CommonSubscriber(request.mHttpCallbackSoftReference, request.mActivitySoftReference));
    }

    /**
     * 日志输出
     * 自行判定是否添加
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor(){
        //日志显示级别
        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                KLog.d("Retrofit====Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
