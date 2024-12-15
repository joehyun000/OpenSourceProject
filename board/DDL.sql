-- Active: 1732257373510@@127.0.0.1@3306@board_jihoon
CREATE TABLE `user` (
  `email` varchar(50) COLLATE utf8mb3_bin NOT NULL COMMENT '유저 이메일',
  `password` varchar(200) COLLATE utf8mb3_bin NOT NULL COMMENT '암호화된 비밀번호',
  `nickname` varchar(50) COLLATE utf8mb3_bin NOT NULL COMMENT '유저 닉네임',
  `tel_number` varchar(15) COLLATE utf8mb3_bin NOT NULL COMMENT '유저 휴대전화 번호',
  `address` text COLLATE utf8mb3_bin NOT NULL COMMENT '유저 주소',
  `address_detail` text COLLATE utf8mb3_bin COMMENT '유저 상세 주소',
  `agreed_personal` tinyint NOT NULL COMMENT '개인정보 동의 여부',
  `profile_image_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '유저 프로필 사진 URL',
  PRIMARY KEY (`email`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `nickname_UNIQUE` (`nickname`),
  UNIQUE KEY `tel_number_UNIQUE` (`tel_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='사용자 테이블';

CREATE TABLE `board` (
  `board_number` int NOT NULL AUTO_INCREMENT COMMENT '게시물 번호',
  `title` text COLLATE utf8mb3_bin NOT NULL COMMENT '게시물 제목',
  `contents` text COLLATE utf8mb3_bin NOT NULL COMMENT '게시물 내용',
  `write_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '게시물 작성 날짜 및 시간',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '게시물 조회수',
  `writer_email` varchar(50) COLLATE utf8mb3_bin NOT NULL COMMENT '작성자 이메일',
  `comment_count` int NOT NULL DEFAULT '0' COMMENT '댓글 수',
  `favorite_count` int NOT NULL DEFAULT '0' COMMENT '좋아요 수',
  `board_type` VARCHAR(20) NOT NULL DEFAULT 'INFORMATION' COMMENT '게시판 타입(INFORMATION/TEAM)',
  PRIMARY KEY (`board_number`),
  UNIQUE KEY `board_number_UNIQUE` (`board_number`),
  KEY `fk_board_user_idx` (`writer_email`),
  CONSTRAINT `fk_board_user` FOREIGN KEY (`writer_email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='게시물 테이블';

CREATE TABLE `board_image` (
  `board_number` int NOT NULL COMMENT '게시물 번호',
  `image_url` text COLLATE utf8mb3_bin NOT NULL COMMENT '게시물 이미지 URL',
  `sequence` int NOT NULL AUTO_INCREMENT COMMENT '게시물 이미지 ',
  PRIMARY KEY (`sequence`),
  KEY `fk_board_image_board1_idx` (`board_number`),
  CONSTRAINT `fk_board_image_board1` FOREIGN KEY (`board_number`) REFERENCES `board` (`board_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='게시물 이미지 테이블';

CREATE TABLE `comment` (
  `comment_number` int NOT NULL AUTO_INCREMENT COMMENT '댓글 번호',
  `user_email` varchar(50) COLLATE utf8mb3_bin NOT NULL COMMENT '댓글 작성자 이메일',
  `board_number` int NOT NULL COMMENT '댓글 대상 게시물 번호',
  `contents` text COLLATE utf8mb3_bin NOT NULL COMMENT '댓글 내용',
  `write_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '댓글 작성 날짜 및 시간',
  PRIMARY KEY (`comment_number`),
  KEY `comment_user_FK_idx` (`user_email`),
  KEY `comment_board_FK_idx` (`board_number`),
  CONSTRAINT `comment_board_FK` FOREIGN KEY (`board_number`) REFERENCES `board` (`board_number`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comment_user_FK` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='댓글 관계 테이블 (유저 - 게시물)';

CREATE TABLE `favorite` (
  `user_email` varchar(50) COLLATE utf8mb3_bin NOT NULL COMMENT '좋아요한 사용자 이메일',
  `board_number` int NOT NULL COMMENT '대상 게시물 번호',
  PRIMARY KEY (`user_email`,`board_number`),
  KEY `fk_user_has_board_board1_idx` (`board_number`),
  KEY `fk_user_has_board_user1_idx` (`user_email`),
  CONSTRAINT `fk_user_has_board_board1` FOREIGN KEY (`board_number`) REFERENCES `board` (`board_number`),
  CONSTRAINT `fk_user_has_board_user1` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='좋아요 관계 테이블 (유저 - 게시물)';

CREATE TABLE `search_log` (
  `search_word` text COLLATE utf8mb3_bin NOT NULL COMMENT '검색어',
  `relation_word` text COLLATE utf8mb3_bin COMMENT '관련검색어',
  `relation` tinyint(1) NOT NULL COMMENT '연관검색어여부'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='검색 기록 테이블';

CREATE TABLE `team_matching` (
  `team_number` int NOT NULL AUTO_INCREMENT COMMENT '팀 번호',
  `team_leader_nickname` varchar(50) COLLATE utf8mb3_bin NOT NULL COMMENT '팀장 닉네임',
  `team_member_nickname` varchar(50) COLLATE utf8mb3_bin NOT NULL COMMENT '팀원 닉네임',
  `team_member_total_count` int NOT NULL COMMENT '총 팀원 수',
  `team_member_current_count` int NOT NULL COMMENT '현재 팀원 수',
  `team_image_url` text COLLATE utf8mb3_bin NOT NULL COMMENT '오픈채팅 URL',
  PRIMARY KEY (`team_number`),
  KEY `fk_team_matching_user1_idx` (`team_leader_nickname`),
  KEY `fk_team_matching_user2_idx` (`team_member_nickname`),
  CONSTRAINT `fk_team_matching_user1` FOREIGN KEY (`team_leader_nickname`) REFERENCES `user` (`nickname`),
  CONSTRAINT `fk_team_matching_user2` FOREIGN KEY (`team_member_nickname`) REFERENCES `user` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='팀 매칭 테이블';

CREATE VIEW board_view AS
SELECT 
    B.board_number AS board_number,
    B.title AS title,
    B.contents AS content,
    I.board_title_image AS board_title_image,
    B.view_count AS view_count,
    B.favorite_count AS favorite_count,
    B.comment_count AS comment_count,
    B.write_datetime AS write_datetime,
    B.writer_email AS writer_email,
    U.nickname AS writer_nickname,
    U.profile_image_url AS writer_profile_image
FROM board AS B
INNER JOIN user AS U
ON B.writer_email = U.email
LEFT JOIN (
    SELECT board_number, ANY_VALUE(image_url) AS board_title_image
    FROM board_image
    GROUP BY board_number
) AS I
ON B.board_number = I.board_number;

ALTER TABLE `search_log` 
ADD COLUMN `sequence` INT PRIMARY KEY AUTO_INCREMENT COMMENT '로그번호';

CREATE TABLE `exercise_diary` (
  `diary_number` int NOT NULL AUTO_INCREMENT COMMENT '운동일지 번호',
  `user_email` varchar(50) COLLATE utf8mb3_bin NOT NULL COMMENT '작성자 이메일',
  `exercise_date` date NOT NULL COMMENT '운동 날짜',
  `contents` text COLLATE utf8mb3_bin NOT NULL COMMENT '운동 내용',
  `write_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성 날짜 및 시간',
  PRIMARY KEY (`diary_number`),
  UNIQUE KEY `unique_user_date` (`user_email`, `exercise_date`),
  KEY `fk_exercise_diary_user_idx` (`user_email`),
  CONSTRAINT `fk_exercise_diary_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='운동 일지 테이블';

ALTER TABLE `comment` 
ADD COLUMN `parent_comment_number` int NULL COMMENT '부모 댓글 번호' AFTER `write_datetime`,
ADD CONSTRAINT `comment_parent_FK` 
    FOREIGN KEY (`parent_comment_number`) 
    REFERENCES `comment` (`comment_number`) 
    ON DELETE CASCADE;

CREATE TABLE team_board_detail (
    board_number INT NOT NULL COMMENT '게시물 번호',
    team_url TEXT NOT NULL COMMENT '팀 참여 URL',
    PRIMARY KEY (board_number),
    CONSTRAINT fk_team_board_detail
        FOREIGN KEY (board_number) 
        REFERENCES board (board_number)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='팀 게시판 상세 정보';

CREATE OR REPLACE VIEW board_view AS
SELECT 
    B.board_number AS board_number,
    B.title AS title,
    B.contents AS content,
    I.board_title_image AS board_title_image,
    B.view_count AS view_count,
    B.favorite_count AS favorite_count,
    B.comment_count AS comment_count,
    B.write_datetime AS write_datetime,
    B.writer_email AS writer_email,
    B.board_type AS board_type,
    T.team_url AS team_url,
    U.nickname AS writer_nickname,
    U.profile_image_url AS writer_profile_image
FROM board AS B
INNER JOIN user AS U
ON B.writer_email = U.email
LEFT JOIN (
    SELECT board_number, ANY_VALUE(image_url) AS board_title_image
    FROM board_image
    GROUP BY board_number
) AS I
ON B.board_number = I.board_number
LEFT JOIN team_board_detail AS T
ON B.board_number = T.board_number;