package org.fran.front;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fran on 2018/1/16.
 */
public class FranRoutingFilter extends ZuulFilter{

    private static final Logger logger = LoggerFactory.getLogger(StartServer.class);


    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        if (request.getContentLength() < 0) {
            context.setChunkedRequestBody();
        }

        String uri = buildZuulRequestURI(request);

        return null;
    }


    protected InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = (InputStream) RequestContext.getCurrentContext().get(FilterConstants.REQUEST_ENTITY_KEY);
            if (requestEntity == null) {
                requestEntity = request.getInputStream();
            }
        }
        catch (IOException ex) {
            logger.error("error during getRequestBody", ex);
        }
        return requestEntity;
    }

    private String getVerb(HttpServletRequest request) {
        String sMethod = request.getMethod();
        return sMethod.toUpperCase();
    }

    public String buildZuulRequestURI(HttpServletRequest request) {
        RequestContext context = RequestContext.getCurrentContext();
        String uri = request.getRequestURI();
        String contextURI = (String) context.get("requestURI");
        if (contextURI != null) {
            try {
                uri = UriUtils.encodePath(contextURI, characterEncoding(request));
            }
            catch (Exception e) {
                logger.debug(
                        "unable to encode uri path from context, falling back to uri from request",
                        e);
            }
        }
        return uri;
    }


    private String characterEncoding(HttpServletRequest request) {
        return request.getCharacterEncoding() != null ? request.getCharacterEncoding()
                : WebUtils.DEFAULT_CHARACTER_ENCODING;
    }

}
