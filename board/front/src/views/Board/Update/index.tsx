import React, { ChangeEvent, useRef, useState, useEffect } from 'react';
import './style.css';
import { useBoardStore } from 'stores';
import { useNavigate, useParams } from 'react-router-dom';
import { boardMock } from 'mocks';
import { convertUrlsToFiles } from 'utils';
import { getBoardRequest } from 'apis';
import { GetBoardResponseDto } from 'apis/dto/response/board';
import ResponseDto from 'apis/dto/response';
import { MAIN_PATH } from 'constant';
import { BoardType } from 'types/board.interface';

//          component: 게시물 수정 화면          //
export default function BoardUpdate() {

  //          state: 이미지 인풋 ref 상태          //
  const imageInputRef = useRef<HTMLInputElement | null>(null);
  //          state: 본문 텍스트 영역 ref 상태          //
  const contentsTextAreaRef = useRef<HTMLTextAreaElement | null>(null);
  //          state: 게시물 번호 path variable 상태          //
  const { boardNumber } = useParams();
  //          state: 게시물 상태          //
  const { 
    title, setTitle,
    contents, setContents,
    images, setImages,
    boardType, setBoardType,
    teamUrl, setTeamUrl
  } = useBoardStore();
  //          state: 게시물 이미지 URL 상태          //
  const [imageUrls, setImageUrls] = useState<string[]>([]);

  //          function: 네비게이트 함수          //
  const navigator = useNavigate();
  //          function: get board response 처리 함수          //
  const getBoardResponse = (responseBody: GetBoardResponseDto | ResponseDto) => {
    const { code } = responseBody;
    if (code === 'NB') alert('존재하지 않는 게시물입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');
    if (code !== 'SU') {
      navigator(MAIN_PATH);
      return;
    }
    
    const { title, content, boardImageList, boardType, teamUrl } = responseBody as GetBoardResponseDto;
    setTitle(title);
    setContents(content);
    setBoardType(boardType);
    setTeamUrl(teamUrl || '');
    convertUrlsToFiles(boardImageList).then(files => setImages(files));
    setImageUrls(boardImageList);
  }

  //          event handler: 제목 변경 이벤트 처리          //
  const onTitleChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
    const title = event.target.value;
    setTitle(title);
  }
  //          event handler: 내용 변경 이벤트 처리          //
  const onContentsChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) => {
    const contents = event.target.value;
    setContents(contents);
    if (!contentsTextAreaRef.current) return;
    contentsTextAreaRef.current.style.height = 'auto';
    contentsTextAreaRef.current.style.height = `${contentsTextAreaRef.current.scrollHeight}px`;
  }
  //          event handler: 이미지 변경 이벤트 처리          //
  const onImageChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
    if (!event.target.files || !event.target.files.length) return;
    const file = event.target.files[0];
    const imageUrl = URL.createObjectURL(file);
    const newImageUrls = imageUrls.map(url => url);
    newImageUrls.push(imageUrl);
    const newImages = images.map(image => image);
    newImages.push(file);

    setImageUrls(newImageUrls);
    setImages(newImages);
  }

  //          event handler: 이미지 업로드 버튼 클릭 이벤트 처리          //
  const onImageUploadButtonClickHandler = () => {
    if (!imageInputRef.current) return;
    imageInputRef.current.click();
  }
  //          event handler: 이미지 닫기 버튼 클릭 이벤트 처리          //
  const onImageCloseButtonClickHandler = (deleteIndex: number) => {
    if (!imageInputRef.current) return;
    imageInputRef.current.value = '';

    const newImageUrls = imageUrls.filter((url, index) => index !== deleteIndex);
    setImageUrls(newImageUrls);
    const newImages = images.filter((image, index) => index !== deleteIndex);
    setImages(newImages);
  }

  //          event handler: 게시판 타입 변경 이벤트 처리          //
  const onBoardTypeChangeHandler = (event: ChangeEvent<HTMLSelectElement>) => {
    const type = event.target.value as BoardType;
    setBoardType(type);
  }

  //          event handler: 팀 URL 변경 이벤트 처리          //
  const onTeamUrlChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
    setTeamUrl(event.target.value);
  }

  //          effect: 게시물 번호 path variable이 변경될 때마다 실행될 함수          //
  useEffect(() => {
    if (!boardNumber) return;
    getBoardRequest(boardNumber).then(getBoardResponse);

    
  }, [boardNumber]);

  //          render: 게시물 수정 화면 렌더링          //
  return (
    <div id='board-write-wrapper'>
      <div className='board-write-container'>
        <div className='board-write-box'>
          <div className='board-write-title-box'>
            <input className='board-write-title-input' type='text' placeholder='제목을 작성해주세요.' value={title} onChange={onTitleChangeHandler} />
          </div>
          <div className='board-write-type-box'>
            <select 
              className='board-write-type-select'
              value={boardType}
              onChange={onBoardTypeChangeHandler}
            >
              <option value={BoardType.INFORMATION}>정보 공유</option>
              <option value={BoardType.TEAM}>팀 게시판</option>
            </select>
            {boardType === BoardType.TEAM && (
              <input 
                className='board-write-team-url-input'
                type='text'
                placeholder='팀 참여 URL을 입력해주세요.'
                value={teamUrl}
                onChange={onTeamUrlChangeHandler}
              />
            )}
          </div>
          <div className='divider'></div>
          <div className='board-write-contents-box'>
            <textarea ref={contentsTextAreaRef} className='board-write-contents-textarea' placeholder='본문을 작성해주세요.' spellCheck={false} value={contents} onChange={onContentsChangeHandler} />
            <input ref={imageInputRef} type='file' accept='image/*' style={{ display: 'none' }} onChange={onImageChangeHandler} />
            <div className='icon-button' onClick={onImageUploadButtonClickHandler}>
              <div className='image-box-light-icon'></div>
            </div>
          </div>
          <div className='board-write-images-box'>
            {imageUrls.map((imageUrl, index) => (
            <div className='board-write-image-box'>
              <img className='board-write-image' src={imageUrl} />
              <div className='icon-button image-close' onClick={() => onImageCloseButtonClickHandler(index)}>
                <div className='close-icon'></div>
              </div>
            </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
