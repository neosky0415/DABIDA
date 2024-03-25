package com.example.demo11.repository;

import com.example.demo11.entity.TestNumber;
import com.example.demo11.entity.Tested;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestedRepository extends JpaRepository<Tested, String> {

    @Query(value = "SELECT t.uuid FROM tested t WHERE t.tested_uuid = ?1", nativeQuery = true)
    List<String> findTestedUuidByUuid(String testedUuid);


    @Query(value = "SELECT t.id FROM tested t WHERE t.q_uuid = ?1", nativeQuery = true)
    List<String> findByQuestionUuid(String quuid);

    List<Tested> findAllByTestNumber(TestNumber testNumber);

    List<Tested> findByTestedUuid(String uuid);
}
