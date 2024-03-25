package com.example.demo11.repository;

import com.example.demo11.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User>findByUsername(String username);

    Page<User> findAll(Pageable pageable);
    Page<User> findByRoles(String searchRoles, Pageable pageable);
    Optional<User>findByRealNameAndPhoneNum(String realName, String phoneNum);

    List<User> findByRolesAndTeamName(String roles, String teamName);
    List<User> findByTeamName(String teaName);
    List<User> findBySubject(String subject);

    List<User> findBySubjectAndRoles(String subject, String roles);
}
