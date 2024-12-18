package com.seojihoon.boardback.dto.response.exerciseDiary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.seojihoon.boardback.dto.response.ResponseCode;
import com.seojihoon.boardback.dto.response.ResponseMessage;
import com.seojihoon.boardback.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class DeleteExerciseDiaryResponseDto extends ResponseDto {
    
    private DeleteExerciseDiaryResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<DeleteExerciseDiaryResponseDto> success() {
        DeleteExerciseDiaryResponseDto result = new DeleteExerciseDiaryResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> error() {
        ResponseDto result = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
} 