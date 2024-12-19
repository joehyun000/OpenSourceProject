package com.seojihoon.boardback.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.seojihoon.boardback.entity.CommentEntity;
import com.seojihoon.boardback.repository.resultSet.CommentListResultSet;

@Repository
public interface CommentRespository extends JpaRepository<CommentEntity, Integer> {
    
    @Query(
        value=
        "SELECT " + 
            "C.comment_number AS commentNumber, " +
            "U.nickname AS nickname, " +
            "U.profile_image_url AS profileImage, " +
            "C.contents AS content, " +
            "C.write_datetime AS writeDatetime, " +
            "C.user_email AS userEmail, " +
            "C.deleted AS deleted, " +
            "C.parent_comment_number AS parentCommentNumber " +
        "FROM comment AS C " +
        "INNER JOIN user AS U " +
        "ON C.user_email = U.email " +
        "WHERE C.board_number = ?1 " +
        "ORDER BY " +
            "CASE WHEN C.parent_comment_number IS NULL THEN C.comment_number ELSE C.parent_comment_number END, " +
            "C.parent_comment_number IS NOT NULL, " +
            "C.write_datetime",
        nativeQuery=true
    )
    List<CommentListResultSet> findByCommentList(Integer boardNumber);

    @Query(
        value = 
        "SELECT COUNT(*) " +
        "FROM comment AS C " +
        "WHERE C.parent_comment_number = ?1",
        nativeQuery = true
    )
    Integer countByParentCommentNumber(Integer parentCommentNumber);

    @Transactional
    void deleteByBoardNumber(Integer boardNumber);

}
