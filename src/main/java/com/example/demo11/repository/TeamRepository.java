package com.example.demo11.repository;

import com.example.demo11.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    Optional<Team> findByTeamName(String team);
    List<Team> findAllByUuid(String uuid);

}
