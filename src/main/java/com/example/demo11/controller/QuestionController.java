package com.example.demo11.controller;

import com.example.demo11.entity.*;
import com.example.demo11.form.QuestionForm;
import com.example.demo11.form.TestNumberForm;
import com.example.demo11.repository.*;
import jakarta.servlet.http.HttpSession;
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
import java.util.*;



@Controller
public class QuestionController {


    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final TestedRepository testedRepository;
    private final TestNumberRepository testNumberRepository;
    private final SubjectRepository subjectRepository;
    private final TeamRepository teamRepository;
    private final TestStudentsRepository testStudentsRepository;
    private final HttpSession session;

    public QuestionController(
            HttpSession session,
            UserRepository userRepository,
            QuestionRepository questionRepository,
            TestedRepository testedRepository,
            TestNumberRepository testNumberRepository,
            SubjectRepository subjectRepository,
            TeamRepository teamRepository,
            TestStudentsRepository testStudentsRepository
    ) {
        this.userRepository = userRepository;
        this.session = session;
        this.subjectRepository = subjectRepository;
        this.questionRepository = questionRepository;
        this.testedRepository = testedRepository;
        this.testNumberRepository = testNumberRepository;
        this.teamRepository = teamRepository;
        this.testStudentsRepository = testStudentsRepository;
    }
    @GetMapping("/question_testtitle_list")
    public String question_testtitle_list(
            Principal principal,
            Model model
    ) {

        List<TestNumber> byMiniDateIsNull = testNumberRepository.findByMiniDateIsNullOrderByCreatedAtDesc();
        model.addAttribute("byMiniDateIsNull", byMiniDateIsNull);
        return "question/question_testtitle_list";
    }

    @GetMapping("/question_testtitle_add")
    public String question_testtitle_add(
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
        return "question/question_testtitle_add";
    }

    @PostMapping("/question_testtitle_add")
    public String question_testtitle_add(
            @Valid TestNumberForm testNumberForm,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {
        Optional<TestNumber> testTitleOptional = testNumberRepository.findByTestTitle(testNumberForm.getTestTitle());
        if (testTitleOptional.isPresent()) {
            bindingResult.addError(new FieldError("testNumberForm",
                    "testTitle", "이미 등록된 시험 입니다."));
        }
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("testNumberForm", testNumberForm);
            return "question/question_testtitle_add";
        }

        Set<String> set = new HashSet<>();
        String number = RandomStringUtils.randomNumeric(7);
        set.add(number);

        String setStr = String.format("3%s", set);
        String subSet = setStr.substring(2, 7);

        Optional<Subject> subjectOptional = subjectRepository.findBySubject(testNumberForm.getTestSubject());
        Subject subject = subjectOptional.get();
        Optional<Team> teamOptional = teamRepository.findByTeamName(testNumberForm.getTeamName());
        Team team = teamOptional.get();
        String author = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(author);
        User user = userOptional.get();
        TestNumber testNumber = TestNumber.builder()
                .testTitle(testNumberForm.getTestTitle())
                .testNumber(testNumberForm.getTestTitle() + subSet)
                .subject(subject)
                .team(team)
                .user(user)
                .build();
        testNumberRepository.save(testNumber);

        String student = "ROLE_USER";
        List<User> studentList = userRepository.findByRolesAndTeamName(student, testNumberForm.getTeamName());
        for(int i=0; i<studentList.size(); i++) {
            User user1 = studentList.get(i);
            TestStudents testStudents = new TestStudents();
            testStudents.setTestNumber(testNumber);
            testStudents.setUser(user1);
            testStudentsRepository.save(testStudents);
        }

        return "question/testtitle_add_result";
    }

        @GetMapping("/testtitle_add_result")
        public String testtitle_add_result (){
            return "question/testtitle_add_result";
        }

    @GetMapping("/testtitle_edit_result")
    public String testtitle_edit_result (){
        return "question/testtitle_edit_result";
    }

    @GetMapping ("/question_testtitle_edit/{uuid}")
    public String question_testtitle_edit (
            @PathVariable String uuid,
            Model model,
            TestNumberForm testNumberForm
    ) {
        Optional<TestNumber> testNumberOptional = testNumberRepository.findByUuid(uuid);
        if (testNumberOptional.isPresent()) {
            TestNumber testNumber = testNumberOptional.get();
            BeanUtils.copyProperties(testNumber, testNumberForm);

            model.addAttribute("testNumberForm", testNumberForm);


            Sort sort = Sort.by(
                    Sort.Order.desc("createdAt"));
            List<Subject> subjectList = subjectRepository.findAll(sort);
            model.addAttribute("subjectList", subjectList);

            Sort sort1 = Sort.by(
                    Sort.Order.desc("createdAt"));
            List<Team> teamList = teamRepository.findAll(sort1);
            model.addAttribute("teamList", teamList);

        }
        return "question/question_testtitle_edit";
    }

