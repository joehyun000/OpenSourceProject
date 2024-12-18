package com.seojihoon.boardback.service.implement;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.seojihoon.boardback.dto.request.exerciseDiary.PatchExerciseDiaryRequestDto;
import com.seojihoon.boardback.dto.request.exerciseDiary.PostExerciseDiaryRequestDto;
import com.seojihoon.boardback.dto.response.ResponseDto;
import com.seojihoon.boardback.dto.response.exerciseDiary.DeleteExerciseDiaryResponseDto;
import com.seojihoon.boardback.dto.response.exerciseDiary.GetExerciseDiaryResponseDto;
import com.seojihoon.boardback.dto.response.exerciseDiary.GetExerciseDiaryListResponseDto;
import com.seojihoon.boardback.entity.ExerciseDiaryEntity;
import com.seojihoon.boardback.entity.UserEntity;
import com.seojihoon.boardback.repository.ExerciseDiaryRepository;
import com.seojihoon.boardback.repository.UserRepository;
import com.seojihoon.boardback.service.ExerciseDiaryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExerciseDiaryServiceImplement implements ExerciseDiaryService {

    private final UserRepository userRepository;
    private final ExerciseDiaryRepository exerciseDiaryRepository;

    @Override
    public ResponseEntity<? super GetExerciseDiaryResponseDto> postExerciseDiary(PostExerciseDiaryRequestDto dto, String email) {
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return ResponseDto.validationFailed();

            ExerciseDiaryEntity exerciseDiaryEntity = new ExerciseDiaryEntity(email, dto.getExerciseDate(), dto.getContents());
            exerciseDiaryRepository.save(exerciseDiaryEntity);

            return ResponseEntity.ok(new GetExerciseDiaryResponseDto(exerciseDiaryEntity));
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetExerciseDiaryResponseDto> patchExerciseDiary(PatchExerciseDiaryRequestDto dto, String email) {
        try {
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return ResponseDto.validationFailed();

            ExerciseDiaryEntity exerciseDiaryEntity = exerciseDiaryRepository.findByUserEmailAndExerciseDate(email, dto.getExerciseDate());
            if (exerciseDiaryEntity == null) return ResponseDto.validationFailed();

            exerciseDiaryEntity.updateContents(dto.getContents());
            exerciseDiaryRepository.save(exerciseDiaryEntity);

            return ResponseEntity.ok(new GetExerciseDiaryResponseDto(exerciseDiaryEntity));

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetExerciseDiaryResponseDto> getExerciseDiary(String email, String date) {
        try {
            ExerciseDiaryEntity exerciseDiaryEntity = exerciseDiaryRepository.findByUserEmailAndExerciseDate(email, date);
            if (exerciseDiaryEntity == null) return ResponseDto.validationFailed();

            return ResponseEntity.ok(new GetExerciseDiaryResponseDto(exerciseDiaryEntity));

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetExerciseDiaryListResponseDto> getExerciseDiaryList(String email, String startDate, String endDate) {
        try {
            List<ExerciseDiaryEntity> exerciseDiaryEntities = 
                exerciseDiaryRepository.findByUserEmailAndExerciseDateBetweenOrderByExerciseDateDesc(email, startDate, endDate);

            return ResponseEntity.ok(new GetExerciseDiaryListResponseDto(exerciseDiaryEntities));

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super DeleteExerciseDiaryResponseDto> deleteExerciseDiary(Integer diaryNumber, String email) {
        try {
            // 사용자 존재 여부 확인
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return ResponseDto.validationFailed();

            // 운동 일지 조회 및 권한 확인
            ExerciseDiaryEntity exerciseDiaryEntity = 
                exerciseDiaryRepository.findByDiaryNumberAndUserEmail(diaryNumber, email);
            if (exerciseDiaryEntity == null) return ResponseDto.validationFailed();

            // 운동 일지 삭제
            exerciseDiaryRepository.delete(exerciseDiaryEntity);

            return DeleteExerciseDiaryResponseDto.success();

        } catch (Exception exception) {
            exception.printStackTrace();
            return DeleteExerciseDiaryResponseDto.error();
        }
    }
} 