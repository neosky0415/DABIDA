package com.example.demo11.repository;

import com.example.demo11.entity.Subject;
import com.example.demo11.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {

    Optional<Subject> findBySubject(String subject);
    List<Subject> findAllByUuid(String uuid);
}
