
package com.example.demo11.charts;

import com.example.demo11.entity.TestScore;
import com.example.demo11.entity.User;
import com.example.demo11.repository.TestScoreRepository;
import com.example.demo11.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChartsController {
    private final TestScoreRepository testScoreRepository;

    private final UserRepository userRepository;
    @GetMapping(value = "/charts")
    public String chartsHome(Model model) {

        return "charts/charts";
    }

    @GetMapping("/chartsList")
    public String chartsList(
        TestScoreForm testScoreForm,
        Model model,
        @PageableDefault(page = 0, size = 10, sort = "uuid", direction = Sort.Direction.DESC)
        Pageable pageable,
        String  searchUsername,
        String searchTestSubject
    ) {

        //        /*검색기능-3*/
        Page<TestScore> list = null;
        /*searchKeyword = 검색하는 단어*/

        if((searchUsername == null && searchTestSubject == null) || (searchUsername == "" && searchTestSubject == "")) {
            list = testScoreRepository.findAll(pageable);
            long count = list.stream().count();
            model.addAttribute("count", count);
//        } else if(searchUsername == "" && searchTitle == ""){
//            list = scoreRepository.findAll(pageable);//기존의 리스트보여줌
//            long count = list.stream().count();
//            model.addAttribute("count", count);
        }else {
            list = testScoreRepository.findByUserUsernameAndTestNumberSubjectSubject(searchUsername, searchTestSubject ,pageable);//검색리스트반환
            long count = list.stream().count();
            model.addAttribute("count", count);
        }

        List<User> userSearchList = userRepository.findAll();
        List<TestScore> newSearchList = testScoreRepository.findAll();
//        List<TestScore> newSearchList = searchList.stream().distinct().collect(Collectors.toList());
        model.addAttribute("userSearchList" ,userSearchList);
        model.addAttribute("newSearchList" ,newSearchList);
        int Page = list.getPageable().getPageNumber() + 1; //pageable에서 넘어온 현재페이지를 가지고올수있다 * 0부터시작하니까 +1
//        int startPage = Math.max(Page - 4, 1); //매개변수로 들어온 두 값을 비교해서 큰값을 반환
//        int endPage = Math.min(Page + 5, list.getTotalPages());

        //BoardService에서 만들어준 boardList가 반환되는데, list라는 이름으로 받아서 넘기겠다는 뜻
        model.addAttribute("userList" , list);
        model.addAttribute("Page", Page);

//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);

        return "charts/user_list";
    }


    @Transactional
    @PostMapping("/charts_admin_edit/{uuid}")
    public String charts_admin_edit (
        @PathVariable String id,
        Model model,
        TestScoreForm testScoreForm
    ) {
        Optional<TestScore> scoreOptional =  testScoreRepository.findById(id);
        if(scoreOptional.isPresent()){
            TestScore testScore = scoreOptional.get();
            BeanUtils.copyProperties(testScoreForm, testScore);

//            score.setRoles(userForm.getRoles());
//            score.setLoginFailureCount(userForm.getLoginFailureCount());
            testScoreRepository.save(testScore);
        } else {

        }
        return "redirect:/chartsList";








    }











}


