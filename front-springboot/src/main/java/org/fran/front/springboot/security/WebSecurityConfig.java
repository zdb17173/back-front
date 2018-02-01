package org.fran.front.springboot.security;

import org.fran.front.springboot.config.SecurityRoleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessUserService accessUserService;
    @Autowired
    SessionRegistry sessionRegistry;
    @Autowired
    SecurityRoleConfig securityRoleConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<SecurityRoleConfig.SecurityRoleConfigVo> configs = securityRoleConfig.getConfigs();
        if(configs == null){
            http.csrf().disable();
        }else {
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry author = http.authorizeRequests();
            for (SecurityRoleConfig.SecurityRoleConfigVo conf : configs) {
                if (conf.getPath() != null) {
                    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authors = author.antMatchers(conf.getPath());
                    if(conf.isPermitAll()){
                        author = authors.permitAll();
                    }else{
                        author = authors.hasAnyRole(conf.getRoles());
                    }
                }
            }

            author.and().formLogin().loginPage("/login").failureUrl("/login-error");
            http.exceptionHandling().authenticationEntryPoint(new RequestForbiddenHandle("/login"));
            http.csrf().disable();
        }

        /*http.csrf().disable();
        http
            .authorizeRequests()
            .antMatchers("/css*//**//**", "/index").permitAll()
            .antMatchers("/local*//**//**").hasRole("role1")
            .and()
            .formLogin().loginPage("/login").failureUrl("/login-error");*/
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accessUserService).passwordEncoder(new PasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {
                return MD5Util.encode((String) rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(MD5Util.encode((String) rawPassword));
            }
        });
    }

    @Bean
    public SessionRegistry getSessionRegistry(){
        SessionRegistry sessionRegistry=new SessionRegistryImpl();
        return sessionRegistry;
    }
}