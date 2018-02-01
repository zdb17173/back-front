package org.fran.front.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by fran on 2018/1/29.
 */
@Configuration
@ConfigurationProperties(prefix = "roles")
public class SecurityRoleConfig {
    List<SecurityRoleConfigVo> configs;

    public List<SecurityRoleConfigVo> getConfigs() {
        return configs;
    }

    public void setConfigs(List<SecurityRoleConfigVo> configs) {
        this.configs = configs;
    }

    public static class SecurityRoleConfigVo {
        String path;
        String[] roles;
        boolean permitAll = false;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }

        public boolean isPermitAll() {
            return permitAll;
        }

        public void setPermitAll(boolean permitAll) {
            this.permitAll = permitAll;
        }
    }
}
