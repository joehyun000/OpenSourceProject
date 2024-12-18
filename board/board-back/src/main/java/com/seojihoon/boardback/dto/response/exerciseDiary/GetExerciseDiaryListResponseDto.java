package com.seojihoon.boardback.dto.response.exerciseDiary;

import java.util.List;

import com.seojihoon.boardback.dto.response.ResponseDto;
import com.seojihoon.boardback.entity.ExerciseDiaryEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetExerciseDiaryListResponseDto extends ResponseDto {
    
    private List<ExerciseDiary> exerciseDiaryList;

    public GetExerciseDiaryListResponseDto(List<ExerciseDiaryEntity> exerciseDiaryEntities) {
        super("SU", "Success");
        
        System.out.println("Converting entities to DTOs:");
        exerciseDiaryEntities.forEach(entity -> 
            System.out.println("Entity: " + entity.getDiaryNumber() + ", " + entity.getExerciseDate())
        );
        
        this.exerciseDiaryList = exerciseDiaryEntities.stream()
            .map(entity -> {
                ExerciseDiary dto = new ExerciseDiary(entity);
                System.out.println("Converted DTO: " + dto.getDiaryNumber() + ", " + dto.getExerciseDate());
                return dto;
            })
            .toList();
    }

    @Getter
    @NoArgsConstructor
    public static class ExerciseDiary {
        private int diaryNumber;
        private String exerciseDate;
        private String contents;
        private String writeDatetime;

        public ExerciseDiary(ExerciseDiaryEntity exerciseDiaryEntity) {
            this.diaryNumber = exerciseDiaryEntity.getDiaryNumber();
            this.exerciseDate = exerciseDiaryEntity.getExerciseDate();
            this.contents = exerciseDiaryEntity.getContents();
            this.writeDatetime = exerciseDiaryEntity.getWriteDatetime();
        }
    }
} 