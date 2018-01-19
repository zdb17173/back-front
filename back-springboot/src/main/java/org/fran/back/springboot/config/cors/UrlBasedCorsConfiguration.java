package org.fran.back.springboot.config.cors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "cors")
public class UrlBasedCorsConfiguration {
    private Map<String, CorsConfiguration> configs;

    public Map<String, CorsConfiguration> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, CorsConfiguration> configs) {
        this.configs = configs;
    }
}
