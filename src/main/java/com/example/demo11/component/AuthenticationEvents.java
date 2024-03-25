
package com.example.demo11.component;

import com.example.demo11.service.LoginUserDetailService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationEvents {

    private final LoginUserDetailService loginUserDetailService;

    public AuthenticationEvents(LoginUserDetailService loginUserDetailService) {
        this.loginUserDetailService = loginUserDetailService;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        String username = success.getAuthentication().getName();
        loginUserDetailService.resetLoginFailureCount(username);
     }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        String username = failures.getAuthentication().getName();
        loginUserDetailService.incrementLoginFailureCount(username);
    }
}

