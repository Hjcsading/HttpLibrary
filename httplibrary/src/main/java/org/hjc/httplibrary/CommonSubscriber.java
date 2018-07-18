package org.hjc.httplibrary;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.socks.library.KLog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.hjc.httplibrary.exception.RequestException;
import org.hjc.httplibrary.parent.HttpCallback;

import java.lang.ref.SoftReference;
import java.net.SocketTimeoutException;

import rx.Subscriber;

public class CommonSubscriber<T> extends Subscriber<T>{

    /**
     * 缓存和activity的软引用
     */
    private SoftReference<HttpCallback<T>>  mCallbackSoftReference;
    private SoftReference<RxAppCompatActivity> mActivitySoftReference;
    /**
     * 是否显示加载中，默认是
     */
    private boolean mIfShowDialog = true;
    private static ProgressDialogFragment mFragment;

    public CommonSubscriber(SoftReference<HttpCallback<T>> callbackSoftReference, SoftReference<RxAppCompatActivity> activitySoftReference) {
        mCallbackSoftReference = callbackSoftReference;
        mActivitySoftReference = activitySoftReference;
        if(mFragment == null) {
            mFragment = new ProgressDialogFragment();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mIfShowDialog && mActivitySoftReference.get() != null && !mFragment.isResumed()){
            mFragment.show(mActivitySoftReference.get().getSupportFragmentManager(), "progress");
        }
    }

    @Override
    public void onCompleted() {
        if(mFragment != null && mFragment.isResumed()) {
            mFragment.dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(mFragment != null && mFragment.isResumed()) {
            mFragment.dismiss();
        }
        handlerError(e);
    }

    @Override
    public void onNext(T o) {
        if(o != null && mCallbackSoftReference.get() != null){
            mCallbackSoftReference.get().onNext(o);
        }
    }

    /**
     * 处理异常
     * @param e
     */
    private void handlerError(Throwable e){
        e.printStackTrace();
        if(mActivitySoftReference.get() != null) {
            if (e instanceof RequestException) {
                Toast.makeText(mActivitySoftReference.get(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }else if(e instanceof SocketTimeoutException){
                Toast.makeText(mActivitySoftReference.get(), R.string.socket_out, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mActivitySoftReference.get(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if(mCallbackSoftReference.get() != null){
            mCallbackSoftReference.get().onError(e.getMessage());
        }
    }
}
