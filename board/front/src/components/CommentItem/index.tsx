import React, { useState } from 'react';
import './style.css';
import { CommentListItem } from 'types';
import DefaultProfileImage from 'assets/default-profile-image.png';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';

// dayjs 플러그인 설정
dayjs.extend(utc);
dayjs.extend(timezone);
dayjs.tz.setDefault('Asia/Seoul');

//          interface: 댓글 리스트 아이템 컴포넌트 Props         //
interface Props {
  commentItem: CommentListItem;
  onReplySubmit: (content: string, parentCommentNumber: number) => void;
  onDeleteComment: (commentNumber: number) => void;
  currentUserEmail: string | undefined;
  depth?: number;
}

//          component: 댓글 리스트 아이템 컴포넌트          //
export default function CommentItem({ 
  commentItem, 
  onReplySubmit, 
  onDeleteComment,
  currentUserEmail,
  depth = 0
}: Props) {

  //          state: Properties          //
  const { 
    commentNumber, 
    content, 
    profileImage, 
    writeDatetime, 
    nickname, 
    replies,
    userEmail,  // 댓글 작성자 이메일
    deleted    // 삭제 여부
  } = commentItem;
  const [showReplyInput, setShowReplyInput] = useState<boolean>(false);
  const [replyContent, setReplyContent] = useState<string>('');
  const [showReplies, setShowReplies] = useState<boolean>(false);
  const [showMore, setShowMore] = useState<boolean>(false);

  const isMyComment = currentUserEmail === userEmail;

  //          event handler: 대댓글 버튼 클릭 이벤트 처리          //
  const handleReplyClick = () => {
    setShowReplyInput(!showReplyInput);
  };

  //          event handler: 대댓글 내용 변경 이벤트 처리          //
  const handleReplyContentChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    setReplyContent(event.target.value);
  };

  //          event handler: 대댓글 제출 이벤트 처리          //
  const handleSubmitReply = () => {
    if (!replyContent.trim()) return;
    onReplySubmit(replyContent, commentNumber);
    setReplyContent('');
    setShowReplyInput(false);
  };

  //          event handler: 삭제 버튼 클릭 이벤트 처리          //
  const handleDeleteClick = () => {
    onDeleteComment(commentNumber);
  };

  //          function: 작성일 경과시간 함수          //
  const getElapsedTime = () => {
    const now = dayjs().add(9, 'hour');
    const writeTime = dayjs(writeDatetime);

    const gap = now.diff(writeTime, 's');
    if (gap < 60) return `${gap}초 전`;
    if (gap < 3600) return `${Math.floor(gap/60)}분 전`;
    if (gap < 86400) return `${Math.floor(gap/3600)}시간 전`;
    return `${Math.floor(gap/86400)}일 전`;
  };

  //          render: 댓글 리스트 아이템 컴포넌트 렌더링          //
  return (
    <div className={`comment-list-item-box ${depth > 0 ? 'reply-item' : ''}`}>
      <div className='comment-list-item-top'>
        <div className='comment-list-item-profile-box'>
          <div 
            className='comment-list-item-profile-image' 
            style={{ backgroundImage: `url(${profileImage || DefaultProfileImage})` }}
          />
        </div>
        <div className='comment-list-item-nickname'>{nickname}</div>
        <div className='comment-list-item-time'>{getElapsedTime()}</div>
        {isMyComment && !deleted && (
          <div className='more-button-box'>
            <div className='icon-button' onClick={() => setShowMore(!showMore)}>
              <div className='more-icon'></div>
            </div>
            {showMore && (
              <div className='more-box'>
                <div className='more-item' onClick={handleDeleteClick}>
                  <div className='more-item-text'>삭제</div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
      <div className='comment-list-item-main'>
        {deleted ? (
          <div className="deleted-comment">삭제된 댓글입니다.</div>
        ) : (
          <div className='comment-list-item-content'>{content}</div>
        )}

        <div className='comment-actions'>
          {!deleted && depth === 0 && (
            <div className='comment-list-item-reply-button' onClick={handleReplyClick}>
              답글 달기
            </div>
          )}
          {depth === 0 && replies && replies.length > 0 && (
            <div className='comment-list-item-toggle-replies' onClick={() => setShowReplies(!showReplies)}>
              {showReplies ? '답글 숨기기' : `답글 ${replies.length}개 보기`}
            </div>
          )}
        </div>

        {showReplyInput && !deleted && (
          <div className='reply-input-container'>
            <textarea
              className='reply-input'
              placeholder='답글을 입력해주세요.'
              value={replyContent}
              onChange={handleReplyContentChange}
            />
            <div className='reply-button-group'>
              <button 
                className={`reply-submit-button ${replyContent.trim() ? 'active' : ''}`}
                onClick={handleSubmitReply}
              >
                답글달기
              </button>
            </div>
          </div>
        )}
      </div>

      {replies && replies.length > 0 && showReplies && (
        <div className='replies-container'>
          {replies.map((reply: CommentListItem) => (
            <CommentItem 
              key={reply.commentNumber}
              commentItem={reply}
              onReplySubmit={onReplySubmit}
              onDeleteComment={onDeleteComment}
              currentUserEmail={currentUserEmail}
              depth={depth + 1}
            />
          ))}
        </div>
      )}
    </div>
  )
}
