package com.example.demo11.controller;

import com.example.demo11.entity.Subject;
import com.example.demo11.entity.TeacherCode;
import com.example.demo11.entity.User;
import com.example.demo11.form.TeacherCodeForm;
import com.example.demo11.form.UserForm;
import com.example.demo11.repository.SubjectRepository;
import com.example.demo11.repository.TeacherCodeRepository;
import com.example.demo11.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class ManagerController {

    private final UserRepository userRepository;
    private final HttpSession session;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherCodeRepository teacherCodeRepository;

    @GetMapping("/manager")
        public String manager(
    ) {
          return "manager/manager";
    }


    @GetMapping("/all")
    public String all(){
        return "selectBox/all";
    }


    @GetMapping("/staff_info")
    public String staff_info(
            Principal principal, Model model
    ){
        String author = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(author);
        User user = userOptional.get();
        BeanUtils.copyProperties(userOptional, user);
        model.addAttribute("userOptional", userOptional);
        model.addAttribute("user", user);
        session.setAttribute("author",author);

        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Subject> subjectList = subjectRepository.findAll(sort);
        model.addAttribute("subjectList", subjectList);

        return "manager/staff_info";
    }

    @GetMapping("/admin_staff_info/{author}")
    public String admin_staff_info(
            @PathVariable String author,
            Model model
    ) {
        Optional<User> userOptional = userRepository.findByUsername(author);
        User user = userOptional.get();
        BeanUtils.copyProperties(userOptional, user);

        String staffName = user.getRealName();
        String staffPhone = user.getPhoneNum();
        String staffSubject = user.getSubject();
        String staffId = user.getUsername();

        model.addAttribute("staffId", staffId);
        model.addAttribute("staffSubject", staffSubject);
        model.addAttribute("staffPhone", staffPhone);
        model.addAttribute("staffName", staffName);
        model.addAttribute("staffPhone", staffPhone);
        model.addAttribute("userOptional", userOptional);
        model.addAttribute("user", user);
        session.setAttribute("author",author);
        return "admin/admin_staff_info";

    }



    @Transactional
    @PostMapping("/staff_info_edit")
    public String staff_info_edit (
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
        user.setSubject(userForm.getSubject());
        userRepository.save(user);
        return "redirect:/staff_info";
    }



    @GetMapping("/staff_password")
    public String staff_password( UserForm userForm){
        return "manager/staff_password";
    }

    @PostMapping("/staff_password")
    public String staff_password(
            UserForm userForm,
            @Valid BindingResult bindingResult
    ){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();
        String password = ((UserDetails) principal).getPassword();
        if(!passwordEncoder.matches(userForm.getPassword(),password)){
            bindingResult.rejectValue("password","inCorrect","현재 비밀번호가 일치 하지 않습니다.");
            return  "manager/staff_password";
        }
        return "redirect:/staff_passwordchange";
    }


    @GetMapping("/staff_passwordchange")
    public String staff_passwordchange(
            Principal principal, Model model,
            UserForm userForm
    ){
        String author = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(author);
        User user = userOptional.get();
        BeanUtils.copyProperties(userOptional, user);
        model.addAttribute("userOptional", userOptional);
        session.setAttribute("author",author);


        return "/manager/staff_passwordchange";
    }

    @PostMapping("/staff_passwordchange")
    public String staff_passwordchange(
            UserForm userForm
    ){
        String username = (String) session.getAttribute("author");
        User user = userRepository.findByUsername(username).orElseGet(() -> {
            // IllegalArgumentException 예외 처리
            throw new IllegalArgumentException("해당하는 아이디가 없습니다 username : " + username);
        });
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userRepository.save(user);
        return "redirect:/staff_info";
    }

    @GetMapping("/staff_userDelete")
    public  String staff_userDelete(){
        return "manager/staff_userDelete";
    }

    @GetMapping("/teacherCode")
    public String teacherCode(
            TeacherCodeForm teacherCodeForm,
            Model model
    ) {
        List<TeacherCode> all = teacherCodeRepository.findAll();
        all.forEach(teacherCode -> {
            String teacherCode1 = teacherCode.getTeacherCode();
            Long id = teacherCode.getId();
            model.addAttribute("teacherCode1", teacherCode1);
            model.addAttribute("id", id);
        });
        model.addAttribute("all", all);
        return "manager/teacherCode";
    }

    @GetMapping("/teacherCode_add")
    @ResponseBody
    public String teacherCode_add(
            TeacherCodeForm teacherCodeForm,
            Model model
    ) {

        TeacherCode teacherCode = TeacherCode.builder()
                .teacherCode("1234")
                .build();
        teacherCodeRepository.save(teacherCode);
        return "manager/teacherCode_result";
    }

    @GetMapping("/teacherCode_edit/{id}")
    public String teacherCode_edit(
            TeacherCodeForm teacherCodeForm,
            @PathVariable Long id,
            Model model
    ) {
        Optional<TeacherCode> byId = teacherCodeRepository.findById(id);
        if (byId.isPresent()) {
            TeacherCode teacherCode = byId.get();
            BeanUtils.copyProperties(teacherCode, teacherCodeForm);
        }
        model.addAttribute("teacherCodeForm", teacherCodeForm);
        return "manager/teacherCode_edit";
    }

    @PostMapping("/teacherCode_edit")
    public String teacherCode_edit(
            @Valid TeacherCodeForm teacherCodeForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        Optional<TeacherCode> byId = teacherCodeRepository.findById(teacherCodeForm.getId());
        if (byId.isPresent()) {
            TeacherCode teacherCode = byId.get();
            BeanUtils.copyProperties(teacherCodeForm, teacherCode);

            teacherCode = teacherCodeRepository.save(teacherCode);

            if (teacherCode != null) {
                String toast_message = "수정완료.";
                redirectAttributes.addFlashAttribute("toast_message", toast_message);
                return "manager/teacherCode_result";
            }
        }
        return "manager/teacherCode_edit";
    }



}

