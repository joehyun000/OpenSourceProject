package com.seojihoon.boardback.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="team_board_detail")
@Table(name="team_board_detail")
public class TeamBoardDetailEntity {
    
    @Id
    private int boardNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "board_number")
    private BoardEntity board;

    private String teamUrl;

    public TeamBoardDetailEntity(BoardEntity board, String teamUrl) {
        this.boardNumber = board.getBoardNumber();
        this.board = board;
        this.teamUrl = teamUrl;
    }

    public void updateTeamUrl(String teamUrl) {
        this.teamUrl = teamUrl;
    }
} 