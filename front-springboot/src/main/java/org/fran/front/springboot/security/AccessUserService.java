package org.fran.front.springboot.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fran on 2018/1/25.
 */
@Service
public class AccessUserService implements UserDetailsService {

    static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_role1"));
        authorities.add(new SimpleGrantedAuthority("ROLE_role2"));
        authorities.add(new SimpleGrantedAuthority("ROLE_role3"));

        return new CustomUser(
                username,
                username,//user name
                MD5Util.encode((String) username),//user pwd
                authorities);
    }

    public static class CustomUser implements UserDetails{
        String uid;
        String userName;
        String passWord;
        List<GrantedAuthority> grantedAuthority;

        public CustomUser(
                String uid,
                String userName,
                String passWord,
                List<GrantedAuthority> grantedAuthority
        ){
            this.uid = uid;
            this.userName = userName;
            this.passWord = passWord;
            this.grantedAuthority = grantedAuthority;
        }

        @Override
        public String toString(){
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return "";
            }
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return grantedAuthority;
        }

        @Override
        public String getPassword() {
            return passWord;
        }

        @Override
        public String getUsername() {
            return userName;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
