package com.seojihoon.boardback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seojihoon.boardback.entity.ExerciseDiaryEntity;

@Repository
public interface ExerciseDiaryRepository extends JpaRepository<ExerciseDiaryEntity, Integer> {
    
    // 특정 사용자의 특정 날짜 운동 기록 조회
    ExerciseDiaryEntity findByUserEmailAndExerciseDate(String userEmail, String exerciseDate);
    
    // 특정 사용자의 모든 운동 기록 조회
    List<ExerciseDiaryEntity> findByUserEmail(String userEmail);
    
    // 특정 사용자의 특정 날짜 운동 기록 존재 여부 확인
    boolean existsByUserEmailAndExerciseDate(String userEmail, String exerciseDate);
    
    // 특정 사용자의 특정 기간 운동 기록 조회
    List<ExerciseDiaryEntity> findByUserEmailAndExerciseDateBetweenOrderByExerciseDateDesc(
        String userEmail, 
        String startDate, 
        String endDate
    );
    
    // 특정 사용자의 특정 운동 일지 삭제를 위한 조회
    ExerciseDiaryEntity findByDiaryNumberAndUserEmail(Integer diaryNumber, String userEmail);
} 