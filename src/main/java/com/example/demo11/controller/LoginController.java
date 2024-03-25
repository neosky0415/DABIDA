package com.example.demo11.controller;

import com.example.demo11.entity.User;
import com.example.demo11.form.FindIdForm;
import com.example.demo11.form.LoginForm;
import com.example.demo11.form.UserForm;
import com.example.demo11.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
@RequiredArgsConstructor
@Controller
public class LoginController {


    private final UserRepository userRepository;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(
            LoginForm loginForm
    ) {
        return "/login/login";
    }

    @GetMapping("/logout")
    public  String logout(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth !=null) {
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }
        return "redirect:/";
    }

    @GetMapping("/find_id")
    public String findId(
            FindIdForm findIdForm){

        return "login/find_id";
    }

    @PostMapping("/find_id")
    public String findId(
            FindIdForm findIdForm,
            Model model,
            @Valid BindingResult bindingResult
    ){
        String name =  findIdForm.getRealName();
        String Phone = findIdForm.getPhoneNum();
        Optional<User> userOptional = userRepository.findByRealNameAndPhoneNum(name,Phone);
        if( userOptional.isPresent()) {
            User user = userOptional.get();
            String id = user.getUsername();
            Date idDate = user.getCreatedAt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
            String formatIdDate = simpleDateFormat.format(idDate);

            model.addAttribute("id", id);
            model.addAttribute("formatIdDate", formatIdDate);
            return "login/result_find_id";
        }
        bindingResult.rejectValue("realName","inCorrect","입력하신 정보와 일치한 회원 정보를 찾을 수 없습니다.");
        return  "login/find_id";
    }

    @GetMapping("/find_password")
    public String find_password(
           FindIdForm findIdForm

    ){
        return "login/find_password";
    }

    @PostMapping("/find_password")
    public String find_password(
            FindIdForm findIdForm,
            @Valid BindingResult bindingResult
    ){
        String userId= findIdForm.getUsername();
       String pass= findIdForm.getMail_number();
       String pass1= findIdForm.getMail_number1();
        if (!pass1.equals(pass)) {
            bindingResult.rejectValue("mail_number","inCorrect","현재 인증 번호가 일치 하지 않습니다.");
            return  "login/find_password";
        }
        session.setAttribute("userId", userId);
        return "redirect:/find_passwordchange";

    }
    @GetMapping("/find_passwordchange")
    public String password(
            UserForm userForm,
            Model model
    ){
        String findUserId = (String) session.getAttribute("userId");
        Optional<User> userOptional = userRepository.findByUsername(findUserId);
        User user = userOptional.get();
        BeanUtils.copyProperties(userOptional, user);
        model.addAttribute("userOptional", userOptional);
        session.setAttribute("findUserId",findUserId);
        return "/login/find_passwordchange";
    }

    @PostMapping("/find_passwordchange")
    public String passwordchange(
            UserForm userForm
    ){
        String find_User = (String) session.getAttribute("findUserId");
        User user = userRepository.findByUsername(find_User).orElseGet(() -> {
            // IllegalArgumentException 예외 처리
            throw new IllegalArgumentException("해당하는 아이디가 없습니다 username : " + find_User);
        });
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/userDelete")
    public String userDelete(
    ){
        return "login/userDelete";
    }

    @GetMapping("/userDelete_password")
    public String userDelete_password(
            UserForm userForm
    ){
        return "login/userDelete_password";
    }

    @PostMapping("/userDelete_password")
    public String userDelete_password(
            UserForm userForm,
            @Valid BindingResult bindingResult
    ){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();
        String password = ((UserDetails) principal).getPassword();
        if(!passwordEncoder.matches(userForm.getPassword(),password)){
            bindingResult.rejectValue("password","inCorrect","현재 비밀번호가 일치 하지 않습니다.");
            return  "login/userDelete_password";
        }
        return "redirect:/userDelete_pass";
    }

    @Transactional
    @GetMapping("/userDelete_pass")
    public String userDelete_password(
            Principal principal,
            Model model
    ) {
        String delUser = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(delUser);
        User user = userOptional.get();
        Long id = user.getId();
        userRepository.deleteById(id);
        model.addAttribute("delUser",delUser);
        return "login/userDelete_result";
    }
}
