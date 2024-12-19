package com.seojihoon.boardback.dto.response.board;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.seojihoon.boardback.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class DeleteCommentResponseDto extends ResponseDto {

    private DeleteCommentResponseDto(String code, String message) {
        super(code, message);
    }

    public static ResponseEntity<DeleteCommentResponseDto> success() {
        DeleteCommentResponseDto result = new DeleteCommentResponseDto("SU", "Success");
        return ResponseEntity.ok(result);
    }

    public static ResponseEntity<ResponseDto> notExistUser() {
        ResponseDto result = new ResponseDto("NU", "Not Exist User");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistComment() {
        ResponseDto result = new ResponseDto("NC", "Not Exist Comment");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> noPermission() {
        ResponseDto result = new ResponseDto("NP", "No Permission");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }
} 