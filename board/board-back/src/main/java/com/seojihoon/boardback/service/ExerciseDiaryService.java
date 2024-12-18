package com.seojihoon.boardback.service;

import org.springframework.http.ResponseEntity;

import com.seojihoon.boardback.dto.request.exerciseDiary.PatchExerciseDiaryRequestDto;
import com.seojihoon.boardback.dto.request.exerciseDiary.PostExerciseDiaryRequestDto;
import com.seojihoon.boardback.dto.response.exerciseDiary.DeleteExerciseDiaryResponseDto;
import com.seojihoon.boardback.dto.response.exerciseDiary.GetExerciseDiaryResponseDto;
import com.seojihoon.boardback.dto.response.exerciseDiary.GetExerciseDiaryListResponseDto;

public interface ExerciseDiaryService {
    
    // 운동 일지 작성
    ResponseEntity<? super GetExerciseDiaryResponseDto> postExerciseDiary(PostExerciseDiaryRequestDto dto, String email);
    
    // 운동 일지 수정
    ResponseEntity<? super GetExerciseDiaryResponseDto> patchExerciseDiary(PatchExerciseDiaryRequestDto dto, String email);
    
    // 특정 날짜의 운동 일지 조회
    ResponseEntity<? super GetExerciseDiaryResponseDto> getExerciseDiary(String email, String date);
    
    // 특정 기간의 운동 일지 목록 조회
    ResponseEntity<? super GetExerciseDiaryListResponseDto> getExerciseDiaryList(String email, String startDate, String endDate);
    
    // 운동 일지 삭제
    ResponseEntity<? super DeleteExerciseDiaryResponseDto> deleteExerciseDiary(Integer diaryNumber, String email);
} 