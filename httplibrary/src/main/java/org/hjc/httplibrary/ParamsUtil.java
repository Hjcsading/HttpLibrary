package org.hjc.httplibrary;

import android.app.Application;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public class ParamsUtil {
    public static final boolean IS_DEBUG = true;
    public static Application APPLICATION;

    public String mBaseUrl = "";   //基础url 需以"/"结尾
    public short mTimeout = 3 * 1000;  //超时时间，默认3秒
    public int mMaxConnectionNum = 5;  //连接池的最大连接数，默认5个
    public int mConnectionRetain = 5 * 1000;   //连接池中的连接保留时间 ,默认5秒
    public int mCount = 3;  //retry次数
    public long mDelay = 3000;  //延迟
    public long mIncreaseDelay = 3000;  //叠加延迟
    public boolean mIfCache = false;    //是否使用网络缓存
    public boolean mIfShowProgress; //是否显示加载框

    public ParamsUtil(RxAppCompatActivity activity){
        if(APPLICATION == null && activity!= null){
            APPLICATION = activity.getApplication();
        }
    }
}
