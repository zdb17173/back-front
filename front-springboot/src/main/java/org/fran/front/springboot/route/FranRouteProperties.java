package org.fran.front.springboot.route;

import org.fran.front.springboot.ha.HAService;
import org.fran.front.springboot.ha.Server;
import org.fran.front.springboot.ha.keepalive.DefaultKeepAlive;
import org.fran.front.springboot.ha.loadbalancer.DefaultLoadBalancer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@Configuration
@ConfigurationProperties("fran")
public class FranRouteProperties {
    Map<String, FranRoute> routes;
    static HAService haService;
    boolean inited = false;

    public Map<String, FranRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, FranRoute> routes) {
        this.routes = routes;
    }

    @PostConstruct
    public void init() {
        if(inited){
            return;
        }else
            inited = true;

        haService = HAService.build()
                .withKeepAlive(new DefaultKeepAlive())
                .withLoadBalance(new DefaultLoadBalancer());

        for (Map.Entry<String, FranRoute> entry : this.routes.entrySet()) {
            FranRoute value = entry.getValue();
            value.key = entry.getKey();
//            if (!StringUtils.hasText(value.getLocation())) {
//                value.serviceId = entry.getKey();
//            }
            if (!StringUtils.hasText(value.getId())) {
                value.id = entry.getKey();
            }
            if (!StringUtils.hasText(value.getPath())) {
                value.path = "/" + entry.getKey() + "/**";
            }

            if(value.keepalive!= null && value.keepalive.using){
                haService.withFixedServer
                        (entry.getKey(),true, value.keepalive.path, value.keepalive.expectedContent,
                                value.url);
            }

        }
        haService.start();
    }


    public static class Keepalive{
        private boolean using = false;

        private String path;

        private String expectedContent;

        public boolean isUsing() {
            return using;
        }

        public void setUsing(boolean using) {
            this.using = using;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getExpectedContent() {
            return expectedContent;
        }

        public void setExpectedContent(String expectedContent) {
            this.expectedContent = expectedContent;
        }
    }

    public static class FranRoute{

        private String key;

        private Keepalive keepalive;

        /**
         * The ID of the route (the same as its map key by default).
         */
        private String id;

        /**
         * The path (pattern) for the route, e.g. /foo/**.
         */
        private String path;

        /**
         * The service ID (if any) to map to this route. You can specify a physical URL or
         * a service, but not both.
         */
        private String serviceId;

        /**
         * A full physical URL to map to the route. An alternative is to use a service ID
         * and service discovery to find the physical address.
         */
        private String[] url;

        /**
         * Flag to determine whether the prefix for this route (the path, minus pattern
         * patcher) should be stripped before forwarding.
         */
        private boolean stripPrefix = true;

        /**
         * Flag to indicate that this route should be retryable (if supported). Generally
         * retry requires a service ID and ribbon.
         */
        private Boolean retryable;

        /**
         * List of sensitive headers that are not passed to downstream requests. Defaults
         * to a "safe" set of headers that commonly contain user credentials. It's OK to
         * remove those from the list if the downstream service is part of the same system
         * as the proxy, so they are sharing authentication data. If using a physical URL
         * outside your own domain, then generally it would be a bad idea to leak user
         * credentials.
         */
        private Set<String> sensitiveHeaders = new LinkedHashSet<>();

        private boolean customSensitiveHeaders = false;

        public FranRoute() {}

        public FranRoute(String id, String path, String serviceId, String[] url,
                         boolean stripPrefix, Boolean retryable, Set<String> sensitiveHeaders) {
            this.id = id;
            this.path = path;
            this.serviceId = serviceId;
            this.url = url;
            this.stripPrefix = stripPrefix;
            this.retryable = retryable;
            this.sensitiveHeaders = sensitiveHeaders;
            this.customSensitiveHeaders = sensitiveHeaders != null;
        }

        public FranRoute(String text) {
            String location = null;
            String path = text;
            if (text.contains("=")) {
                String[] values = StringUtils
                        .trimArrayElements(StringUtils.split(text, "="));
                location = values[1];
                path = values[0];
            }
            this.id = extractId(path);
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            setLocation(location);
            this.path = path;
        }

        public FranRoute(String path, String location) {
            this.id = extractId(path);
            this.path = path;
            setLocation(location);
        }

        public String getLocation() {

            if(keepalive!= null && keepalive.using){
                Server n = haService.getServer(key);
                if(n!= null)
                    return n.getUrl();
            }

            if (this.url!= null && this.url.length > 0) {
                long tid = Thread.currentThread().getId();
                long num = tid % url.length;
                return this.url[Long.valueOf(num).intValue()];
            }

            return this.serviceId;
        }

        public void setLocation(String location) {
            if (location != null
                    && (location.startsWith("http:") || location.startsWith("https:"))) {
                this.url = new String[]{location};
            }
            else {
                this.serviceId = location;
            }
        }

        private String extractId(String path) {
            path = path.startsWith("/") ? path.substring(1) : path;
            path = path.replace("/*", "").replace("*", "");
            return path;
        }

        public Route getRoute(String prefix) {
            return new Route(this.id, this.path, getLocation(), prefix, this.retryable,
                    isCustomSensitiveHeaders() ? this.sensitiveHeaders : null,
                    this.stripPrefix);
        }

        public void setSensitiveHeaders(Set<String> headers) {
            this.customSensitiveHeaders = true;
            this.sensitiveHeaders = new LinkedHashSet<>(headers);
        }

        public Keepalive getKeepalive() {
            return keepalive;
        }

        public void setKeepalive(Keepalive keepalive) {
            this.keepalive = keepalive;
        }

        public boolean isCustomSensitiveHeaders() {
            return this.customSensitiveHeaders;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String[] getUrl() {
            return url;
        }

        public void setUrl(String[] url) {
            this.url = url;
        }

        public boolean isStripPrefix() {
            return stripPrefix;
        }

        public void setStripPrefix(boolean stripPrefix) {
            this.stripPrefix = stripPrefix;
        }

        public Boolean getRetryable() {
            return retryable;
        }

        public void setRetryable(Boolean retryable) {
            this.retryable = retryable;
        }

        public Set<String> getSensitiveHeaders() {
            return sensitiveHeaders;
        }

        public void setCustomSensitiveHeaders(boolean customSensitiveHeaders) {
            this.customSensitiveHeaders = customSensitiveHeaders;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FranRoute that = (FranRoute) o;
            return customSensitiveHeaders == that.customSensitiveHeaders &&
                    Objects.equals(id, that.id) &&
                    Objects.equals(path, that.path) &&
                    Objects.equals(retryable, that.retryable) &&
                    Objects.equals(sensitiveHeaders, that.sensitiveHeaders) &&
                    Objects.equals(serviceId, that.serviceId) &&
                    stripPrefix == that.stripPrefix &&
                    Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customSensitiveHeaders, id, path, retryable,
                    sensitiveHeaders, serviceId, stripPrefix, url);
        }

        @Override public String toString() {
            return new StringBuilder("ZuulRoute{").append("id='").append(id).append("', ")
                    .append("path='").append(path).append("', ")
                    .append("serviceId='").append(serviceId).append("', ")
                    .append("url='").append(url).append("', ")
                    .append("stripPrefix=").append(stripPrefix).append(", ")
                    .append("retryable=").append(retryable).append(", ")
                    .append("sensitiveHeaders=").append(sensitiveHeaders).append(", ")
                    .append("customSensitiveHeaders=").append(customSensitiveHeaders).append(", ")
                    .append("}").toString();
        }

    }

}
