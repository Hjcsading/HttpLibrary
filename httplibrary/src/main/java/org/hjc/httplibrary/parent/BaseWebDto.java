package org.hjc.httplibrary.parent;

public class BaseWebDto<T> {

    /**
     * 错误码
     */
    public int stateCode;
    /**
     * 错误信息
     */
    public String msg;
    /**
     * 其它数据
     */
    public T data;

}
