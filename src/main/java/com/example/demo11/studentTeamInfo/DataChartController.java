package com.example.demo11.studentTeamInfo;


import com.example.demo11.entity.TestScore;
import com.example.demo11.repository.SubjectRepository;
import com.example.demo11.repository.TestScoreRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DataChartController {

    private final TestScoreRepository testScoreRepository;
    private final SubjectRepository subjectRepository;
    private final HttpSession session;

    @GetMapping(value = "/rest/score-data")
    public List<Map<String, Object>> getColumnData(
            DataChartsForm dataChartsForm
    ) {
        List<Map<String, Object>> columnData = new ArrayList<>();

        Map<String, Object> headerElement = new HashMap<>();
        headerElement.put("data", new String[]{"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"});
        columnData.add(headerElement);

        String student = (String) session.getAttribute("name");
        String subject1 = (String) session.getAttribute("sub1");
        String subject2 = (String) session.getAttribute("sub2");
        String subject3 = (String) session.getAttribute("sub3");

        Map<String, Object> dataElement1 = new HashMap<>();
        Optional<TestScore> scoreOptional = testScoreRepository.findByUserUsernameAndTestNumberSubjectSubject(student,subject1);
        if (scoreOptional.isPresent()) {
            TestScore testScore = scoreOptional.get();
        }

        if(subject1!="" && scoreOptional.isPresent()){
        String [] name = {scoreOptional.get().getTestNumber().getSubject().getSubject()};
        Integer [] pieData = {scoreOptional.get().getMonth01(),
                             scoreOptional.get().getMonth02(),
                             scoreOptional.get().getMonth03(),
                             scoreOptional.get().getMonth04(),
                scoreOptional.get().getMonth05(),
                scoreOptional.get().getMonth06(),
                scoreOptional.get().getMonth07(),
                scoreOptional.get().getMonth08(),
                scoreOptional.get().getMonth09(),
                scoreOptional.get().getMonth10(),
                scoreOptional.get().getMonth11(),
                scoreOptional.get().getMonth12()};
              dataElement1.put("name", name);
              dataElement1.put("data", pieData);
        } else {
              dataElement1.put("name", "없음");
              dataElement1.put("data", new Integer[]{10,90,89,100,100,30,40,90,88,19, 67, 88});
        }
        columnData.add(dataElement1);

        Map<String, Object> dataElement2 = new HashMap<>();
        Optional<TestScore> scoreOptional1 = testScoreRepository.findByUserUsernameAndTestNumberSubjectSubject(student, subject2);
        if (scoreOptional1.isPresent()) {
            TestScore testScore= scoreOptional1.get();
        }
        if(subject2!="" && scoreOptional1.isPresent()) {
            String[] name1 = {scoreOptional1.get().getTestNumber().getSubject().getSubject()};
            Integer[] pieData1 = {scoreOptional1.get().getMonth01(),
                    scoreOptional1.get().getMonth02(),
                    scoreOptional1.get().getMonth03(),
                    scoreOptional1.get().getMonth04(),
                    scoreOptional1.get().getMonth05(),
                    scoreOptional1.get().getMonth06(),
                    scoreOptional1.get().getMonth07(),
                    scoreOptional1.get().getMonth08(),
                    scoreOptional1.get().getMonth09(),
                    scoreOptional1.get().getMonth10(),
                    scoreOptional1.get().getMonth11(),
                    scoreOptional1.get().getMonth12()};
            dataElement2.put("name",name1 );
            dataElement2.put("data", pieData1  );

        }else{
            dataElement2.put("name", "없음");
            dataElement2.put("data", new Integer[] {0,0,0,0,0,0,0,0,0,0,0,0} );
        }
        columnData.add(dataElement2);


        Map<String, Object> dataElement3 = new HashMap<>();
        Optional<TestScore> scoreOptional2 = testScoreRepository.findByUserUsernameAndTestNumberSubjectSubject(student,subject3);
        if (scoreOptional2.isPresent()) {
            TestScore testScore= scoreOptional2.get();
        }
        if(subject3!="" && scoreOptional2.isPresent()) {
            String[] name2 = {scoreOptional2.get().getTestNumber().getSubject().getSubject()};
            Integer[] pieData2 = {scoreOptional2.get().getMonth01(),
                    scoreOptional2.get().getMonth02(),
                    scoreOptional2.get().getMonth03(),
                    scoreOptional2.get().getMonth04(),
                    scoreOptional2.get().getMonth05(),
                    scoreOptional2.get().getMonth06(),
                    scoreOptional2.get().getMonth07(),
                    scoreOptional2.get().getMonth08(),
                    scoreOptional2.get().getMonth09(),
                    scoreOptional2.get().getMonth10(),
                    scoreOptional2.get().getMonth11(),
                    scoreOptional2.get().getMonth12()};

            dataElement3.put("name",name2);
            dataElement3.put("data",pieData2 );

        }else {
            dataElement3.put("name", "없음");
            dataElement3.put("data", new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0});
        }
        columnData.add(dataElement3);
        return columnData;
    }
}
