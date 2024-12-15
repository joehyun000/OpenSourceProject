package com.seojihoon.boardback.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import com.seojihoon.boardback.common.BoardType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="board_view")
@Table(name="board_view")
public class BoardViewEntity {
    @Id
    private int boardNumber;
    private String title;
    private String content;
    private String boardTitleImage;
    private int viewCount;
    private int favoriteCount;
    private int commentCount;
    private String writeDatetime;
    private String writerEmail;
    private String writerNickname;
    private String writerProfileImage;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;
    private String teamUrl;  // null for non-team boards
}
