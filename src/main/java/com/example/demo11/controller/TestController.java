package com.example.demo11.controller;


import com.example.demo11.entity.*;
import com.example.demo11.form.TestForm;
import com.example.demo11.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
public class TestController {

    private final QuestionRepository questionRepository;
    private final HttpSession session;
    private final TestedRepository testedRepository;
    private final TestedInfoRepository testedInfoRepository;
    private final TestScoreRepository testScoreRepository;
    private final UserRepository userRepository;
    private final TestNumberRepository testNumberRepository;
    private final TestStudentsRepository testStudentsRepository;
    private final MiniTestScoreRepository miniTestScoreRepository;
    private final MiniTestStudentsRepository miniTestStudentsRepository;

    public TestController(
            QuestionRepository questionRepository,
            HttpSession session,
            TestedRepository testedRepository,
            TestedInfoRepository testedInfoRepository,
            TestScoreRepository testScoreRepository,
            UserRepository userRepository,
            TestNumberRepository testNumberRepository,
            TestStudentsRepository testStudentsRepository,
            MiniTestScoreRepository miniTestScoreRepository,
            MiniTestStudentsRepository miniTestStudentsRepository

    ) {
        this.testedRepository = testedRepository;
        this.questionRepository = questionRepository;
        this.testedInfoRepository = testedInfoRepository;
        this.session = session;
        this.testScoreRepository = testScoreRepository;
        this.userRepository = userRepository;
        this.testNumberRepository = testNumberRepository;
        this.testStudentsRepository = testStudentsRepository;
        this.miniTestScoreRepository = miniTestScoreRepository;
        this.miniTestStudentsRepository = miniTestStudentsRepository;


    }

    @PostMapping("/test_number_check/{uuid}")
    public String test_number_check(
            Principal principal,
            @PathVariable String uuid

    ) {
        String tester = principal.getName();
        Optional<TestStudents> testStudentsOptional = testStudentsRepository.findByUserUsernameAndTestNumberUuid(tester, uuid);
        TestStudents testStudents = testStudentsOptional.get();
        String number = testStudents.getTestNumber().getTestNumber();
        Optional<TestNumber> testNumberOptional = testNumberRepository.findByTestNumber(number);
        if (testNumberOptional.isPresent()) {
            TestNumber testNumber = testNumberOptional.get();
            String testNumber1 = testNumber.getTestNumber();
            session.setAttribute("testNumber1", testNumber1);
            session.setAttribute("testStudents", testStudents);


            if (testNumber1.equals(number)) {
                return "redirect:/test_attention";
            }

        }
        return "redirect:/my_test_list";
    }


    @PostMapping("/miniTest_number_check/{uuid}")
    public String miniTest_number_check(
            Principal principal,
            @PathVariable String uuid

    ) {
        String tester = principal.getName();
        Optional<MiniTestStudents> miniTestStudentssOptional = miniTestStudentsRepository.findByUserUsernameAndTestNumberUuid(tester, uuid);
        MiniTestStudents miniTestStudents = miniTestStudentssOptional.get();
        String number = miniTestStudents.getTestNumber().getTestNumber();
        Optional<TestNumber> testNumberOptional = testNumberRepository.findByTestNumber(number);
        if (testNumberOptional.isPresent()) {
            TestNumber testNumber = testNumberOptional.get();
            String testNumber1 = testNumber.getTestNumber();
            session.setAttribute("testNumber1", testNumber1);
            session.setAttribute("miniTestStudents", miniTestStudents);


            if (testNumber1.equals(number)) {
                return "redirect:/test_attention";
            }

        }
        return "redirect:/my_test_list";
    }


    @GetMapping("/test_attention")
    public String test_attention() {
        return "/test/test_attention";
    }

    @GetMapping("/test")
    public String test(
            TestForm testForm,
            Question question,
            Model model
    ) {
        String testNumber1 = (String) session.getAttribute("testNumber1");

        Optional<TestNumber> byTestNumber = testNumberRepository.findByTestNumber(testNumber1);
        if (byTestNumber.isPresent()) {
            TestNumber testNumber = byTestNumber.get();
            String testSubject = testNumber.getSubject().getSubject();

            List<Question> questionList = questionRepository.findAllByTestNumber(testNumber);

            model.addAttribute("questionList", questionList);
            model.addAttribute("testSubject", testSubject);

            session.setAttribute("testNumber", testNumber);
            session.setAttribute("questionList", questionList);
        }
        return "/test/test";
    }

