package com.example.demo11.controller;


import com.example.demo11.entity.*;
import com.example.demo11.form.TestedForm;
import com.example.demo11.repository.MiniTestStudentsRepository;
import com.example.demo11.repository.TestStudentsRepository;
import com.example.demo11.repository.TestedInfoRepository;
import com.example.demo11.repository.TestedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class TestViewController {

   private final TestedRepository testedRepository;
   private final TestedInfoRepository testedInfoRepository;

    @GetMapping("tested_view/{uuid}")
    public String tested_view(
            @PathVariable String uuid,
            Model model
    ) {
        List<String> uuidList = testedRepository.findTestedUuidByUuid(uuid);
        List <TestedForm> testedList = new ArrayList<TestedForm>();
        for(int i=0; i<uuidList.size(); i++) {
            String uuid1 = uuidList.get(i);
            Optional<Tested> testedOptional = testedRepository.findById(uuid1);
            if(testedOptional.isPresent()) {
                Tested tested = testedOptional.get();
                Question question = tested.getQuestion();
                TestedForm testedForm = new TestedForm()
                        .builder()
                        .title(question.getTitle())
                        .choice1(question.getChoice1())
                        .choice2(question.getChoice2())
                        .choice3(question.getChoice3())
                        .choice4(question.getChoice4())
                        .imgPath(question.getImgPath())
                        .choice1Img(question.getChoice1Img())
                        .choice2Img(question.getChoice2Img())
                        .choice3Img(question.getChoice3Img())
                        .choice4Img(question.getChoice4Img())
                        .correctAnswer(question.getCorrectAnswer())
                        .answer(tested.getAnswer())
                        .build();
                testedList.add(testedForm);


                Optional<TestedInfo> testedInfoOptional = testedInfoRepository.findByUuid(uuid);
                  int correctAnswerCount = testedInfoOptional.get().getCorrectAnswerCount();
                  int questionCount = testedInfoOptional.get().getQuestionCount();
                  int scores = testedInfoOptional.get().getScore();


                  model.addAttribute("correctAnswerCount",correctAnswerCount);
                  model.addAttribute("questionCount",questionCount);
                  model.addAttribute("scores",scores);
            }

        }
        model.addAttribute("testedList" , testedList);
        return "view/tested_view0";
    }


}
