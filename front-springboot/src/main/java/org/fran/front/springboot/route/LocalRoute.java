package org.fran.front.springboot.route;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class LocalRoute implements RouteLocator, Ordered {

    @Resource
    private ZuulProperties properties;
    @Resource
    private FranRouteProperties franRouteProperties;

    private PathMatcher pathMatcher = new AntPathMatcher();
    private AtomicReference<Map<String, FranRouteProperties.FranRoute>> routes = new AtomicReference<>();

    @PostConstruct
    private void init(){
    }

    @Override
    public Collection<String> getIgnoredPaths() {
        return properties.getIgnoredPatterns();
    }

    @Override
    public List<Route> getRoutes() {
        List<Route> values = new ArrayList<>();
        for (Map.Entry<String, FranRouteProperties.FranRoute> entry : getRoutesMap().entrySet()) {
            FranRouteProperties.FranRoute route = entry.getValue();
            String path = route.getPath();
            values.add(getRoute(route, path));
        }
        return values;
    }

    @Override
    public Route getMatchingRoute(String path) {
        return getSimpleMatchingRoute(path);
    }

    protected Route getSimpleMatchingRoute(final String path) {
        FranRouteProperties.FranRoute franRote = getFranRoute(path);
        return getRoute(franRote, path);
    }

    @Override
    public int getOrder() {
        return 0;
    }


    protected FranRouteProperties.FranRoute getFranRoute(String path) {
        if (!matchesIgnoredPatterns(path)) {
            for (Map.Entry<String, FranRouteProperties.FranRoute> entry : getRoutesMap().entrySet()) {
                String pattern = entry.getKey();
                if (this.pathMatcher.match(pattern, path)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    protected boolean matchesIgnoredPatterns(String path) {
        for (String pattern : this.properties.getIgnoredPatterns()) {
            if (this.pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }


    protected Map<String, FranRouteProperties.FranRoute> getRoutesMap() {
        if (this.routes.get() == null) {
            this.routes.set(locateRoutes());
        }
        return this.routes.get();
    }

    protected Map<String, FranRouteProperties.FranRoute> locateRoutes() {
        LinkedHashMap<String, FranRouteProperties.FranRoute> routesMap = new LinkedHashMap<String, FranRouteProperties.FranRoute>();
        for (FranRouteProperties.FranRoute route : this.franRouteProperties.getRoutes().values()) {
            routesMap.put(route.getPath(), route);
        }
        return routesMap;
    }

    protected Route getRoute(FranRouteProperties.FranRoute route, String path) {
        if (route == null) {
            return null;
        }
        String targetPath = path;
        String prefix = this.properties.getPrefix();
        if (path.startsWith(prefix) && this.properties.isStripPrefix()) {
            targetPath = path.substring(prefix.length());
        }
        if (route.isStripPrefix()) {
            int index = route.getPath().indexOf("*") - 1;
            if (index > 0) {
                String routePrefix = route.getPath().substring(0, index);
                targetPath = targetPath.replaceFirst(routePrefix, "");
                prefix = prefix + routePrefix;
            }
        }
        Boolean retryable = this.properties.getRetryable();
        if (route.getRetryable() != null) {
            retryable = route.getRetryable();
        }


        return new Route(
                route.getId(),
                targetPath,
                route.getLocation(),
                prefix,
                retryable,
                route.isCustomSensitiveHeaders() ? route.getSensitiveHeaders() : null,
                route.isStripPrefix());
    }
}
