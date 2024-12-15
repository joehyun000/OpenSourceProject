import { BoardType } from 'types/board.interface';

export default interface PostBoardRequestDto {
    title: string;
    content: string;
    boardType: BoardType;
    teamUrl?: string;
    boardImageList: string[];
}