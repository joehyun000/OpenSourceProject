import ResponseDto from '..';
import BoardListItem from 'types/board-list-item.interface';

export default interface GetTypeListResponseDto extends ResponseDto {
    boardList: BoardListItem[];
} 