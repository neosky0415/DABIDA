package com.example.demo11.repository;


import com.example.demo11.entity.MiniTestStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiniTestStudentsRepository extends JpaRepository<MiniTestStudents, String> {


  Optional<MiniTestStudents> findByUserUsernameAndTestNumberUuid(String username ,String uuid);

  List<MiniTestStudents> findByUserUsername(String username);

  List<MiniTestStudents> findByUserUsernameAndTestedInfoUuidNull(String username);

  List<MiniTestStudents> findByUserUsernameAndTestedInfoUuidNotNull(String username);



}
