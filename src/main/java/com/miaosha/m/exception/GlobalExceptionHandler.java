package com.miaosha.m.exception;

import com.miaosha.m.result.CodeMsg;
import com.miaosha.m.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {



    @ExceptionHandler(value = Exception.class) // 拦截所有异常
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace(); // 方便后台控制台调试

        if (e instanceof GlobalException) {
            // 如果是我们自己抛出的业务异常
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCodeMsg());

        } else if (e instanceof BindException) {
            // 如果是 JSR303 参数校验异常
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));

        } else {
            // 其他未知的服务器异常
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
