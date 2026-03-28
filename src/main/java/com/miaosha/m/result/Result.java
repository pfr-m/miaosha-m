package com.miaosha.m.result;

import lombok.Getter;

@Getter
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    // 成功时的调用
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    // 失败时的调用
    public static <T> Result<T> error(CodeMsg cm) {
        return new Result<>(cm);
    }

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg cm) {
        if(cm == null) return;
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }
}
