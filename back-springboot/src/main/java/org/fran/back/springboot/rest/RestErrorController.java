package org.fran.back.springboot.rest;

import org.fran.back.springboot.vo.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常拦截器
 * Created by fran on 2018/1/22.
 */
@ControllerAdvice
public class RestErrorController {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public JsonResult handleError(
            HttpServletRequest request,
            Exception ex){
        JsonResult res = new JsonResult();
        res.setStatus(500);
        res.setDescription(ex == null ? "error" : ex.getMessage());
        return res;
    }
}
