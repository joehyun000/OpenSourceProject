package com.seojihoon.boardback.common.object;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.seojihoon.boardback.repository.resultSet.CommentListResultSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentListItem {
    private Integer commentNumber;
    private String nickname;
    private String content;
    private String writeDatetime;
    private String profileImage;
    private String userEmail;
    private boolean deleted;
    private Integer parentCommentNumber;
    private List<CommentListItem> replies;

    public CommentListItem(CommentListResultSet resultSet) {
        this.commentNumber = resultSet.getCommentNumber();
        this.nickname = resultSet.getNickname();
        this.content = resultSet.getContent();
        this.writeDatetime = resultSet.getWriteDatetime();
        this.profileImage = resultSet.getProfileImage();
        this.userEmail = resultSet.getUserEmail();
        this.deleted = resultSet.getDeleted();
        this.parentCommentNumber = resultSet.getParentCommentNumber();
        this.replies = new ArrayList<>();
    }

    public static List<CommentListItem> getList(List<CommentListResultSet> resultSets) {
        List<CommentListItem> topLevelComments = new ArrayList<>();
        Map<Integer, CommentListItem> commentMap = new HashMap<>();

        // 모든 댓글을 생성하고 맵에 저장
        for (CommentListResultSet resultSet: resultSets) {
            CommentListItem comment = new CommentListItem(resultSet);
            commentMap.put(comment.getCommentNumber(), comment);

            // 부모 댓글이 없으면 최상위 댓글 리스트에 추가
            if (comment.getParentCommentNumber() == null) {
                topLevelComments.add(comment);
            }
        }

        // 대댓글들을 부모 댓글의 replies에 추가
        for (CommentListItem comment : commentMap.values()) {
            if (comment.getParentCommentNumber() != null) {
                CommentListItem parentComment = commentMap.get(comment.getParentCommentNumber());
                if (parentComment != null) {
                    parentComment.getReplies().add(comment);
                }
            }
        }

        return topLevelComments;
    }
}
