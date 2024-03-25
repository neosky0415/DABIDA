package com.example.demo11.repository;

import com.example.demo11.entity.TestScore;
import com.example.demo11.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestScoreRepository extends JpaRepository<TestScore, String> {



    Page<TestScore> findAll(Pageable pageable);

    Optional<TestScore> findByUserUsernameAndTestNumberSubjectSubject(String username, String subject);

    Page<TestScore>findByUserUsernameAndTestNumberSubjectSubject(String searchUsername, String searchTestSubject,  Pageable pageable);
   List<TestScore> findByUser(User user);

}
