import { useState, useEffect } from 'react';

const usePagination = <T>(countPerPage: number) => {
    //          state: 현재 페이지 번호 상태          //
    const [currentPageNumber, setCurrentPageNumber] = useState<number>(1);
    //          state: 현재 섹션 번호 상태          //
    const [currentSectionNumber, setCurrentSectionNumber] = useState<number>(1);
    //          state: 보여줄 게시물 리스트 상태          //
    const [viewBoardList, setViewBoardList] = useState<T[]>([]);
    //          state: 보여줄 페이지 번호 리스트 상태          //
    const [viewPageNumberList, setViewPageNumberList] = useState<number[]>([]);
    //          state: 전체 페이지 번호 상태          //
    const [totalPage, setTotalPage] = useState<number>(0);
    //          state: 전체 섹션 번호 상태          //
    const [totalSection, setTotalSection] = useState<number>(0);
    //          state: 전체 게시물 리스트 상태          //
    const [boardList, setBoardList] = useState<T[]>([]);

    //          function: 보여줄 게시물 리스트 불러오기 함수          //
    const setViewBoard = () => {
        const FIRST_INDEX = countPerPage * (currentPageNumber - 1);
        const LAST_INDEX = countPerPage * currentPageNumber;
        const tmpList = boardList.filter((item, index) => (index >= FIRST_INDEX && index < LAST_INDEX));
        
        setViewBoardList(tmpList);
    }
    //          function: 보여줄 페이지 리스트 불러오기 함수          //
    const setViewPage = (totalPage: number) => {
        const SECTION_SIZE = 10;
        const FIRST_PAGE_INDEX = SECTION_SIZE * (currentSectionNumber - 1) + 1;
        const LAST_PAGE_INDEX = Math.min(SECTION_SIZE * currentSectionNumber, totalPage);

        const tmpPageNumberList = [];
        for (let pageNumber = FIRST_PAGE_INDEX; pageNumber <= LAST_PAGE_INDEX; pageNumber++) {
            tmpPageNumberList.push(pageNumber);
        }

        setViewPageNumberList(tmpPageNumberList);
    }

    //          effect: 전체 게시물 리스트가 변경될 시 작업          //
    useEffect(() => {
        // 전체 페이지 수 계산 수정
        const totalPage = Math.ceil(boardList.length / countPerPage);
        // 전체 섹션 수 계산 수정
        const totalSection = Math.ceil(totalPage / 10);
        
        setCurrentPageNumber(1);
        setCurrentSectionNumber(1);
        setTotalPage(totalPage);
        setTotalSection(totalSection);
        setCurrentPageNumber(1);
        setCurrentSectionNumber(1);

        setViewBoard();
        setViewPage(totalPage);
    }, [boardList]);

    //          effect: 현재 페이지가 변경될 시 보여줄 게시물 리스트 불러오기          //
    useEffect(() => {
        setViewBoard();
    }, [currentPageNumber]);

    //          effect: 현재 섹션이 변경될 시 보여줄 페이지 리스트 불러오기          //
    useEffect(() => {
        // 현재 섹션이 전체 섹션보다 크면 마지막 섹션으로 조정
        if (currentSectionNumber > totalSection) {
            setCurrentSectionNumber(totalSection);
            return;
        }
        setViewPage(totalPage);
    }, [currentSectionNumber, totalSection]);

    return {
        currentPageNumber, 
        setCurrentPageNumber, 
        currentSectionNumber, 
        setCurrentSectionNumber,
        viewBoardList,
        viewPageNumberList,
        totalSection,
        setBoardList,
        totalCount: boardList.length,
        countPerPage
    };
}

export default usePagination;