    @GetMapping("/test_result")
    public String test(
            TestForm testForm,
            TestedInfo testedInfo,
            Principal principal,
            Model model
    ) {
        String testNumber1 = (String) session.getAttribute("testNumber1");
        String substring = testNumber1.substring(0, 1);
        String m = "m";
        if (!substring.equals(m)) {
            TestNumber testNumber = (TestNumber) session.getAttribute("testNumber");

            String author = principal.getName();
            Optional<User> userOptional = userRepository.findByUsername(author);
            if (userOptional.isPresent()) {
                User user = userOptional.get();


                List<Question> questionList = (List) session.getAttribute("questionList");
                List<Integer> answerList = new ArrayList<>(
                        Arrays.asList(
                                testForm.getTest_1(),
                                testForm.getTest_2(),
                                testForm.getTest_3(),
                                testForm.getTest_4(),
                                testForm.getTest_5(),
                                testForm.getTest_6(),
                                testForm.getTest_7(),
                                testForm.getTest_8(),
                                testForm.getTest_9(),
                                testForm.getTest_10(),
                                testForm.getTest_11(),
                                testForm.getTest_12(),
                                testForm.getTest_13(),
                                testForm.getTest_14(),
                                testForm.getTest_15(),
                                testForm.getTest_16(),
                                testForm.getTest_17(),
                                testForm.getTest_18(),
                                testForm.getTest_19(),
                                testForm.getTest_20()
                        )
                );

                int hitCount = 0;
                double c = 0;
                int score = 0;
                String passMessage = null;


                UUID uuid = UUID.randomUUID();
                for (int i = 0; i < questionList.size(); i++) {
                    Question question = questionList.get(i);
                    int correctAnswer = Integer.parseInt(question.getCorrectAnswer());
                    Tested tested = new Tested();
                    tested.setTestedUuid(uuid.toString());
                    tested.setQuestion(question);
                    tested.setUser(user);
                    tested.setTestNumber(testNumber);
                    tested.setAnswer(answerList.get(i));
                    testedRepository.save(tested);
                    if (answerList.get(i) == correctAnswer) {
                        hitCount++;
                    }


                    double ab = (double) hitCount / questionList.size() * 100;
                    c = Math.round(ab * 10.0) / 10.0;

                    score = (int) c;

                    passMessage = "불 합 격";
                    if (score >= 60.0) {
                        passMessage = "합 격";
                    }

                    testedInfo = new TestedInfo().builder()
                            .uuid(uuid.toString())
                            .user(user)
                            .testNumber(testNumber)
                            .correctAnswerCount(hitCount)
                            .questionCount(questionList.size())
                            .score(score)
                            .testResult(passMessage)
                            .build();
                    testedInfo = testedInfoRepository.save(testedInfo);
                    if (testedInfo != null) {
                    }

                }
                session.setAttribute("uuid", uuid.toString());


            }

            return "redirect:/test_score_add";

        } else if (substring.equals(m)) {

            TestNumber testNumber = (TestNumber) session.getAttribute("testNumber");
            String testNumberUuid = testNumber.getUuid();
            TestNumber testNumber2 = TestNumber.builder()
                    .uuid(testNumberUuid)
                    .build();
            String author = principal.getName();
            Optional<User> userOptional = userRepository.findByUsername(author);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Long id = user.getId();
                User user1 = User.builder()
                        .id(id)
                        .build();
                List<Question> questionList = (List) session.getAttribute("questionList");
                List<Integer> answerList = new ArrayList<>(
                        Arrays.asList(
                                testForm.getTest_1(),
                                testForm.getTest_2(),
                                testForm.getTest_3(),
                                testForm.getTest_4(),
                                testForm.getTest_5(),
                                testForm.getTest_6(),
                                testForm.getTest_7(),
                                testForm.getTest_8(),
                                testForm.getTest_9(),
                                testForm.getTest_10(),
                                testForm.getTest_11(),
                                testForm.getTest_12(),
                                testForm.getTest_13(),
                                testForm.getTest_14(),
                                testForm.getTest_15(),
                                testForm.getTest_16(),
                                testForm.getTest_17(),
                                testForm.getTest_18(),
                                testForm.getTest_19(),
                                testForm.getTest_20()
                        )
                );

                int hitCount = 0;
                double c = 0;
                int score = 0;
                String passMessage = null;

                UUID uuid = UUID.randomUUID();
                for (int i = 0; i < questionList.size(); i++) {
                    Question question = questionList.get(i);
                    int correctAnswer = Integer.parseInt(question.getCorrectAnswer());
                    Tested tested = new Tested();
                    tested.setTestedUuid(uuid.toString());
                    tested.setQuestion(question);
                    tested.setUser(user1);
                    tested.setTestNumber(testNumber2);
                    tested.setAnswer(answerList.get(i));
                    testedRepository.save(tested);
                    if (answerList.get(i) == correctAnswer) {
                        hitCount++;
                    }


                    double ab = (double) hitCount / questionList.size() * 100;
                    c = Math.round(ab * 10.0) / 10.0;

                    score = (int) c;

                    passMessage = "불 합 격";
                    if (score >= 60.0) {
                        passMessage = "합 격";
                    }

                    testedInfo = new TestedInfo().builder()
                            .uuid(uuid.toString())
                            .user(user)
                            .testNumber(testNumber)
                            .correctAnswerCount(hitCount)
                            .questionCount(questionList.size())
                            .score(score)
                            .testResult(passMessage)
                            .build();
                    testedInfo = testedInfoRepository.save(testedInfo);
                    if (testedInfo != null) {
                    }

                }
                session.setAttribute("uuid", uuid.toString());
                session.setAttribute("testedInfo", testedInfo);
            }
        }
        return "redirect:/mini_test_score_add";
    }

    @GetMapping("/mini_test_score_add")
    public String mini_test_score_add(
            Model model
    ) {

        TestedInfo testedInfo1 = (TestedInfo) session.getAttribute("testedInfo");
        MiniTestStudents miniTestStudents1 = (MiniTestStudents) session.getAttribute("miniTestStudents");
        miniTestStudents1.setTestedInfo(testedInfo1);
        miniTestStudentsRepository.save(miniTestStudents1);


        String uuid = (String) session.getAttribute("uuid");
        Optional<TestedInfo> byId = testedInfoRepository.findById(uuid);
        if (byId.isPresent()) {
            TestedInfo testedInfo = byId.get();
            List<Tested> byTestedUuid = testedRepository.findByTestedUuid(uuid);
            Tested tested = byTestedUuid.get(0);

            String testedUuid = tested.getUuid();
            Tested tested1 = Tested.builder()
                    .uuid(testedUuid)
                    .build();

            User user = tested.getUser();
            Long id = user.getId();
            User user1 = User.builder()
                    .id(id)
                    .build();

            TestNumber testNumber = tested.getTestNumber();
            Date miniDate = testNumber.getMiniDate();
            String testNumberUuid = testNumber.getUuid();
            TestNumber testNumber1 = TestNumber.builder()
                    .uuid(testNumberUuid)
                    .build();

            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            String formatedNow = now.format(formatter);
            model.addAttribute("formatedNow", formatedNow);

            int questionList = testedInfo.getQuestionCount();
            int hitCount = testedInfo.getCorrectAnswerCount();
            int score1 = testedInfo.getScore();
            String passMessage = testedInfo.getTestResult();

            String testName = "쪽지";

            model.addAttribute("testName", testName);
            model.addAttribute("questionList", questionList);
            model.addAttribute("hitCount", hitCount);
            model.addAttribute("score1", score1);
            model.addAttribute("passMessage", passMessage);

            MiniTestScore miniTestScore = MiniTestScore.builder()
                    .tested(tested1)
                    .testedDate(miniDate)
                    .testNumber(testNumber1)
                    .score(score1)
                    .user(user1)
                    .build();

            miniTestScoreRepository.save(miniTestScore);
        }
        return "/test/test_result";
    }

    @GetMapping("/test_score_add")
    public String test_score_add(
            Model model
    ) throws ParseException {
        String uuid = (String) session.getAttribute("uuid");
        List<Tested> testedList = testedRepository.findByTestedUuid(uuid);
        Tested tested = testedList.get(0);
        String testedUuid = tested.getTestedUuid();
        User user = tested.getUser();
        String userUsername = user.getUsername();
        Question question = tested.getQuestion();
        TestNumber testNumber = question.getTestNumber();
        String testSubject = testNumber.getSubject().getSubject();

        Optional<TestedInfo> testedInfoOptional = testedInfoRepository.findById(uuid);
        if (testedInfoOptional.isPresent()) {
            TestedInfo testedInfo = testedInfoOptional.get();

            TestStudents testStudents1 = (TestStudents) session.getAttribute("testStudents");
            testStudents1.setTestedInfo(testedInfo);
            testStudentsRepository.save(testStudents1);

            SimpleDateFormat formatter_one = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            SimpleDateFormat formatter_two = new SimpleDateFormat("yyyyMMddHHmmss");
            String inString = String.valueOf(testedInfo.getTestedAt());
            ParsePosition pos = new ParsePosition(0);
            Date frmTime = formatter_one.parse(inString, pos);
            String outString = formatter_two.format(frmTime);
            Date parsedTime = formatter_two.parse(outString);
            Calendar taday = Calendar.getInstance();
            int yyyy = taday.get(Calendar.YEAR);

            Date parsedTime101 = formatter_two.parse(yyyy + "0101000000");
            Date parsedTime131 = formatter_two.parse(yyyy + "0131235959");
            Date parsedTime201 = formatter_two.parse(yyyy + "0201000000");
            Date parsedTime229 = formatter_two.parse(yyyy + "0229235959");
            Date parsedTime301 = formatter_two.parse(yyyy + "0301000000");
            Date parsedTime331 = formatter_two.parse(yyyy + "0331235959");
            Date parsedTime401 = formatter_two.parse(yyyy + "0401000000");
            Date parsedTime430 = formatter_two.parse(yyyy + "0430235959");
            Date parsedTime501 = formatter_two.parse(yyyy + "0501000000");
            Date parsedTime531 = formatter_two.parse(yyyy + "0053235959");
            Date parsedTime601 = formatter_two.parse(yyyy + "0601000000");
            Date parsedTime630 = formatter_two.parse(yyyy + "0630235959");
            Date parsedTime701 = formatter_two.parse(yyyy + "0701000000");
            Date parsedTime731 = formatter_two.parse(yyyy + "0731235959");
            Date parsedTime801 = formatter_two.parse(yyyy + "0801000000");
            Date parsedTime831 = formatter_two.parse(yyyy + "0831235959");
            Date parsedTime901 = formatter_two.parse(yyyy + "0901000000");
            Date parsedTime930 = formatter_two.parse(yyyy + "0931235959");
            Date parsedTime1001 = formatter_two.parse(yyyy + "1001000000");
            Date parsedTime1031 = formatter_two.parse(yyyy + "1031235959");
            Date parsedTime1101 = formatter_two.parse(yyyy + "1101000000");
            Date parsedTime1130 = formatter_two.parse(yyyy + "1130235959");
            Date parsedTime1201 = formatter_two.parse(yyyy + "1201000000");
            Date parsedTime1231 = formatter_two.parse(yyyy + "1231235959");


            Optional<TestScore> TestScoreOptional = testScoreRepository.findByUserUsernameAndTestNumberSubjectSubject(userUsername, testSubject);
            if (TestScoreOptional.isPresent()) {
                TestScore testScore = TestScoreOptional.get();

                if ((parsedTime.after(parsedTime101)) && (!parsedTime.after(parsedTime131))) {

                    testScore.setMonth01(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime201)) && (!parsedTime.after(parsedTime229))) {
                    ;
                    testScore.setMonth02(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime301)) && (!parsedTime.after(parsedTime331))) {

                    testScore.setMonth03(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime401)) && (!parsedTime.after(parsedTime430))) {

                    testScore.setMonth04(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime501)) && (!parsedTime.after(parsedTime531))) {

                    testScore.setMonth05(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime601)) && (!parsedTime.after(parsedTime630))) {

                    testScore.setMonth06(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime701)) && (!parsedTime.after(parsedTime731))) {

                    testScore.setMonth07(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime801)) && (!parsedTime.after(parsedTime831))) {

                    testScore.setMonth08(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime901)) && (!parsedTime.after(parsedTime930))) {

                    testScore.setMonth09(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime1001)) && (!parsedTime.after(parsedTime1031))) {

                    testScore.setMonth10(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime1101)) && (!parsedTime.after(parsedTime1130))) {

                    testScore.setMonth11(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime1201)) && (!parsedTime.after(parsedTime1231))) {

                    testScore.setMonth12(testedInfo.getScore());
                }
                testScore.setUser(user);
                testScore.setTestNumber(testNumber);

                testScoreRepository.save(testScore);
            } else {
                TestScore testScore = new TestScore();

                if ((parsedTime.after(parsedTime101)) && (!parsedTime.after(parsedTime131))) {

                    testScore.setMonth01(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime201)) && (!parsedTime.after(parsedTime229))) {

                    testScore.setMonth02(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime301)) && (!parsedTime.after(parsedTime331))) {

                    testScore.setMonth03(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime401)) && (!parsedTime.after(parsedTime430))) {

                    testScore.setMonth04(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime501)) && (!parsedTime.after(parsedTime531))) {

                    testScore.setMonth05(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime601)) && (!parsedTime.after(parsedTime630))) {

                    testScore.setMonth06(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime701)) && (!parsedTime.after(parsedTime731))) {

                    testScore.setMonth07(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime801)) && (!parsedTime.after(parsedTime831))) {

                    testScore.setMonth08(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime901)) && (!parsedTime.after(parsedTime930))) {

                    testScore.setMonth09(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime1001)) && (!parsedTime.after(parsedTime1031))) {

                    testScore.setMonth10(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime1101)) && (!parsedTime.after(parsedTime1130))) {

                    testScore.setMonth11(testedInfo.getScore());
                }
                if ((parsedTime.after(parsedTime1201)) && (!parsedTime.after(parsedTime1231))) {

                    testScore.setMonth12(testedInfo.getScore());
                }

                testScore.setUser(user);
                testScore.setTestNumber(testNumber);
                testScoreRepository.save(testScore);
            }

            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            String formatedNow = now.format(formatter);
            model.addAttribute("formatedNow", formatedNow);

            String testName = "월말";

            model.addAttribute("testName", testName);

            int questionList = testedInfo.getQuestionCount();
            int hitCount = testedInfo.getCorrectAnswerCount();
            int score1 = testedInfo.getScore();
            String passMessage = testedInfo.getTestResult();

            model.addAttribute("questionList", questionList);
            model.addAttribute("hitCount", hitCount);
            model.addAttribute("score1", score1);
            model.addAttribute("passMessage", passMessage);
        }
        return "test/test_result";
    }

    @GetMapping("/test0")
    public String test0() {
        return "/test/test0";
    }


    @GetMapping("/test1")
    public String test1(
            TestForm testForm,
            Question question,
            Model model
    ) {
        String testNumber1 = (String) session.getAttribute("testNumber1");

        Optional<TestNumber> byTestNumber = testNumberRepository.findByTestNumber(testNumber1);
        if (byTestNumber.isPresent()) {
            TestNumber testNumber = byTestNumber.get();
            String testSubject = testNumber.getSubject().getSubject();


            List<Question> questionList = questionRepository.findAllByTestNumber(testNumber);

            model.addAttribute("questionList", questionList);
            model.addAttribute("testSubject", testSubject);
            session.setAttribute("testNumber", testNumber);
            session.setAttribute("questionList", questionList);
        }
        return "/test/test1";
    }

    @GetMapping("/test2")
    public String test2() {
        return "test/test2";
    }




}










