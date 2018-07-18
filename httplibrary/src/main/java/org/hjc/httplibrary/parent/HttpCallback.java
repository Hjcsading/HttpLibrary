package org.hjc.httplibrary.parent;

public interface HttpCallback<T> {

    void onNext(T t);

    void onError(String e);

}
