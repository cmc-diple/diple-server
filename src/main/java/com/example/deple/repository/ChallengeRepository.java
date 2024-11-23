package com.example.deple.repository;

import com.example.deple.entity.Challenge;
import com.example.deple.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findByNumber(Integer day);
    List<Challenge> findByStatus(Status status);
}
