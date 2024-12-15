package com.seojihoon.boardback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seojihoon.boardback.entity.TeamBoardDetailEntity;

@Repository
public interface TeamBoardDetailRepository extends JpaRepository<TeamBoardDetailEntity, Integer> {
    
    TeamBoardDetailEntity findByBoardNumber(Integer boardNumber);
    
    boolean existsByBoardNumber(Integer boardNumber);
} 