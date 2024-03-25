package com.example.demo11.controller;

import com.example.demo11.entity.*;
import com.example.demo11.form.SubjectForm;
import com.example.demo11.form.TeamForm;
import com.example.demo11.form.UserForm;
import com.example.demo11.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class SelectController {

    private final SubjectRepository subjectRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final MiniTestScoreRepository miniTestScoreRepository;
    private final TestScoreRepository testScoreRepository;

    public SelectController(
            SubjectRepository subjectRepository,
            MiniTestScoreRepository miniTestScoreRepository,
            TestScoreRepository testScoreRepository,
            TeamRepository teamRepository,
            UserRepository userRepository
    ) {
        this.testScoreRepository = testScoreRepository;
        this.miniTestScoreRepository = miniTestScoreRepository;
        this.subjectRepository = subjectRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/subject_add")
    public String subject_add1(
            SubjectForm subjectForm
    ) {
        return "selectBox/subject_add";
    }

    @PostMapping("/subject_add")
    public String subject_add(
            @Valid SubjectForm subjectForm,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        Optional<Subject> subjectOptional = subjectRepository.findBySubject(subjectForm.getSubject());
        if (subjectOptional.isPresent()) {
            bindingResult.addError(new FieldError("subjectForm"
                    ,"subject", "이미 등록된 과정 입니다."));
        }
        if (bindingResult.hasErrors()) {
            System.out.println(subjectForm);
            model.addAttribute("subjectForm", subjectForm);
            return "selectBox/subject_add";
        }

        String name = principal.getName();
        Optional<User> byUsername = userRepository.findByUsername(name);
        if (byUsername.isPresent()) {
            User user = byUsername.get();

            Subject subject = Subject.builder()
                    .user(user)
                    .subject(subjectForm.getSubject())
                    .build();

            subjectRepository.save(subject);
        }
        return "selectBox/subject_add";
    }

    @GetMapping("/subject_list")
    public String subject_list(
            Model model,
            @ModelAttribute("toast_message") String toast_message
    ) {
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Subject> subjectList = subjectRepository.findAll(sort);
        model.addAttribute("subjectList", subjectList);


        return "selectBox/subject_list";
    }

    @GetMapping("/subject_edit/{uuid}")
    public String subject_edit(
            @PathVariable String uuid,
            SubjectForm subjectForm,
            Model model
    ) {
        Optional<Subject> subjectOptional = subjectRepository.findById(uuid);
        if (subjectOptional.isPresent()) {
            Subject s = subjectOptional.get();

            BeanUtils.copyProperties(s , subjectForm);
        }
        model.addAttribute("subjectForm", subjectForm);
        return "selectBox/subject_edit";
    }

    @PostMapping("/subject_edit")
    public String subject_edit(
            @Valid SubjectForm subjectForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {



            Optional<Subject> subjectOptional = subjectRepository.findById(subjectForm.getUuid());
            if (subjectOptional.isPresent()) {
                Subject s = subjectOptional.get();
                BeanUtils.copyProperties(subjectForm, s);


            }
        Optional<Subject> subject = subjectRepository.findBySubject(subjectForm.getSubject());
        if (subjectOptional.isPresent()) {
            bindingResult.addError(new FieldError("subjectForm"
                    ,"subject", "이미 등록된 과정 입니다."));
        }
        if (bindingResult.hasErrors()) {
            System.out.println(subjectForm);
            model.addAttribute("subjectForm", subjectForm);
            return "selectBox/subject_edit";
        }

        Subject subject1 = Subject.builder()
                .subject(subjectForm.getSubject())
                .build();

         subject1 = subjectRepository.save(subject1);


        if (subject1 != null) {
                    String toast_message = "수정완료.";
                    redirectAttributes.addFlashAttribute("toast_message", toast_message);
                    return "selectBox/select_edit_result";
                }


        return "subject_edit";
    }

    @GetMapping("/subject_delete/{uuid}")
    public String subject_delete(
            @PathVariable String uuid,
            RedirectAttributes redirectAttributes
    ) {
        subjectRepository.deleteById(uuid);

        String toast_message = "삭제 되었습니다.";

        redirectAttributes.addFlashAttribute("toast_message", toast_message);

        return "redirect:/subject_list";
    }


    @GetMapping("/team_add")
    public String team_add(
            TeamForm teamForm
    ) {
        return "selectBox/team_add";
    }


    @PostMapping("/team_add")
    public String team_add(
            @Valid TeamForm teamForm,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        Optional<Team> teamOptional = teamRepository.findByTeamName(teamForm.getTeamName());
        if (teamOptional.isPresent()) {
            bindingResult.addError(new FieldError("teamForm",
                    "teamName", "이미 등록된 반 입니다."));
        }
        if (bindingResult.hasErrors()) {
            System.out.println(teamForm);
            model.addAttribute("teamForm", teamForm);
            return "selectBox/team_add";
        }

        String name = principal.getName();
        Optional<User> byUsername = userRepository.findByUsername(name);
        if (byUsername.isPresent()) {
            User user = byUsername.get();

            Team team = Team.builder()
                    .user(user)
                    .teamName(teamForm.getTeamName())
                    .build();

            teamRepository.save(team);
        }
        return "selectBox/team_add";
    }

    @GetMapping("/team_list")
    public String team_list(
            Model model,
            @ModelAttribute("toast_message") String toast_message
    ) {
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Team> teamList = teamRepository.findAll(sort);
        model.addAttribute("teamList", teamList);

        return "selectBox/team_list";
    }

    @GetMapping("/team_student_list/{uuid}")
    public String team_student_list(
            Model model,
            @PathVariable String uuid,
            @ModelAttribute("toast_message") String toast_message
    ) {

        Optional<Team> teamOptional = teamRepository.findById(uuid);
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();


            model.addAttribute("team", team);
        }

        List<Team> byUuid = teamRepository.findAllByUuid(uuid);
        byUuid.forEach(team -> {
            String teamName = team.getTeamName();
            List<User> byTeamName = userRepository.findByTeamName(teamName);
            model.addAttribute("byTeamName", byTeamName);
        });

        return "selectBox/team_student_list";
    }

    @GetMapping("/subject_student_list/{uuid}")
    public String subject_student_list(
            Model model,
            @PathVariable String uuid,
            @ModelAttribute("toast_message") String toast_message
    ) {

        Optional<Subject> subjectOptional = subjectRepository.findById(uuid);
        if (subjectOptional.isPresent()) {
            Subject subject = subjectOptional.get();
            model.addAttribute("subject", subject);
        }

        List<Subject> allByUuid = subjectRepository.findAllByUuid((uuid));
        allByUuid.forEach(subject -> {
            String subject1 = subject.getSubject();
            List<User> bySubject = userRepository.findBySubject(subject1);


            model.addAttribute("bySubject", bySubject);
        });

        return "selectBox/subject_student_list";
    }

    @GetMapping("/student_score/{uuid}")
    public String student_score(
            Model model,
            @PathVariable Long uuid
    ) {
        Optional<User> userOptional = userRepository.findById(uuid);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            List<TestScore> byUser = testScoreRepository.findByUser(user);
            byUser.forEach(testScore -> {
                String uuid1 = testScore.getUuid();
                Optional<TestScore> byId = testScoreRepository.findById(uuid1);
                if (byId.isPresent()) {
                    TestScore testScore1 = byId.get();
            model.addAttribute("testScore1", testScore1);
                }
            });

        }
        return "selectBox/student_score";
    }

    @GetMapping("/student_mini_score/{uuid}")
    public String student_mini_score(
            Model model,
            @PathVariable Long uuid
    ) {
        Optional<User> userOptional = userRepository.findById(uuid);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            List<MiniTestScore> byUser1 = miniTestScoreRepository.findByUser(user);
            byUser1.forEach(miniTestScore -> {
                Long id = miniTestScore.getId();
                Optional<MiniTestScore> byId = miniTestScoreRepository.findById(id);
                if (byId.isPresent()) {
                    MiniTestScore miniTestScore1 = byId.get();
                    model.addAttribute("miniTestScore1", miniTestScore1);
                }
            });
        }
        return "selectBox/student_mini_score";
    }

    @GetMapping("/team_edit/{uuid}")
    public String team_edit(
            @PathVariable String uuid,
            TeamForm teamForm,
            Model model
    ) {
        Optional<Team> teamOptional = teamRepository.findById(uuid);
        if (teamOptional.isPresent()) {
            Team t = teamOptional.get();
            BeanUtils.copyProperties(t , teamForm);
        }

        model.addAttribute("subjectForm", teamForm);
        return "selectBox/team_edit";
    }

    @PostMapping("/team_edit")
    public String team_edit(
            @Valid TeamForm teamForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if(!bindingResult.hasErrors()) {
            Optional<Team> teamOptional = teamRepository.findById(teamForm.getUuid());
            if (teamOptional.isPresent()) {
                Team t = teamOptional.get();

                BeanUtils.copyProperties(teamForm, t);

                t = teamRepository.save(t);
                if (t != null) {
                    String toast_message = "수정완료";
                    redirectAttributes.addFlashAttribute("toast_message", toast_message);
                    return "selectBox/select_edit_result";
                }
            }
        }
        return "team_edit";
    }

    @GetMapping("/team_delete/{uuid}")
    public String team_delete(
            @PathVariable String uuid,
            RedirectAttributes redirectAttributes
    ) {
        teamRepository.deleteById(uuid);

        String toast_message = "삭제완료";

        redirectAttributes.addFlashAttribute("toast_message", toast_message);
        return "redirect:/team_list";
    }

    @GetMapping("/get_subjects")
    //@CrossOrigin
    public ResponseEntity<List<Subject>> getSubjects() {

        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Subject> testSubjectList = subjectRepository.findAll(sort);

        try {
            return new ResponseEntity<List<Subject>>(testSubjectList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<Subject>>(HttpStatus.BAD_REQUEST);
        }
    }

}
