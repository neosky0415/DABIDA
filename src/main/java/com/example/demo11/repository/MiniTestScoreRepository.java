package com.example.demo11.repository;

import com.example.demo11.entity.MiniTestScore;
import com.example.demo11.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MiniTestScoreRepository extends JpaRepository<MiniTestScore, Long> {
    List<MiniTestScore> findByUser(User user);
}
