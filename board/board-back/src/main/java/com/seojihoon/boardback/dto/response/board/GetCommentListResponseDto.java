package com.seojihoon.boardback.dto.response.board;

import java.util.List;

import com.seojihoon.boardback.common.object.CommentListItem;
import com.seojihoon.boardback.dto.response.ResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
public class GetCommentListResponseDto extends ResponseDto {
    private List<CommentListItem> commentList;

    public GetCommentListResponseDto(String code, String message, List<CommentListItem> commentList) {
        super(code, message);
        this.commentList = commentList;
    }

    public static ResponseEntity<GetCommentListResponseDto> success(List<CommentListItem> commentList) {
        GetCommentListResponseDto result = new GetCommentListResponseDto("SU", "Success", commentList);
        return ResponseEntity.ok(result);
    }

    public static ResponseEntity<ResponseDto> noBoard() {
        ResponseDto result = new GetCommentListResponseDto("NB", "Not Exist Board", null);
        return ResponseEntity.badRequest().body(result);
    }
}
