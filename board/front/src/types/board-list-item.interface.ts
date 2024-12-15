import { BoardType } from './board.interface';

export default interface BoardListItem {
    boardNumber: number;
    title: string;
    content: string;
    boardTitleImage: string | null;
    favoriteCount: number;
    commentCount: number;
    viewCount: number;
    writeDatetime: string;
    writerNickname: string;
    writerProfileImage: string | null;
    boardType: BoardType;
    teamUrl?: string;
}
