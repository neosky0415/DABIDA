package com.example.demo11.controller;

import com.example.demo11.entity.MiniTestStudents;
import com.example.demo11.entity.TestStudents;
import com.example.demo11.entity.TestedInfo;
import com.example.demo11.repository.MiniTestStudentsRepository;
import com.example.demo11.repository.TestStudentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MyStudyController {

    private final TestStudentsRepository testStudentsRepository;
    private final MiniTestStudentsRepository miniTestStudentsRepository;


    @GetMapping("/my_mini_test")
    public String my_test(
            Principal principal,
            Model model
    ){
        String name = principal.getName();
        List<MiniTestStudents> miniTestStudentsMyList = miniTestStudentsRepository.findByUserUsernameAndTestedInfoUuidNull(name);
        long miniCount = miniTestStudentsMyList.stream().count();

        model.addAttribute("miniCount",miniCount);
        model.addAttribute("miniTestStudentsMyList", miniTestStudentsMyList);
        return "study/my_mini_test";
    }

    @GetMapping("/my_test")
    public String my_test_list(
            Principal principal,
            Model model
    ){
        String name = principal.getName();
        List<TestStudents> testStudentsMyList = testStudentsRepository.findByUserUsernameAndTestedInfoUuidNull(name);
        long testCount = testStudentsMyList.stream().count();

        model.addAttribute("testCount",testCount);
        model.addAttribute("testStudentsMyList", testStudentsMyList);
        return "study/my_test";
    }

    @GetMapping("/my_mini_test_score")
    public String my_mini_test_score(
            Principal principal,
            TestedInfo testedInfo,
            Model model
    ){
        String name = principal.getName();
        List<MiniTestStudents> miniSummitStudentsMyList = miniTestStudentsRepository.findByUserUsernameAndTestedInfoUuidNotNull(name);

        miniSummitStudentsMyList.forEach(miniTestStudents -> {
            TestedInfo testedInfo1 = miniTestStudents.getTestedInfo();
            Date testedAt = testedInfo1.getTestedAt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
            String formatTestedAt = simpleDateFormat.format(testedAt);

            model.addAttribute("formatTestedAt",formatTestedAt);


        });

        model.addAttribute("miniSummitStudentsMyList", miniSummitStudentsMyList);
        return "study/my_mini_test_score";
    }

    @GetMapping("/my_test_score")
    public String my_test_score(
            Principal principal,
            TestedInfo testedInfo,
            Model model
    ){
        String name = principal.getName();
        List<TestStudents> summitStudentsMyList = testStudentsRepository.findByUserUsernameAndTestedInfoUuidNotNull(name);
        summitStudentsMyList.forEach(testStudents -> {
            TestedInfo testedInfo1 = testStudents.getTestedInfo();
            Date testedAt = testedInfo1.getTestedAt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
            String formatTestedAt1 = simpleDateFormat.format(testedAt);

            model.addAttribute("formatTestedAt1",formatTestedAt1);
        } );

        model.addAttribute("summitStudentsMyList", summitStudentsMyList);
        return "study/my_test_score";
    }

}
