package org.fran.back.springboot.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fran.back.springboot.security.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by fran on 2018/1/31.
 */
public class LoginUserFilter implements Filter{

    static ObjectMapper objectMapper = new ObjectMapper();
    UserService userService;

    LoginUserFilter(UserService userService){
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        String user = httpRequest.getHeader("x-auth-id");
        if(user!= null && !user.equals("")){
            try{
                UserService.CustomUser u = objectMapper.readValue(user, UserService.CustomUser.class);
                userService.setCurUser(u);
            }catch (Exception e){
                e.printStackTrace();
                userService.remove();
            }
        }else{
            userService.remove();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }



}
