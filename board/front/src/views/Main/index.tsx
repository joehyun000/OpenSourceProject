import { useEffect, useState } from 'react';
import './style.css';
import { BoardListItem } from 'types';
import { BoardType } from 'types/board.interface';
import { currentBoardListMock, popularWordListMock, top3ListMock } from 'mocks';
import Top3ListItem from 'components/Top3ListItem';
import { useNavigate } from 'react-router-dom';
import { SEARCH_PATH } from 'constant';
import BoardItem from 'components/BoardItem';
import Pagination from 'components/Pagination';
import { usePagination } from 'hooks';
import { getLatestBoardListRequest, getPopularListRequest, getTop3BoardListRequest } from 'apis';
import GetLatestBoardListResponseDto from 'apis/dto/response/board/get-latest-board-list.response.dto';
import ResponseDto from 'apis/dto/response';
import { GetTop3BoardListResponseDto } from 'apis/dto/response/board';
import { GetPopularListResponseDto } from 'apis/dto/response/search';
import { useUserStore } from 'stores';
import { useCookies } from 'react-cookie';
import { BOARD_WRITE_PATH } from 'constant';

//          component: 메인 페이지          //
export default function Main() {

  //          component: 메인 상단 컴포넌트          //
  const MainTop = () => {

    //          state: 주간 Top3 게시물 리스트 상태          //
    const [top3List, setTop3List] = useState<BoardListItem[]>([]);

    //          function: get top 3 board list response 처리 함수          //
    const getTop3BoardListResponse = (responseBody: GetTop3BoardListResponseDto | ResponseDto) => {
      const { code } = responseBody;
      if (code === 'DBE') alert('데이터베이스 오류입니다.');
      if (code !== 'SU') return;

      const { top3List } = responseBody as GetTop3BoardListResponseDto;
      setTop3List(top3List);
    }

    //          effect: 컴포넌트 마운트 시 top3 리스트 불러오기          //
    useEffect(() => {
      getTop3BoardListRequest().then(getTop3BoardListResponse);
    }, []);

    //          render: 메인 상단 컴포넌트 렌더링          //
    return (
      <div id='main-top-wrapper'>
        <div className='main-top-container'>
          <div className='main-top-intro'>{'WellbeingHub에서\n다양한 이야기를 나눠보세요'}</div>
          <div className='main-top-contents-box'>
            <div className='main-top-contents-title'>{'주간 TOP 3 게시글'}</div>
            <div className='main-top-contents'>
              {top3List.map(boardItem => <Top3ListItem boardItem={boardItem} />)}
            </div>
          </div>
        </div>
      </div>
    );
  }
  //          component: 메인 하단 컴포넌트          //
  const MainBottom = () => {
    //          state: 인기 검색어 리스트 상태          //
    const [popularWordList, setPopularWordList] = useState<string[]>([]);
    //          state: 전체 게시글 리스트 상태          //
    const [boardList, setBoardList] = useState<BoardListItem[]>([]);
    //          state: 페이지네이션 관련 상태          //
    const {currentPageNumber, setCurrentPageNumber, currentSectionNumber, setCurrentSectionNumber, viewBoardList, viewPageNumberList, totalSection, setBoardList: setPaginationBoardList} = usePagination<BoardListItem>(5);
    const [selectedType, setSelectedType] = useState<BoardType>(BoardType.INFORMATION);

    //          function: 네비게이트 함수          //
    const navigator = useNavigate();
    const { user } = useUserStore();  // 로그인 상태 확인을 위해 추가
    const [cookies] = useCookies();   // 쿠키 확인을 위해 추가

    //          function: get popular list response 처리 함수          //
    const getPopularListResponse = (responseBody: GetPopularListResponseDto | ResponseDto) => {
      const { code } = responseBody;
      if (code === 'DBE') alert('데이터베이스 오류입니다.');
      if (code !== 'SU') return;

      const { popularWordList } = responseBody as GetPopularListResponseDto;
      setPopularWordList(popularWordList);
    }
    //          function: get latest board list response 처리 함수          //
    const getLatestBoardListResponse = (responseBody: GetLatestBoardListResponseDto | ResponseDto) => {
      const { code } = responseBody;
      if (code === 'DBE') alert('데이터베이스 오류입니다.');
      if (code !== 'SU') return;

      const { latestList } = responseBody as GetLatestBoardListResponseDto;
      setBoardList(latestList);
      // 현재 선택된 타입의 게시글만 페이지네이션에 전달
      const filteredList = latestList.filter(board => board.boardType === selectedType);
      setPaginationBoardList(filteredList);
    }

    //          event handler: 인기 검색어 뱃지 클릭 이벤트 처리          //
    const onWordBadgeClickHandler = (word: string) => {
      navigator(SEARCH_PATH(word));
    }

    //          event handler: 타입 필터 클릭 이벤트 처리          //
    const onTypeFilterClick = (type: BoardType) => {
      setSelectedType(type);
      // 선택된 타입의 게시글만 필터링하여 페이지네이션에 전달
      const filteredList = boardList.filter(board => board.boardType === type);
      setPaginationBoardList(filteredList);
      // 페이지 초기화
      setCurrentPageNumber(1);
      setCurrentSectionNumber(1);
    };

    //          event handler: 글쓰기 버튼 클릭 이벤트 처리          //
    const onWriteButtonClickHandler = () => {
        if (!cookies.accessToken) {
            alert('로그인이 필요합니다.');
            return;
        }
        navigator(BOARD_WRITE_PATH);
    }

    //          effect: 컴포넌트 마운트 시 실행할 함수          //
    useEffect(() => {
      getPopularListRequest().then(getPopularListResponse);
      getLatestBoardListRequest().then(getLatestBoardListResponse);
    }, []);

    //          render: 메인 하단 컴포넌트 렌더링          //
    return (
      <div id='main-bottom-wrapper'>
        <div className='main-bottom-container'>
          <div className='main-bottom-header'>
            <div className='main-bottom-title-box'>
              <div className='main-bottom-title'>{'최신 게시물'}</div>
              <div className='write-button' onClick={onWriteButtonClickHandler}>
                글쓰기
              </div>
            </div>
            <div className='main-bottom-filter'>
              <div 
                className={`filter-button ${selectedType === BoardType.INFORMATION ? 'selected' : ''}`}
                onClick={() => onTypeFilterClick(BoardType.INFORMATION)}
              >
                정보 공유
              </div>
              <div 
                className={`filter-button ${selectedType === BoardType.TEAM ? 'selected' : ''}`}
                onClick={() => onTypeFilterClick(BoardType.TEAM)}
              >
                팀 참여
              </div>
            </div>
          </div>
          <div className='main-bottom-contents-box'>
            <div className='main-bottom-latest-contents-box'>
              {viewBoardList.map(boardItem => (
                <BoardItem key={boardItem.boardNumber} boardItem={boardItem} />
              ))}
            </div>
            <div className='main-bottom-popular-word-box'>
              <div className='main-bottom-popular-word-card'>
                <div className='main-bottom-popular-card-box'>
                  <div className='main-bottom-popular-card-title'>{'인기 검색어'}</div>
                  <div className='main-bottom-popular-card-contents'>
                    { popularWordList.map(popularWord => <div className='word-badge' onClick={() => onWordBadgeClickHandler(popularWord)}>{popularWord}</div>) } 
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className='main-bottom-pagination-box'>
            <Pagination
              currentPageNumber={currentPageNumber}
              currentSectionNumber={currentSectionNumber}
              setCurrentPageNumber={setCurrentPageNumber}
              setCurrentSectionNumber={setCurrentSectionNumber}
              viewPageNumberList={viewPageNumberList}
              totalSection={totalSection}
            />
          </div>
        </div>
      </div>
    );
  }

  //          render: 메인 페이지 렌더링          //
  return (
    <>
      <MainTop />
      <MainBottom />
    </>
  )
}
