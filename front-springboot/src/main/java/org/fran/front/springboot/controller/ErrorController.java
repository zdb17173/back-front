package org.fran.front.springboot.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fran on 2018/1/22.
 */
@Controller
public class ErrorController implements ErrorViewResolver {

    @Override
    public ModelAndView resolveErrorView(
            javax.servlet.http.HttpServletRequest httpServletRequest,
            HttpStatus httpStatus,
            Map<String, Object> map) {

        return new ModelAndView("/error", map);
    }
}