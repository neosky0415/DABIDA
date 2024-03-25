package com.example.demo11.studentTeamInfo;

import com.example.demo11.entity.Subject;
import com.example.demo11.entity.Team;
import com.example.demo11.entity.User;
import com.example.demo11.repository.SubjectRepository;
import com.example.demo11.repository.TeamRepository;
import com.example.demo11.repository.TestStudentsRepository;
import com.example.demo11.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class StudentManageController {
    private final HttpSession session;
    private final UserRepository userRepository;
    private final TestStudentsRepository testStudentsRepository;
    private final SubjectRepository subjectRepository;
    private final TeamRepository teamRepository;



    @GetMapping("/studentManage/team_list")
    public String team_list(
            Model model
    ) {
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Team> teamList = teamRepository.findAll(sort);
        model.addAttribute("teamList", teamList);

        List <CountForm> countList = new ArrayList<CountForm>();
        model.addAttribute("countList", countList);
        for(int i=0; i<teamList.size(); i++) {
            teamList.get(i).getTeamName();
            List<User> userTeamList = userRepository.findByTeamName(teamList.get(i).getTeamName());
            CountForm countForm = new CountForm();
            int count = 0 ;
            count = userTeamList.size();
            countForm.setUuid(teamList.get(i).getUuid());
            countForm.setTeamName(teamList.get(i).getTeamName());
            countForm.setCreatedAt(teamList.get(i).getCreatedAt());
            countForm.setEditedAt(teamList.get(i).getEditedAt());
            countForm.setCount(count);
            countList.add(countForm);
        }

        return "view/team_list";
    }

    @GetMapping("/studentManage/team_student_list/{uuid}")
    public String team_student_list(
            Model model,
            @PathVariable String uuid,
            DataChartsForm dataChartsForm

    ) {

        Optional<Team> teamOptional = teamRepository.findById(uuid);
        String teamName = null;
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            teamName = teamOptional.get().getTeamName();

            model.addAttribute("team", team);
        }

        List<User> byTeamName = userRepository.findByTeamName(teamName);
        model.addAttribute("byTeamName", byTeamName);

                Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Subject> subjectList = subjectRepository.findAll(sort);
        model.addAttribute("subjectList", subjectList);

        return "view/team_student_list";
    }

    @PostMapping("/studentManage_info/{name}")
    public String studentManage_info(
            @PathVariable String name,
            DataChartsForm dataChartsForm,
            Model model
            ) {

        session.setAttribute("name", name);
        String sub1 = dataChartsForm.getSubject1();
        String sub2 = dataChartsForm.getSubject2();
        String sub3 = dataChartsForm.getSubject3();
        session.setAttribute("sub1", sub1);
        session.setAttribute("sub2", sub2);
        session.setAttribute("sub3", sub3);

        Optional<User> userOptional = userRepository.findByUsername(name);
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

        return "charts/test_chart";
    }

}
