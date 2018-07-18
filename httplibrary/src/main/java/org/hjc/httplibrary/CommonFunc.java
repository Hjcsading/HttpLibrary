package org.hjc.httplibrary;

import org.hjc.httplibrary.exception.RequestException;
import org.hjc.httplibrary.parent.BaseWebDto;

import rx.functions.Func1;

public class CommonFunc<T> implements Func1<BaseWebDto<T>, T>{
    private static final int SUCCESS_CODE = 200
            ;

    @Override
    public T call(BaseWebDto<T> baseWebDto){
        if(baseWebDto.stateCode != SUCCESS_CODE){
            throw new RequestException(baseWebDto.msg);
        }
        return baseWebDto.data;
    }
}
