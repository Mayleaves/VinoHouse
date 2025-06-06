package com.VinoHouse.exception;

/**
 * 禁止删除异常
 */
public class DeletionNotAllowedException extends BaseException {

    public DeletionNotAllowedException(String msg) {
        super(msg);
    }

}
