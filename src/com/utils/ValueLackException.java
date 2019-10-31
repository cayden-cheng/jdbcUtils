package com.utils;

/**
 * @author chengliang
 * @date 2019/10/31 9:19
 */
public class ValueLackException extends RuntimeException {

    public ValueLackException(){
        super();
    }

    public ValueLackException(String message){
        super(message);
    }

    public ValueLackException(String message, Throwable throwable){
        super(message,throwable);
    }

    public ValueLackException(Throwable throwable){
        super(throwable);
    }

    protected ValueLackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
