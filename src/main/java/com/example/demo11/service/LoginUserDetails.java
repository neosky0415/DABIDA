package com.example.demo11.service;

import com.example.demo11.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


public class LoginUserDetails implements UserDetails, OAuth2User {
    int lockingBoundaries = 3;

    private User user;
    private String nickname;
    private String username;
    private String realName;
    private String password;

    private String userImgPath;

    Collection <? extends GrantedAuthority> authorities;


    public LoginUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.realName = user.getRealName();
        this.userImgPath = user.getUserImgPath();
        this.authorities =  Arrays.stream(user.getRoles().split(","))
                .map(role -> new SimpleGrantedAuthority(role))
                .toList();
        this.user = user;
    }
    public LoginUserDetails(User user, Map<String, Object> attributes) {

        this.authorities =  Arrays.stream(user.getRoles().split(","))
                .map(role -> new SimpleGrantedAuthority(role))
                .toList();
        this.user = user;
        this.attributes = attributes;

    }
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {

        return attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public String getNickname() {
        return user.getNickname();
    }


    public String getRealName() {
        return user.getRealName();
    }

    public String getUserImgPath() {return user.getUserImgPath();}


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (this.user.getLoginFailureCount() >= lockingBoundaries) {
            return false;
        }
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


    @Override
    public String getName() {
        return null;
    }


}
