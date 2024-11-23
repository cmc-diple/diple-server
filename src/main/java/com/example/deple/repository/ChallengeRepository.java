package com.example.deple.repository;

import com.example.deple.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findByNumber(Integer day);

    boolean existsByNumber(Integer day);
}
