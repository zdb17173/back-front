package org.fran.front.springboot.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.fran.front.springboot.security.FreemarkerUtil;
import org.fran.front.springboot.vo.JsonResult;
import org.fran.front.springboot.vo.UserVo;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by fran on 2018/1/22.
 */
@Controller
@RequestMapping("/local")
public class FreeMarkerController {

    @ResponseBody
    @GetMapping(value = "/selectList", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public JsonResult<List<String>> selectTest(@RequestParam(name="id") int ids){
        if(ids == 5)
            throw new RuntimeException("error!!!!");

        JsonResult<List<String>> res = new JsonResult<>();
        res.setData(new ArrayList<String>(){
            {
                add("sadsda1");
                add("sadsda2");
                add("sadsda3");
                add("sadsda4");
                add("sadsda5");
            }
        });
        res.setDescription("sahdjhsajdhjsahdjsajdjh");
        res.setStatus(200);
        return res;
    }

    @RequestMapping("/{template}")
    public ModelAndView selectTest(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable(name = "template") String template,
            @RequestParam(name="id", required = false) Integer ids){



        UserVo user = new UserVo();
        user.setAge(1);
        user.setName("dsa");
        user.setTag(new ArrayList<String>(){
            {
                add("1:dsa");
                add("2:23ads");
                add("3:dsdsa");
            }
        });

        Map<String, Object> map = new HashMap<>();
        map.put("name", "dsadsad");
        map.put("user", user);
        map.put("security", new FreemarkerUtil());
        return new ModelAndView("/" + template, map);
    }
}
