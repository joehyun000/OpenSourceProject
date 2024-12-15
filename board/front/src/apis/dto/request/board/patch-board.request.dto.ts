import { BoardType } from 'types/board.interface';

export default interface PatchBoardRequestDto {
    title: string;
    content: string;
    boardType: BoardType;
    teamUrl?: string;
    boardImageList: string[];
}