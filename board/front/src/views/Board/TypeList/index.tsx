import React, { useEffect, useState } from 'react';
import './style.css';
import { BoardListItem } from 'types';
import { BoardType } from 'types/board.interface';
import { getBoardsByType } from 'apis';
import BoardItem from 'components/BoardItem';

export default function TypeList() {
    //          state: 게시글 목록 상태          //
    const [boardList, setBoardList] = useState<BoardListItem[]>([]);
    //          state: 선택된 게시판 타입 상태          //
    const [selectedType, setSelectedType] = useState<BoardType>(BoardType.INFORMATION);
    //          state: 로딩 상태          //
    const [isLoading, setLoading] = useState<boolean>(false);

    //          function: 게시글 목록 불러오기 함수          //
    const getBoardList = async () => {
        setLoading(true);
        try {
            const response = await getBoardsByType(selectedType);
            const { code, message, boardList } = response;
            if (code === 'SU') setBoardList(boardList);
            else alert(message);
        } catch (error) {
            alert('Error!');
        }
        setLoading(false);
    }

    //          event handler: 타입 변경 이벤트 처리          //
    const onTypeChangeHandler = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedType(event.target.value as BoardType);
    };

    //          effect: 선택된 타입이 변경될 때마다 게시글 목록 불러오기          //
    useEffect(() => {
        getBoardList();
    }, [selectedType]);

    //          render: 타입별 게시글 목록 컴포넌트 렌더링          //
    return (
        <div id='type-list-wrapper'>
            <div className='type-list-container'>
                <div className='type-list-title-box'>
                    <h1 className='type-list-title'>
                        {selectedType === BoardType.TEAM ? '팀 게시판' : '정보 공유'}
                    </h1>
                    <select 
                        className='type-list-select'
                        value={selectedType}
                        onChange={onTypeChangeHandler}
                    >
                        <option value={BoardType.INFORMATION}>정보 공유</option>
                        <option value={BoardType.TEAM}>팀 게시판</option>
                    </select>
                </div>
                <div className='type-list-contents'>
                    {isLoading ? (
                        <div className='type-list-loading'>로딩중...</div>
                    ) : boardList.length === 0 ? (
                        <div className='type-list-empty'>게시글이 없습니다.</div>
                    ) : (
                        boardList.map(boardItem => (
                            <BoardItem key={boardItem.boardNumber} boardItem={boardItem} />
                        ))
                    )}
                </div>
            </div>
        </div>
    );
} 