package org.fran.front.springboot.security;

import org.fran.front.springboot.config.SecurityRoleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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

            //https://blog.coding.net/blog/Explore-the-cache-request-of-Security-Spring
            //由于缓存问题造成X-Requested-With获取不到， 暂时关闭缓存
            http.requestCache().requestCache(new NullRequestCache());


            /*http.sessionManagement()
                .sessionAuthenticationStrategy(new SessionAuthenticationStrategy() {
                    @Override
                    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
                        //登录拦截
                        System.out.println("sessionAuthenticationStrategy:login!" + authentication.getPrincipal());
                    }
                })
//                    .invalidSessionStrategy(new SimpleRedirectInvalidSessionStrategy("/login"))
                .invalidSessionStrategy(new InvalidSessionStrategy() {
                    @Override
                    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

                        Cookie[] cks = request.getCookies();
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                        if(authentication.getPrincipal() instanceof String){
                            request.getSession();
                            response.sendRedirect("/login");
                        }else{
                            System.out.println("hello" + authentication.getPrincipal());
                        }
                    }
                })
            ;*/

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