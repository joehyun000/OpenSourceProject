import React from 'react';
import './style.css';
import DefaultProfileImage from 'assets/default-profile-image.png';
import { BoardListItem } from 'types';
import { useNavigate } from 'react-router-dom';
import { BOARD_DETAIL_PATH } from 'constant';
import { cutString, getWriteDatetimeFormat } from 'utils';

//          interface: 게시물 리스트 아이템 컴포넌트 Props          //
interface Props {
  boardItem: BoardListItem;
}

//          component: 게시물 리스트 아이템 컴포넌트          //
export default function BoardItem({ boardItem }: Props) {

  //          state: Properties          //
  const { 
    boardNumber, title, content, boardTitleImage,
    viewCount, commentCount, favoriteCount,
    writeDatetime, writerNickname, writerProfileImage,
    boardType, teamUrl  // 추가된 필드들
  } = boardItem;

  //          function: 네비게이트 함수          //
  const navigator = useNavigate();

  //          event handler: Card Click 이벤트 처리 함수          //
  const onCardClickHandler = () => {
    navigator(BOARD_DETAIL_PATH(boardNumber));
  }

  //          event handler: 팀 ��여 버튼 클릭 이벤트 처리          //
  const onTeamJoinClickHandler = (event: React.MouseEvent, url: string) => {
    event.stopPropagation();  // 카드 클릭 이벤트 전파 방지
    const fullUrl = url.startsWith('http') ? url : `https://${url}`;
    window.open(fullUrl, '_blank', 'noopener,noreferrer');
  };

  //          render: 게시물 리스트 아이템 컴포넌트 렌더링         //
  return (
    <div className='board-list-item-card' onClick={onCardClickHandler}>
      <div className='board-list-item-main-box'>
        <div className='board-list-item-top'>
          <div className='board-list-item-profile-box'>
            <div className='board-list-item-profile-image' style={{ backgroundImage: `url(${writerProfileImage ? writerProfileImage : DefaultProfileImage})` }}></div>
          </div>
          <div className='board-list-item-write-box'>
            <div className='board-list-item-nickname'>{writerNickname}</div>
            <div className='board-list-item-write-date'>{getWriteDatetimeFormat(writeDatetime)}</div>
          </div>
          <div className='board-list-item-type'>
            {boardType === 'TEAM' ? '팀 게시판' : '정보 공유'}
          </div>
        </div>
        <div className='board-list-item-middle'>
          <div className='board-list-item-title'>{cutString(title, 50)}</div>
          <div className='board-list-item-contents'>{cutString(content, 130)}</div>
        </div>
        <div className='board-list-item-bottom'>
          <div className='board-list-item-counts'>
            {`댓글 ${commentCount} · 좋아요 ${favoriteCount} · 조회수 ${viewCount}`}
          </div>
          {boardType === 'TEAM' && teamUrl && (
            <button 
              className='board-list-item-team-button'
              onClick={(e) => onTeamJoinClickHandler(e, teamUrl)}
            >
              팀 참여하기
            </button>
          )}
        </div>
      </div>
      { boardTitleImage !== null && (
        <div className='board-list-item-image-box'>
          <div className='board-list-item-image' style={{ backgroundImage: `url(${boardTitleImage})` }}></div>
        </div>
      ) }
    </div>
  )
}
