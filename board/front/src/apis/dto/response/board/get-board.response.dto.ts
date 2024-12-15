import { BoardType } from 'types/board.interface';
import ResponseDto from '..';

export default interface GetBoardResponseDto extends ResponseDto {
    boardNumber: number;
    title: string;
    content: string;
    boardImageList: string[];
    writeDatetime: string;
    writerEmail: string;
    writerNickname: string;
    writerProfileImage: string | null;
    boardType: BoardType;
    teamUrl?: string;
}