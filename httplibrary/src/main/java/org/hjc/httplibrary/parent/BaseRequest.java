package org.hjc.httplibrary.parent;


import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.hjc.httplibrary.CommonFunc;
import org.hjc.httplibrary.ParamsUtil;

import java.lang.ref.SoftReference;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

public abstract class BaseRequest<T> {

    /**
     * 参数集
     */
    public ParamsUtil mParamsUtil;
    /**
     * 数据转换器，默认CommonFun
     */
    public Func1 mFunc1;
    public SoftReference<RxAppCompatActivity> mActivitySoftReference;
    public SoftReference<HttpCallback<T>> mHttpCallbackSoftReference;

    public BaseRequest(RxAppCompatActivity activity, HttpCallback httpCallback) {
        mActivitySoftReference = new SoftReference(activity);
        mHttpCallbackSoftReference = new SoftReference(httpCallback);
        mParamsUtil = new ParamsUtil(mActivitySoftReference.get());
        mFunc1 = new CommonFunc();
    }

    /**
     * 请求信息
     * @param retrofit
     * @return
     */
    public abstract Observable call(Retrofit retrofit);

    public RxAppCompatActivity getActivity(){
        return mActivitySoftReference.get();
    }

    public HttpCallback getHttpCallBack(){
        return mHttpCallbackSoftReference.get();
    }
}
