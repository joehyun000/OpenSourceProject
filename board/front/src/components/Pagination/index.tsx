import React, { Dispatch, SetStateAction } from 'react';
import './style.css';

//          interface: 페이지네이션 컴포넌트 Properties         //
interface Props {
  currentPageNumber: number;
  currentSectionNumber: number;
  setCurrentPageNumber: Dispatch<SetStateAction<number>>;
  setCurrentSectionNumber: Dispatch<SetStateAction<number>>;

  viewPageNumberList: number[];
  totalSection: number;
  totalCount: number;
  countPerPage: number;
}

//          component: 페이지네이션 컴포넌트          //
export default function Pagination(props: Props) {

  //          state: Properties          //
  const { currentPageNumber, currentSectionNumber, setCurrentPageNumber, setCurrentSectionNumber } = props;
  const { viewPageNumberList, totalSection } = props;

  //          event handler: 페이지 번호 클릭 이벤트 처리          //
   const onPageNumberClickHandler = (pageNumber: number) => {
    setCurrentPageNumber(pageNumber);
  }
  //          event handler: 다음 버튼 클릭 이벤트 처리          //
  const onNextButtonClickHandler = () => {
    const lastPage = Math.ceil(props.totalCount / props.countPerPage);
    const nextPage = currentPageNumber + 1;
    
    if (nextPage > lastPage) {
        alert('마지막 페이지입니다.');
        return;
    }
    
    if (nextPage > currentSectionNumber * 10) {
        setCurrentSectionNumber(currentSectionNumber + 1);
    }
    setCurrentPageNumber(nextPage);
  }
  //          event handler: 이전 버튼 클릭 이벤트 처리          //
  const onPreviousButtonClickHandler = () => {
    const prevPage = currentPageNumber - 1;
    
    if (prevPage < 1) {
        alert('첫 페이지입니다.');
        return;
    }
    
    if (prevPage <= (currentSectionNumber - 1) * 10) {
        setCurrentSectionNumber(currentSectionNumber - 1);
    }
    setCurrentPageNumber(prevPage);
  }

  //          render: 페이지네이션 컴포넌트 렌렌링          //
  return (
    <div className='pagination-container'>
      <div className='pagination-change-link-box' onClick={onPreviousButtonClickHandler}>
        <div className='pagination-change-link-icon-box'>
          <div className='left-light-icon'></div>
        </div>
        <div className='pagination-change-link-text'>{'이전'}</div>
      </div>
      <div className='pagination-divider'>{'\|'}</div>
      { viewPageNumberList.map(pageNumber => 
        pageNumber === currentPageNumber ? (
          <div key={pageNumber} className='pagination-active-text'>
            {pageNumber}
          </div>
        ) : (
          <div key={pageNumber} className='pagination-text' onClick={() => onPageNumberClickHandler(pageNumber)}>
            {pageNumber}
          </div>
        )
      )}
      <div className='pagination-divider'>{'\|'}</div>
      <div className='pagination-change-link-box' onClick={onNextButtonClickHandler}>
        <div className='pagination-change-link-text'>{'다음'}</div>
        <div className='pagination-change-link-icon-box'>
          <div className='right-light-icon'></div>
        </div>
      </div>
    </div>
  );
}
