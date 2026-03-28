package com.miaosha.m.exception;

import com.miaosha.m.result.CodeMsg;
import lombok.Getter;

/**
 * 自定义全局异常，方便在 Service 层直接抛出业务错误
 */
@Getter
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public void setCodeMsg(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.codeMsg = cm;
    }
}
