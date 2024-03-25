package com.example.demo11.repository;

import com.example.demo11.entity.Question;
import com.example.demo11.entity.TestNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,String> {

    List<Question> findByUsername(String username);

    Page<Question> findAll(Pageable pageable);

    List<Question> findByTestNumberUuidOrderByCreatedAtDesc(String uuid);

    List<Question> findAllByTestNumber(TestNumber testNumber);
}
