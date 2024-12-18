package com.seojihoon.boardback.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seojihoon.boardback.dto.request.exerciseDiary.PatchExerciseDiaryRequestDto;
import com.seojihoon.boardback.dto.request.exerciseDiary.PostExerciseDiaryRequestDto;
import com.seojihoon.boardback.dto.response.exerciseDiary.DeleteExerciseDiaryResponseDto;
import com.seojihoon.boardback.dto.response.exerciseDiary.GetExerciseDiaryResponseDto;
import com.seojihoon.boardback.dto.response.exerciseDiary.GetExerciseDiaryListResponseDto;
import com.seojihoon.boardback.service.ExerciseDiaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/exercise-diary")
@RequiredArgsConstructor
public class ExerciseDiaryController {
    
    private final ExerciseDiaryService exerciseDiaryService;

    // 운동 일지 작성
    @PostMapping("")
    public ResponseEntity<? super GetExerciseDiaryResponseDto> postExerciseDiary(
        @RequestBody @Valid PostExerciseDiaryRequestDto requestBody,
        @AuthenticationPrincipal String email
    ) {
        ResponseEntity<? super GetExerciseDiaryResponseDto> response = 
            exerciseDiaryService.postExerciseDiary(requestBody, email);
        return response;
    }

    // 운동 일지 수정
    @PatchMapping("")
    public ResponseEntity<? super GetExerciseDiaryResponseDto> patchExerciseDiary(
        @RequestBody @Valid PatchExerciseDiaryRequestDto requestBody,
        @AuthenticationPrincipal String email
    ) {
        ResponseEntity<? super GetExerciseDiaryResponseDto> response = 
            exerciseDiaryService.patchExerciseDiary(requestBody, email);
        return response;
    }

    // 특정 날짜의 운동 일지 조회
    @GetMapping("/{date}")
    public ResponseEntity<? super GetExerciseDiaryResponseDto> getExerciseDiary(
        @AuthenticationPrincipal String email,
        @PathVariable("date") String date
    ) {
        ResponseEntity<? super GetExerciseDiaryResponseDto> response = 
            exerciseDiaryService.getExerciseDiary(email, date);
        return response;
    }

    // 특정 기간의 운동 일지 목록 조회
    @GetMapping("/list/{startDate}/{endDate}")
    public ResponseEntity<? super GetExerciseDiaryListResponseDto> getExerciseDiaryList(
        @AuthenticationPrincipal String email,
        @PathVariable("startDate") String startDate,
        @PathVariable("endDate") String endDate
    ) {
        ResponseEntity<? super GetExerciseDiaryListResponseDto> response = 
            exerciseDiaryService.getExerciseDiaryList(email, startDate, endDate);
        return response;
    }

    // 운동 일지 삭제
    @DeleteMapping("/{diaryNumber}")
    public ResponseEntity<? super DeleteExerciseDiaryResponseDto> deleteExerciseDiary(
        @PathVariable("diaryNumber") Integer diaryNumber,
        @AuthenticationPrincipal String email
    ) {
        ResponseEntity<? super DeleteExerciseDiaryResponseDto> response = 
            exerciseDiaryService.deleteExerciseDiary(diaryNumber, email);
        return response;
    }
} 