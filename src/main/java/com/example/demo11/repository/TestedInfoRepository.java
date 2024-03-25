package com.example.demo11.repository;


import com.example.demo11.entity.TestedInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestedInfoRepository extends JpaRepository <TestedInfo,String> {


    Optional<TestedInfo> findByUserUsernameAndTestNumberTestNumber(String username, String testNumber);

    Optional<TestedInfo> findByUuid(String uuid);

}
