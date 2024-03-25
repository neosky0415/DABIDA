package com.example.demo11.controller;

import com.example.demo11.entity.*;
import com.example.demo11.form.QuestionForm;
import com.example.demo11.form.TestNumberForm;
import com.example.demo11.repository.*;
import jakarta.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MiniTestController {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final TestedRepository testedRepository;
    private final TestNumberRepository testNumberRepository;
    private final SubjectRepository subjectRepository;
    private final TeamRepository teamRepository;

    private final  MiniTestStudentsRepository miniTestStudentsRepository;

    public MiniTestController(
            UserRepository userRepository,
            QuestionRepository questionRepository,
            TestedRepository testedRepository,
            TestNumberRepository testNumberRepository,
            SubjectRepository subjectRepository,
            TeamRepository teamRepository,

            MiniTestStudentsRepository miniTestStudentsRepository

    ) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.questionRepository = questionRepository;
        this.testedRepository = testedRepository;
        this.testNumberRepository = testNumberRepository;
        this.teamRepository = teamRepository;

        this.miniTestStudentsRepository= miniTestStudentsRepository;


    }

    @GetMapping("/mini_test_title_list")
    public String mini_test_title_list(
            Principal principal,
            Model model
    ) {

        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));

        List<TestNumber> byMiniDateIsNotNull = testNumberRepository.findByMiniDateIsNotNullOrderByCreatedAtDesc();
        model.addAttribute("byMiniDateIsNotNull", byMiniDateIsNotNull);


        return "miniTest/mini_test_title_list";
    }

    @GetMapping("/mini_test_title_result")
    public String mini_test_title_result(){
        return "miniTest/mini_test_title_result";
    }

    @GetMapping("/mini_test_title_add")
    public String mini_test_title_add(
            TestNumberForm testNumberForm,
            Model model
    ) {
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Subject> subjectList = subjectRepository.findAll(sort);
        model.addAttribute("subjectList", subjectList);

        Sort sort1 = Sort.by(
                Sort.Order.desc("createdAt"));
        List<Team> teamList = teamRepository.findAll(sort1);
        model.addAttribute("teamList", teamList);

        return "miniTest/mini_test_title_add";
    }

    @PostMapping("/mini_test_title_add")
    public String mini_test_title_add(
            Principal principal,
            @Valid TestNumberForm testNumberForm,
            BindingResult bindingResult,
            Model model
    ) {
        Optional<TestNumber> testTitleOptional = testNumberRepository.findByMiniTitle(testNumberForm.getMiniTitle());
        if (testTitleOptional.isPresent()) {
            bindingResult.addError(new FieldError("testNumberForm",
                    "miniTitle", "시험제목 중복"));
        }
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("testNumberForm", testNumberForm);
            return "miniTest/mini_test_title_add";
        }

        Set<String> set = new HashSet<>();


        String number = RandomStringUtils.randomNumeric(7);
        set.add(number);
        String setStr = String.format("3%s", set);
        String subSet = setStr.substring(2, 7);
        String miniDate = testNumberForm.getMiniDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            dateFormat.parse(miniDate);
        } catch (ParseException e) {
            bindingResult.addError(new FieldError("testNumberForm"
            , "year", "잘 못된 날짜 입니다."));
        }

        Date testDate = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            testDate = df.parse(miniDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Optional<Team> byTeamName = teamRepository.findByTeamName(testNumberForm.getTeamName());
        if (byTeamName.isPresent()) {
            Team team = byTeamName.get();



        Optional<Subject> subjectOptional = subjectRepository.findBySubject(testNumberForm.getTestSubject());
        Subject subject = subjectOptional.get();
        String author = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(author);
        User user= userOptional.get();

        TestNumber testNumber = TestNumber.builder()
                .miniTitle(testNumberForm.getMiniTitle())
                .miniDate(testDate)
                .subject(subject)
                .team(team)
                .user(user)
                .testNumber("m" + testNumberForm.getMiniTitle() + subSet)
                .build();
        testNumberRepository.save(testNumber);

        String student = "ROLE_USER";
        List<User> studentList = userRepository.findByRolesAndTeamName(student, testNumberForm.getTeamName());
        for(int i=0; i<studentList.size(); i++) {
            User user1 = studentList.get(i);
            MiniTestStudents miniTestStudents = new MiniTestStudents();
            miniTestStudents.setTestNumber(testNumber);
            miniTestStudents.setUser(user1);
            miniTestStudentsRepository.save(miniTestStudents);
        }
    }
        return "miniTest/mini_test_title_result";
    }

    @GetMapping("/mini_test_title_edit/{uuid}")
    public String mini_test_title_edit(
            @PathVariable String uuid,
            TestNumberForm testNumberForm,
            Model model
    ) {
        Optional<TestNumber> byUuid = testNumberRepository.findByUuid(uuid);
        if (byUuid.isPresent()) {
            TestNumber testNumber = byUuid.get();
            BeanUtils.copyProperties(testNumber, testNumberForm);

            Sort sort = Sort.by(
                    Sort.Order.desc("createdAt"));
            List<Subject> subjectList = subjectRepository.findAll(sort);
            model.addAttribute("subjectList", subjectList);

            Sort sort1 = Sort.by(
                    Sort.Order.desc("createdAt"));
            List<Team> teamList = teamRepository.findAll(sort1);
            model.addAttribute("teamList", teamList);

        }
        model.addAttribute("testNumberForm", testNumberForm);
        return "miniTest/mini_test_title_edit";
    }


    @PostMapping("/mini_test_title_edit")
    public String mini_test_title_edit(
            @Valid TestNumberForm testNumberForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model,
            Principal principal
    ) {

        Optional<TestNumber> byUuid = testNumberRepository.findByUuid(testNumberForm.getUuid());
        if (byUuid.isPresent()) {
            TestNumber testNumber = byUuid.get();

            BeanUtils.copyProperties(testNumberForm, testNumber);


            Optional<TestNumber> testTitleOptional = testNumberRepository.findByMiniTitle(testNumberForm.getMiniTitle());
            if (testTitleOptional.isPresent()) {
                bindingResult.addError(new FieldError("testNumberForm",
                        "miniTitle", "시험제목 중복"));
            }
            if (bindingResult.hasFieldErrors()) {
                model.addAttribute("testNumberForm", testNumberForm);
                return "miniTest/mini_test_title_add";
            }

            Set<String> set = new HashSet<>();
            String number = RandomStringUtils.randomNumeric(7);
            set.add(number);
            String setStr = String.format("3%s", set);
            String subSet = setStr.substring(2, 7);
            String miniDate = testNumberForm.getMiniDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                dateFormat.parse(miniDate);
            } catch (ParseException e) {
                bindingResult.addError(new FieldError("testNumberForm"
                        , "year", "잘 못된 날짜 입니다."));
            }

            Date testDate = null;
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                testDate = df.parse(miniDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Optional<Subject> subjectOptional = subjectRepository.findBySubject(testNumberForm.getTestSubject());
            Subject subject = subjectOptional.get();
            String author = principal.getName();
            Optional<User> userOptional = userRepository.findByUsername(author);
            User user= userOptional.get();

            testNumber.setMiniTitle(testNumberForm.getMiniTitle());
            testNumber.setMiniDate(testDate);
            testNumber.setSubject(subject);
            testNumber.setUser(user);
            testNumber.setTestNumber("m" + testNumberForm.getMiniTitle() + subSet);



            testNumber = testNumberRepository.save(testNumber);
            if (testNumber != null) {
                String toast_message = "수정 완료";
                redirectAttributes.addFlashAttribute("toast_message", toast_message);
                return "miniTest/mini_test_title_edit_result";
            }
        }

        return "miniTest/mini_test_title_edit";
    }

    @GetMapping("/mini_test_title_delete/{uuid}")
    public String mini_test_title_delete(
            @PathVariable String uuid,
            RedirectAttributes redirectAttributes
    ) {
        testNumberRepository.deleteById(uuid);

        String toast_message = "삭제완료";

        redirectAttributes.addFlashAttribute("toast_message", toast_message);
        return "redirect:/mini_test_title_list";
    }



    @GetMapping("/mini_test_list/{uuid}")
    public String mini_test_list(
            Model model,
            @PathVariable String uuid,
            @ModelAttribute("toast_message") String toast_message
    ) {
        List<Question> miniTestList = questionRepository.findByTestNumberUuidOrderByCreatedAtDesc(uuid);
        if (!miniTestList.isEmpty()) {
            model.addAttribute("miniTestList", miniTestList);
        }

        Optional<TestNumber> testNumberOptional = testNumberRepository.findById(uuid);
        if (testNumberOptional.isPresent()) {
            TestNumber testNumber = testNumberOptional.get();
            model.addAttribute("testNumber", testNumber);
        }
        return "/miniTest/mini_test_list";
    }

    @GetMapping("/mini_test_add/{uuid}")
    public String mini_test_add(
            QuestionForm questionForm,
            @PathVariable String uuid,
            Model model
    ) {
        String testNumberUuidAdd = uuid;
        model.addAttribute("testNumberUuidAdd", testNumberUuidAdd);
        return "/miniTest/mini_test_add";
    }

    @PostMapping("/mini_test_add/{uuid}")
    public String mini_test_add(
            @PathVariable String uuid,
            MultipartFile imgFile,
            MultipartFile choice1Img,
            MultipartFile choice2Img,
            MultipartFile choice3Img,
            MultipartFile choice4Img,
            Model model,
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal
    )throws Exception
    {
        String oriImgName = imgFile.getOriginalFilename();
        String oriChoice1 = choice1Img.getOriginalFilename();
        String oriChoice2 = choice2Img.getOriginalFilename();
        String oriChoice3 = choice3Img.getOriginalFilename();
        String oriChoice4 = choice4Img.getOriginalFilename();
        String imgName = "";
        String choice1 = "";
        String choice2 = "";
        String choice3 = "";
        String choice4 = "";



        String projectPath = System.getProperty("user.dir") + "./uploads";
        UUID uuid2 = UUID.randomUUID();
        if(!bindingResult.hasErrors()) {
            String author = principal.getName();
            Optional<User> userOptional = userRepository.findByUsername(author);
            User user = userOptional.get();
            Question question = new Question();
            TestNumber testNumber2 = TestNumber.builder()
                    .uuid(uuid)
                    .build();

            if (oriImgName!=imgName) {


                String titleImg = uuid2 + "_" + oriImgName; // 파일명 -> imgName
                imgName = titleImg;
                File titleSaveFile = new File(projectPath, imgName);
                imgFile.transferTo(titleSaveFile);
                question.setImgPath(imgName);
                question.setTitle(questionForm.getTitle());
            } else {
                question.setTitle(questionForm.getTitle());

            }

            if (oriChoice1!=choice1) {
                String oriChoice1Img= uuid2 + "_" + oriChoice1; // 파일명 -> imgName
                choice1 = oriChoice1Img;
                File choice1SaveFile = new File(projectPath, choice1);
                choice1Img.transferTo(choice1SaveFile);
                question.setChoice1Img(choice1);
                question.setChoice1(" ");
                questionForm.getChoice1();
            } else {
                question.setChoice1(questionForm.getChoice1());
            }

            if (oriChoice2!=choice2) {
                String oriChoice2Img= uuid2 + "_" + oriChoice2; // 파일명 -> imgName
                choice2 = oriChoice2Img;
                File choice2SaveFile = new File(projectPath,   choice2);
                choice2Img.transferTo(choice2SaveFile);
                question.setChoice2Img(choice2);
                question.setChoice2(" ");

            }else {
                question.setChoice2(questionForm.getChoice2());
            }

            if (oriChoice3!=choice3) {
                String oriChoice3Img= uuid2 + "_" + oriChoice3; // 파일명 -> imgName
                choice3 = oriChoice3Img;
                File choice3SaveFile = new File(projectPath,   choice3);
                choice3Img.transferTo(choice3SaveFile);
                question.setChoice3Img(choice3);
                question.setChoice3(" ");
            }else {
                question.setChoice3(questionForm.getChoice3());
            }

            if (oriChoice4!=choice4) {
                String oriChoice4Img= uuid2 + "_" + oriChoice4; // 파일명 -> imgName
                choice4 = oriChoice4Img;
                File choice4SaveFile = new File(projectPath,   choice4);
                choice4Img.transferTo(choice4SaveFile);
                question.setChoice4Img(choice4);
                question.setChoice4(" ");
            }else {
                question.setChoice4(questionForm.getChoice4());
            }


            question.setTestNumber(testNumber2);
            question.setCorrectAnswer(questionForm.getCorrectAnswer());
            question.setUser(user);
            question=questionRepository.save(question);

            if(question != null) {
                return  "redirect:/mini_test_title_list/{uuid}";
            }
        }
        return "/miniTest/mini_test_add";
    }


    @GetMapping("/mini_test_add_result/{uuid}")
    public String mini_test_add_result(
            Model model,
            @PathVariable String uuid

    ){
        Optional<TestNumber> testNumberOptional = testNumberRepository.findById(uuid);
        if (testNumberOptional.isPresent()) {
            TestNumber testNumber = testNumberOptional.get();
            model.addAttribute("testNumber", testNumber);
        }

        return "miniTest/mini_test_add_result";
    }



    @GetMapping("/mini_test_edit/{quuid}")
    public String mini_test_edit(
            @PathVariable String quuid,
            Model model,
            Principal principal
    ) {
        Optional<Question> questionOptional = questionRepository.findById(quuid);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            model.addAttribute("question", question);
        }
        return "miniTest/mini_test_edit";
    }

    @PostMapping("/mini_test_edit/{uuid}")
    public String mini_test_edit (
            @PathVariable String uuid,
            MultipartFile imgFile,
            MultipartFile choice1Img,
            MultipartFile choice2Img,
            MultipartFile choice3Img,
            MultipartFile choice4Img,
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    )throws Exception {


        if(!bindingResult.hasErrors()) {
            String quuid = questionForm.getQuuid();
            Optional<Question> questionOptional = questionRepository.findById(quuid);
            if(questionOptional.isPresent()) {
                Question question = questionOptional.get();
                BeanUtils.copyProperties(questionForm,question);

                TestNumber testNumber2 = TestNumber.builder()
                        .uuid(uuid)
                        .build();

                question.setQuuid(quuid);
                question.setCorrectAnswer(questionForm.getCorrectAnswer());
                question.setTestNumber(testNumber2);
                String projectPath = System.getProperty("user.dir") + "./uploads";
                UUID uuid2 = UUID.randomUUID();
                String oriImgName = imgFile.getOriginalFilename();
                String oriChoice1 = choice1Img.getOriginalFilename();
                String oriChoice2 = choice2Img.getOriginalFilename();
                String oriChoice3 = choice3Img.getOriginalFilename();
                String oriChoice4 = choice4Img.getOriginalFilename();
                String imgName = "";
                String choice1 = "";
                String choice2 = "";
                String choice3 = "";
                String choice4 = "";


                if (oriImgName!=imgName) {

                    String titleImg = uuid2 + "_" + oriImgName; // 파일명 -> imgName
                    imgName = titleImg;
                    File titleSaveFile = new File(projectPath, imgName);
                    imgFile.transferTo(titleSaveFile);
                    question.setImgPath(imgName);
                    question.setTitle(" ");
                } else {
                    question.setTitle(questionForm.getTitle());

                }

                if (oriChoice1!=choice1) {
                    String oriChoice1Img= uuid2 + "_" + oriChoice1; // 파일명 -> imgName
                    choice1 = oriChoice1Img;
                    File choice1SaveFile = new File(projectPath, choice1);
                    choice1Img.transferTo(choice1SaveFile);
                    question.setChoice1Img(choice1);
                    question.setChoice1(" ");
                    questionForm.getChoice1();
                } else {
                    question.setChoice1(questionForm.getChoice1());
                }

                if (oriChoice2!=choice2) {
                    String oriChoice2Img= uuid2 + "_" + oriChoice2; // 파일명 -> imgName
                    choice2 = oriChoice2Img;
                    File choice2SaveFile = new File(projectPath,   choice2);
                    choice2Img.transferTo(choice2SaveFile);
                    question.setChoice2Img(choice2);
                    question.setChoice2(" ");
                    questionForm.getChoice2();
                }else {
                    question.setChoice2(questionForm.getChoice2());
                }

                if (oriChoice3!=choice3) {
                    String oriChoice3Img= uuid2 + "_" + oriChoice3; // 파일명 -> imgName
                    choice3 = oriChoice3Img;
                    File choice3SaveFile = new File(projectPath,   choice3);
                    choice3Img.transferTo(choice3SaveFile);
                    question.setChoice3Img(choice3);
                    question.setChoice3(" ");
                }else {
                    question.setChoice3(questionForm.getChoice3());
                }

                if (oriChoice4!=choice4) {
                    String oriChoice4Img= uuid2 + "_" + oriChoice4; // 파일명 -> imgName
                    choice4 = oriChoice4Img;
                    File choice4SaveFile = new File(projectPath,   choice4);
                    choice4Img.transferTo(choice4SaveFile);
                    question.setChoice4Img(choice4);
                    question.setChoice4(" ");
                }else {
                    question.setChoice4(questionForm.getChoice4());
                }
                questionRepository.save(question);

                if(question!= null) {
                    redirectAttributes.addFlashAttribute("toast_message","문제가 수정 되었습니다!");

                    return "redirect:/mini_test_list/{uuid}";
                }
            }
        } return "miniTest/mini_test_edit";
    }

    @GetMapping("/mini_test_delete/{quuid}")
    public String question_delete (
            @PathVariable String quuid,
            RedirectAttributes redirectAttributes

    ) {
        String message = "";
        List<String> idlist = testedRepository.findByQuestionUuid(quuid);
        if(idlist.size()>0) {
            message = "해당 문제는 이미 출제 되어 삭제할 수 없습니다.";
        } else {
            questionRepository.deleteById(quuid);
            message = "삭제 되었습니다.";
        }
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/mini_test_list";
    }
}
