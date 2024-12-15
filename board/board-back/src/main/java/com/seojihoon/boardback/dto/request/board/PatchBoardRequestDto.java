package com.seojihoon.boardback.dto.request.board;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.seojihoon.boardback.common.BoardType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchBoardRequestDto {
    @NotBlank
    private String title;
    
    @NotBlank
    private String content;
    
    @NotNull 
    private BoardType boardType;
    
    private String teamUrl;  // optional, only for TEAM type
    
    @NotNull
    private List<String> boardImageList;

    // teamUrl 유효성 검증 메서드
    public boolean validateTeamBoard() {
        if (boardType == BoardType.TEAM) {
            return teamUrl != null && !teamUrl.trim().isEmpty();
        }
        return true;
    }
}
