package com.example.demo11.config;


import com.example.demo11.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final PrincipalOauth2UserService principalOauth2UserService;


    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {

//
//        //csrf disable
//        http
//                .csrf((auth) -> auth.disable());
//
//        //From 로그인 방식 disable
//        http
//                .formLogin((auth) -> auth.disable());
//
//        //http basic 인증 방식 disable
//        http
//                .httpBasic((auth) -> auth.disable());
//


        http.csrf((auth)-> auth.ignoringRequestMatchers("/mail/**"));
        http.csrf((auth)-> auth.ignoringRequestMatchers("/usernamecheck/**"));
        http.csrf((auth)-> auth.ignoringRequestMatchers("/teacherCheck/**"));

//        http.csrf().ignoringRequestMatchers("/mail/**");
//        http.csrf().ignoringRequestMatchers("/usernamecheck/**");
//        http.csrf().ignoringRequestMatchers("/teacherCheck/**");

        http.formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .successHandler(customLoginSuccessHandler)
                        .loginPage("/login")
                        .permitAll())
                        .logout(logout -> logout.logoutUrl("/logout"));

        http .headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));// Disable X-Frame-Options
//      http.headers(headerConfig -> headerConfig.frameOptions().disable());


        http.oauth2Login(oauth2Configure -> oauth2Configure
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .successHandler(customLoginSuccessHandler)
                    .defaultSuccessUrl("/",true)


                    .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(principalOauth2UserService)));

//                        .userInfoEndpoint()
//                        .userService(principalOauth2UserService));

        http.authorizeHttpRequests((authorities)->authorities
//                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/introduce").permitAll()
                        .requestMatchers("/recruitment").permitAll()
                        .requestMatchers("/curriculum").permitAll()
                        .requestMatchers("/find_id").permitAll()
                        .requestMatchers("/result_find_id").permitAll()
                        .requestMatchers("/find_password").permitAll()
                        .requestMatchers("/find_passwordchange").permitAll()
                        .requestMatchers("/mail/**").permitAll()
                        .requestMatchers("/usernamecheck/**").permitAll()
                        .requestMatchers("/teacherCheck/**").permitAll()
                        .requestMatchers("/teacherCode_add").permitAll()
                        .requestMatchers("/user/user").permitAll()
                        .requestMatchers("/user").permitAll()
                        .requestMatchers("/user_add").permitAll()
                // 모달창 허용
                .requestMatchers("/test").permitAll()
                        .requestMatchers("/css/**" , "/js/**", "/webjars/**" ,"/images/**").permitAll()
                        .requestMatchers("/question_add").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/manager").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/question_list").hasRole("ADMIN")
                        .requestMatchers("/userList/**").hasRole("ADMIN")
                        .requestMatchers("/test_number_check/**").hasRole("USER")
                        .anyRequest().authenticated());
        return http.build();
    }
}
