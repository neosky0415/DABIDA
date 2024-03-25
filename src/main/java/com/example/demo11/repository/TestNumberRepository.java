package com.example.demo11.repository;

import com.example.demo11.entity.TestNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestNumberRepository extends JpaRepository<TestNumber, String> {
    Optional<TestNumber> findByTestNumber(String testNumber);
    Optional<TestNumber> findByTestTitle(String testTitle);
    Optional<TestNumber> findByMiniTitle(String miniTitle);
    Optional<TestNumber> findByUuid(String uuid);
    List<TestNumber> findByMiniDateIsNotNullOrderByCreatedAtDesc();
    List<TestNumber> findByMiniDateIsNullOrderByCreatedAtDesc();

    }



