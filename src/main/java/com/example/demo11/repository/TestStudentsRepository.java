package com.example.demo11.repository;


import com.example.demo11.entity.TestStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestStudentsRepository extends JpaRepository<TestStudents, String> {


  Optional<TestStudents> findByUserUsernameAndTestNumberUuid(String username ,String uuid);

  List<TestStudents> findByUserUsername(String username);


  List<TestStudents> findByUserUsernameAndTestedInfoUuidNull(String username);

  List<TestStudents> findByUserUsernameAndTestedInfoUuidNotNull(String username);



}
