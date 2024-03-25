package com.example.demo11.service;

import com.example.demo11.entity.TeacherCode;
import com.example.demo11.entity.Team;
import com.example.demo11.entity.User;
import com.example.demo11.form.UserForm;
import com.example.demo11.repository.TeacherCodeRepository;
import com.example.demo11.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TeacherCodeRepository teacherCodeRepository;

    public void save(
            UserForm userForm,
            String userImg) {

        Team team = Team.builder()
                .uuid(userForm.getTeamName())
                .build();

        User user = new User();
        user.setId(userForm.getId());
        user.setLoginId(userForm.getLoginId());
        user.setUsername(userForm.getUsername());
        user.setRoles(userForm.getRoles());
        user.setRealName(userForm.getRealName());
        user.setPhoneNum(userForm.getPhoneNum());
        user.setGuardianNum(userForm.getGuardianNum());
        user.setSubject(userForm.getSubject());
        user.setTeamName(userForm.getTeamName());
        user.setProvider(userForm.getProvider());
        user.setProviderId(userForm.getProviderId());
        user.setNickname(userForm.getNickname());
        user.setUserImgPath(userImg);
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userRepository.save(user);
    }

    public String emailUsername(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if(byUsername.isPresent()) {
            return "duplicate";
        } else {
            return "null";
        }
    }

    public String teacherCheck(String teacherCode) {
        Optional<TeacherCode> byTeacherCode = teacherCodeRepository.findByTeacherCode(teacherCode);
        if (byTeacherCode.isPresent()) {
            return "duplicate";
        } else {
            return null;
        }

    }


}
