package org.fran.front.springboot.security;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthHeaderFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RequestContext requestContext = RequestContext.getCurrentContext();
        if(authentication.getPrincipal() instanceof String){
            requestContext.addZuulRequestHeader("X-AUTH-ID","");
        }else{
            requestContext.addZuulRequestHeader("X-AUTH-ID",authentication.getPrincipal().toString());
        }
        return null;
    }
}