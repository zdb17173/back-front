package org.fran.front;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.monitoring.MonitoringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.filters.FilterRegistry;

import java.io.IOException;
import java.io.InputStream;

public class StartServer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(StartServer.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("starting server");

        MonitoringHelper.initMocks();

        final FilterRegistry r = FilterRegistry.instance();

        r.put("javaPreFilter", new ZuulFilter() {
            @Override
            public int filterOrder() {
                return 50000;
            }

            @Override
            public String filterType() {
                return "pre";
            }

            @Override
            public boolean shouldFilter() {
                return true;
            }

            @Override
            public Object run() {

                HttpServletRequest req = RequestContext.getCurrentContext().getRequest();
                String pathInfo = req.getPathInfo();
                req.getMethod();
                /*int index = route.getPath().indexOf("*") - 1;
                if (index > 0) {
                    String routePrefix = route.getPath().substring(0, index);
                    targetPath = targetPath.replaceFirst(routePrefix, "");
                    prefix = prefix + routePrefix;
                }*/

                logger.debug("running javaPreFilter");
                RequestContext.getCurrentContext().set("javaPreFilter-ran", true);
                return null;
            }
        });

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("stopping server");
    }




}