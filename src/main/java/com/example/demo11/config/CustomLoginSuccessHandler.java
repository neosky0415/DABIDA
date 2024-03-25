package com.example.demo11.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        List<String> roleNames = new ArrayList<String>();

        //Authentication 객체는 인증된 대상의 정보를 담고있다.
        //그 객체에서 authority(권한)을 추출해 roleNames 리스트에 담는다.
        authentication.getAuthorities().forEach(authority -> {
            roleNames.add(authority.getAuthority());
        });

        //ROLE_ADMIN 이 포함돼있으면 /sample/admin으로 리다이렉트
        if(roleNames.contains("ROLE_ADMIN")) {
            response.sendRedirect("/manager");
            return;
        }

        //ROLE_MEMBER 이 포함돼있으면 /sample/member으로 리다이렉트
        if(roleNames.contains("ROLE_USER")) {
            response.sendRedirect("/");
            return;
        }

        //둘다 없으면 메인 페이지로 리다이렉트
        response.sendRedirect("/");
    }

}
