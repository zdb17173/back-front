package org.fran.back.springboot.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created by fran on 2018/1/31.
 */
@Service
public class UserService {
    static ThreadLocal<CustomUser> curUser = new ThreadLocal<>();

    public CustomUser getCurUser(){
        return curUser.get();
    }

    public void setCurUser(CustomUser customUser){
        curUser.set(customUser);
    }

    public void remove(){
        curUser.remove();
    }

    public static class CustomRole{
        String authority;

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }
    }

    public static class CustomUser{

        String username;
        String password;
        List<CustomRole> authorities;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<CustomRole> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<CustomRole> authorities) {
            this.authorities = authorities;
        }
    }
}
