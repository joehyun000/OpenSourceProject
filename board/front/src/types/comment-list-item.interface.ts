export default interface CommentListItem {
    commentNumber: number;
    nickname: string;
    profileImage: string | null;
    writeDatetime: string;
    content: string;
    userEmail: string;
    deleted: boolean;
    parentCommentNumber: number | null;
    replies?: CommentListItem[];  // 대댓글 목록
}