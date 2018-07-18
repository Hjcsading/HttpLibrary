package org.hjc.httplibrary.exception;

/**
 * 错误请求
 */
public class RequestException extends RuntimeException{

    public RequestException(String message) {
        super(message);
    }
}
