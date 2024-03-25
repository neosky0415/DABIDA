package com.example.demo11.controller;


import com.example.demo11.entity.Question;
import com.example.demo11.entity.Team;
import com.example.demo11.entity.User;
import com.example.demo11.form.UserForm;
import com.example.demo11.repository.MiniTestStudentsRepository;
import com.example.demo11.repository.QuestionRepository;
import com.example.demo11.repository.TestStudentsRepository;
import com.example.demo11.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class MyPageController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestionRepository questionRepository;
    private final HttpSession session;
    private final TestStudentsRepository testStudentsRepository;
    private final MiniTestStudentsRepository miniTestStudentsRepository;


    @GetMapping("/myinfo")
    public String myinfo(Principal principal, Model model) {

        String author = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(author);
        User user = userOptional.get();
        BeanUtils.copyProperties(userOptional, user);
        model.addAttribute("userOptional", userOptional);
        model.addAttribute("user", user);
        session.setAttribute("author",author);
        return "mypage/myinfo";
    }

    @GetMapping("/admin_my_info/{author}")
    public String admin_my_info(
            @PathVariable String author,
            Model model,
            Team team
    ) {

        Optional<User> userOptional = userRepository.findByUsername(author);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            model.addAttribute("user", user);
        }


        User user = userOptional.get();
        BeanUtils.copyProperties(userOptional, user);

        String studentName = user.getRealName();
        String studentPhone = user.getPhoneNum();
        String studentPhone2 =user.getGuardianNum();
        String studentId = user.getUsername();


        model.addAttribute("studentName", studentName);
        model.addAttribute("studentPhone", studentPhone);
        model.addAttribute("studentPhone2", studentPhone2);
        model.addAttribute("studentId", studentId);
        model.addAttribute("userOptional", userOptional);
        model.addAttribute("user", user);
        session.setAttribute("author",author);

        return "admin/admin_my_info";

    }


    @Transactional
    @PostMapping ("/myinfo_edit")
    public String myinfo_edit (
            MultipartFile userFile,
            Model model,
            UserForm userForm
    )throws Exception {
        String username = (String) session.getAttribute("author");
        User user = userRepository.findByUsername(username).orElseGet(() -> {
            // IllegalArgumentException 예외 처리
            throw new IllegalArgumentException("해당하는 아이디가 없습니다 username : " + username);
        });
        String oriImgName = userFile.getOriginalFilename();
        String userImgName = "";

        if( oriImgName == userImgName ){

        } else {
            String projectPath = System.getProperty("user.dir") + "./uploads";
            UUID uuid = UUID.randomUUID();
            String userImg = uuid + "_" + oriImgName;
            userImgName = userImg;
            File userSaveFile = new File(projectPath, userImgName);
            userFile.transferTo(userSaveFile);
            user.setUserImgPath(userImg);
        }
        user.setRealName(userForm.getRealName());
        user.setPhoneNum(userForm.getPhoneNum());
        user.setGuardianNum(userForm.getGuardianNum());
        user.setTeamName(userForm.getTeamName());
        userRepository.save(user);
        return "redirect:/myinfo";
    }


    @GetMapping("/my_password")
    public String my_password(
            UserForm userForm
    ){
        return "mypage/my_password";
    }


    @PostMapping("/my_password")
    public String post_my_password(
            UserForm userForm,
            @Valid BindingResult bindingResult
    ){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();
        String password = ((UserDetails) principal).getPassword();
        if(!passwordEncoder.matches(userForm.getPassword(),password)){
            bindingResult.rejectValue("password","inCorrect","현재 비밀번호가 일치 하지 않습니다.");
            return  "mypage/my_password";
        }
        return "redirect:/passwordchange";
    }


    @GetMapping("/passwordchange")
    public String password(
            Principal principal, Model model,
            UserForm userForm
    ){
        String author = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(author);
        User user = userOptional.get();
        BeanUtils.copyProperties(userOptional, user);
        model.addAttribute("userOptional", userOptional);
        session.setAttribute("author",author);


        return "/mypage/passwordchange";
    }

    @PostMapping("/passwordchange")
    public String passwordchange(
            UserForm userForm
    ){
        String username = (String) session.getAttribute("author");
        User user = userRepository.findByUsername(username).orElseGet(() -> {
            // IllegalArgumentException 예외 처리
            throw new IllegalArgumentException("해당하는 아이디가 없습니다 username : " + username);
        });
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userRepository.save(user);
        return "redirect:/myinfo";
    }


    @GetMapping("/my_study")
    public String my_study(){
        return "/mypage/my_study";
    }


    @GetMapping("/my_question_list")
    public String my_question_list(
            Principal principal,
            Model model
    ) {
        String username = principal.getName();
        List<Question> questionList = this.questionRepository.findByUsername(username);
        if (!questionList.isEmpty()){
            model.addAttribute("questionList", questionList);

             }else {

        }
        return "mypage/my_question_list";

        }

}















