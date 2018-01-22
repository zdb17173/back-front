package org.fran.front.springboot.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.fran.front.springboot.vo.UserVo;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by fran on 2018/1/22.
 */
@Controller
@RequestMapping("/local")
public class FreeMarkerController {

    @RequestMapping("/{template}")
    public ModelAndView selectTest(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable(name = "template") String template,
            @RequestParam(name="id", required = false) Integer ids){

        System.out.println(ids);

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
        return new ModelAndView("/" + template, map);
    }
}
