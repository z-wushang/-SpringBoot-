package com.kaoyan.system.common.exception;

import com.kaoyan.system.common.result.ResultCode;

public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(ResultCode rc) {
        super(rc.getMessage());
        this.code = rc.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.ERROR.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
