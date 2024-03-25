package com.example.demo11.service;

import com.example.demo11.entity.User;
import com.example.demo11.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public void resetLoginFailureCount(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLoginFailureCount(0);
            user = userRepository.save(user);
            if(user != null) {
                System.out.println("login failure reset");
            }
        }
    }
    public void incrementLoginFailureCount(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLoginFailureCount(user.getLoginFailureCount() + 1);
            user = userRepository.save(user);
            if(user != null) {
                System.out.println("login failure increment");
            }
        }
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> _user = userRepository.findByUsername(username);
        if(_user.isPresent()) {
            User user = _user.get();
            return new LoginUserDetails(user);
        }
         throw new UsernameNotFoundException("not found email" + username);

    }




}
