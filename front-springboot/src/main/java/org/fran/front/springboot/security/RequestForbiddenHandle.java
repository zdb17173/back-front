package org.fran.front.springboot.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fran.front.springboot.vo.JsonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fran on 2018/2/1.
 */
public class RequestForbiddenHandle extends LoginUrlAuthenticationEntryPoint {

    static ObjectMapper mapper = new ObjectMapper();
    static String errorMsg = null;
    static {
        JsonResult resultMessage = new JsonResult();
        resultMessage.setStatus(403);
        resultMessage.setDescription("need login!");
        try {
            errorMsg = mapper.writeValueAsString(resultMessage);
        } catch (JsonProcessingException e) {
        }

    }

    public RequestForbiddenHandle(String loginFormUrl) {
        super(loginFormUrl);
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
                || "XMLHttpRequest".equals(request.getHeader("x-requested-with"))
                || "true".equalsIgnoreCase(request.getParameter("isAjaxRequest"))){
            response.setCharacterEncoding("UTF-8");

            response.getWriter().print(errorMsg);
        } else{
            super.commence(request, response, authException);
        }
    }
}
