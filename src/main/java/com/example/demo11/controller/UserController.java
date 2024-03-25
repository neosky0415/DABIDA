package com.example.demo11.controller;

import com.example.demo11.entity.Subject;
import com.example.demo11.entity.TeacherCode;
import com.example.demo11.entity.Team;
import com.example.demo11.entity.User;
import com.example.demo11.form.UserForm;
import com.example.demo11.repository.SubjectRepository;
import com.example.demo11.repository.TeamRepository;
import com.example.demo11.repository.UserRepository;
import com.example.demo11.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Controller
public class UserController {
    //회원가입 컨트롤러

    private final UserService userService;
    private final UserRepository userRepository;
    private final HttpSession session;
    private final SubjectRepository subjectRepository;
    private final TeamRepository teamRepository;

    public UserController(
            UserService userService,
            UserRepository userRepository,
            HttpSession session,
            SubjectRepository subjectRepository,
            TeamRepository teamRepository
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.session = session;
        this.subjectRepository = subjectRepository;
        this.teamRepository = teamRepository;
    }


    @GetMapping("/user/user")
    public String user(
            UserForm userForm,
            Model model
    ){
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Subject> subjectList = subjectRepository.findAll(sort);

        Sort sort1 = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Team> teamList = teamRepository.findAll(sort1);

        model.addAttribute("subjectList", subjectList);
        model.addAttribute("teamList", teamList);
        return "user/user";
    }

    @PostMapping("/user_add")
    public String user(
            @ModelAttribute
            @Valid UserForm userForm,
            MultipartFile userFile,
            BindingResult bindingResult
    ) throws Exception  {
        String oriImgName = userFile.getOriginalFilename();
        String userImgName = "";
        String projectPath = System.getProperty("user.dir") + "./uploads";
        UUID uuid = UUID.randomUUID();

        if (!bindingResult.hasErrors()) {
            if (oriImgName!=userImgName) {
                String userImg = uuid + "_" + oriImgName; // 파일명 -> imgName
                userImgName = userImg;
                File userSaveFile = new File(projectPath, userImgName);
                userFile.transferTo(userSaveFile);
                userService.save(userForm , userImg);
            } else {
                String userImg = null; // 파일명 -> imgName
//                userImgName = userImg;
                userService.save(userForm, null);
            }
            return "login/login";
        }
        return "user/user";
    }



    @GetMapping("/userList")
    public String userList(
            Model model,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            String searchRoles
    ) {

        //        /*검색기능-3*/
        Page<User> list = null;
        /*searchKeyword = 검색하는 단어*/

        if(searchRoles == null){
            list = userRepository.findAll(pageable);
        } else if(searchRoles == ""){
            list = userRepository.findAll(pageable); //기존의 리스트보여줌
        }else {
            list = userRepository.findByRoles(searchRoles, pageable); //검색리스트반환
        }

        long count = list.stream().count();

        int Page = list.getPageable().getPageNumber() + 1; //pageable에서 넘어온 현재페이지를 가지고올수있다 * 0부터시작하니까 +1
//        int startPage = Math.max(Page - 4, 1); //매개변수로 들어온 두 값을 비교해서 큰값을 반환
//        int endPage = Math.min(Page + 5, list.getTotalPages());

        //BoardService에서 만들어준 boardList가 반환되는데, list라는 이름으로 받아서 넘기겠다는 뜻
        model.addAttribute("userList" , list);
        model.addAttribute("Page", Page);
        model.addAttribute("count", count);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);

        return "user/user_list";
    }

    @Transactional
    @PostMapping("/user_admin_edit/{id}")
    public String User_admin_edit (
            @PathVariable Long id,
            Model model,
            UserForm userForm
    ) {
        Optional<User> userOptional =  userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setRoles(userForm.getRoles());
            user.setLoginFailureCount(userForm.getLoginFailureCount());
            userRepository.save(user);
        } else {

        }
        return "redirect:/userList";

    }


    @PostMapping("/teacherCheck")
    @ResponseBody
    public String teacherCheck(
            @RequestBody UserForm userForm

    ) {

        String teacherCode = userForm.getTeacherCode();
        String checkResult = userService.teacherCheck(teacherCode);
        return checkResult;
    }


    @PostMapping("/usernamecheck")
    public @ResponseBody String emailUsername(
            @RequestBody UserForm userForm

            ) {

        String username = userForm.getUsername();
        String result = userService.emailUsername(username);
        return result;
    }



}


