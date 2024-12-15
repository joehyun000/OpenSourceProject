package com.seojihoon.boardback.common;

public enum BoardType {
    INFORMATION,    // 정보 공유 게시판
    TEAM;          // 팀 게시판

    public boolean isTeamBoard() {
        return this == TEAM;
    }
} 