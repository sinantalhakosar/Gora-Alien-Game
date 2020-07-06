package com.aliengame.server.repository;

import com.aliengame.server.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer>, CustomScoreRepository {
}