    @PostMapping ("/question_testtitle_edit")
    public String question_testtitle_edit (
            Model model,
            @Valid TestNumberForm testNumberForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Principal principal
    ) {
        Optional<TestNumber> byUuid = testNumberRepository.findByUuid((testNumberForm.getUuid()));
        if (byUuid.isPresent()) {
            TestNumber testNumber = byUuid.get();
            BeanUtils.copyProperties(testNumberForm, testNumber);

            Optional<TestNumber> byTestTitle = testNumberRepository.findByTestTitle(testNumberForm.getTestTitle());
            if (byTestTitle.isPresent()) {
                bindingResult.addError(new FieldError("testNumber",
                        "testTitle", "시험제목 중복"));
            }
            if (bindingResult.hasFieldErrors()) {
                model.addAttribute("testNumberForm", testNumberForm);
                return "question/question_testtitle_edit";
            }


            Set<String> set = new HashSet<>();
            String number = RandomStringUtils.randomNumeric(7);
            set.add(number);

            String setStr = String.format("3%s", set);
            String subSet = setStr.substring(2, 7);

            Optional<Subject> subjectOptional = subjectRepository.findBySubject(testNumberForm.getTestSubject());
            Subject subject = subjectOptional.get();
            Optional<Team> teamOptional = teamRepository.findByTeamName(testNumberForm.getTeamName());
            Team team = teamOptional.get();
            String author = principal.getName();
            Optional<User> userOptional = userRepository.findByUsername(author);
            User user= userOptional.get();

            testNumber.setTestNumber(testNumberForm.getTestTitle() + subSet);
            testNumber.setUser(user);
            testNumber.setSubject(subject);
            testNumber.setTeam(team);
            testNumber.setTestTitle(testNumberForm.getTestTitle());


            testNumber = testNumberRepository.save(testNumber);
            if (testNumber != null) {
                String toast_message = "수정 완료";
                redirectAttributes.addFlashAttribute("toast_message", toast_message);
                return "question/testtitle_edit_result";
            }

            String student = "ROLE_USER";
            List<User> studentList = userRepository.findByRolesAndTeamName(student, testNumberForm.getTeamName());
            for(int i=0; i<studentList.size(); i++) {
                User user1 = studentList.get(i);
                TestStudents testStudents = new TestStudents();
                testStudents.setTestNumber(testNumber);
                testStudents.setUser(user1);
                testStudentsRepository.save(testStudents);
            }
        }
        return "redirect:/question_testtitle_list";
    }

    @GetMapping("/question_testtitle_delete/{uuid}")
    public String question_testtitle_delete(
            @PathVariable String uuid,
            RedirectAttributes redirectAttributes
    ) {
        testNumberRepository.deleteById(uuid);

        String toast_message = "삭제완료";

        redirectAttributes.addFlashAttribute("toast_message", toast_message);
        return "redirect:/question_testtitle_list";
    }

    @GetMapping("/question_add/{uuid}") //시험문제등록하기
    public String question_add(
            QuestionForm questionForm,
            @PathVariable String uuid,
            Model model
    ){
        String testNumberUuidAdd = uuid;
        model.addAttribute("testNumberUuidAdd", testNumberUuidAdd);
        return "/question/question_add";
    }


    @PostMapping("/question_add/{uuid}")
    public String question_add(
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
                return  "redirect:/question_add_result/{uuid}";
            }
        }
        return "/question/question_add";
    }


    @GetMapping("/question_add_result/{uuid}")
    public String question_add_result(
            @PathVariable String uuid,
            Model model
    ){

        Optional<TestNumber> testNumberOptional = testNumberRepository.findById(uuid);
        if (testNumberOptional.isPresent()) {
            TestNumber testNumberUuid = testNumberOptional.get();
            TestNumber testTitle = testNumberOptional.get();
            model.addAttribute("testNumberUuid", testNumberUuid);

            session.setAttribute("testNumberUuid", testNumberUuid);
        } return "/question/question_add_result";
    }



    @GetMapping("/question_list/{uuid}")
    public String question_test_question_list(
            Model model,
            @PathVariable String uuid,
            @ModelAttribute("toast_message") String toast_message
    ) {
        List<Question> questionList = questionRepository.findByTestNumberUuidOrderByCreatedAtDesc(uuid);
        if (!questionList.isEmpty()) {
            model.addAttribute("questionList",questionList);
        }

        Optional<TestNumber> testNumberOptional = testNumberRepository.findById(uuid);
        if (testNumberOptional.isPresent()) {
            TestNumber testNumberUuid = testNumberOptional.get();
            TestNumber testTitle = testNumberOptional.get();

            model.addAttribute("testNumberUuid", testNumberUuid);
            model.addAttribute("testTitle", testTitle);

            session.setAttribute("testNumberUuid", testNumberUuid);
        }

        return "/question/question_list";
    }

    @GetMapping ("/question_edit/{quuid}")
    public String question_edit (
            @PathVariable String quuid,
            Model model,
            Principal principal
    ) {

        Optional<Question> questionOptional = questionRepository.findById(quuid);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            model.addAttribute("question", question);
            return "question/question_edit";

        }

        return "question/question_edit";
    }


    @PostMapping("/question_edit/{uuid}")
    public String question_edit (
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

                    return "redirect:/question_list/{uuid}";
                }
            }
        } return "question/question_edit";

    }

    @GetMapping ("/question_details/{quuid}")
    public String question_details (
            @PathVariable String quuid,
            Model model, Principal principal
    ) {

        Optional<Question> questionOptional = questionRepository.findById(quuid);
        Question question = questionOptional.get();
        BeanUtils.copyProperties(questionOptional, question);
        String username = principal.getName();
        question.setUsername(username);
        model.addAttribute("questionOptional", questionOptional);
        return "question/question_edit";

    }


    @GetMapping("/question_delete/{quuid}")
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
        return "redirect:/question_list";
    }







}
