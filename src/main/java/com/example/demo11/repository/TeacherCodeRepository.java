package com.example.demo11.repository;

import com.example.demo11.entity.TeacherCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherCodeRepository extends JpaRepository<TeacherCode, Long> {
    Optional<TeacherCode> findByTeacherCode(String teacherCode);
}
