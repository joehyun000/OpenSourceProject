import { BoardListItem } from 'types';
import { BoardType } from 'types/board.interface';

const top3ListMock: BoardListItem[] = [
    {
        boardNumber: 1,
        title: '안녕하세요',
        content: '안녕하세요 반갑습니다.',
        boardTitleImage: null,
        viewCount: 0,
        commentCount: 0,
        favoriteCount: 0,
        writeDatetime: '2023. 08. 24.',
        writerNickname: '깃허브',
        writerProfileImage: null,
        boardType: BoardType.INFORMATION,
    },
    {
        boardNumber: 2,
        title: '안녕하세요',
        content: '안녕하세요 반갑습니다.',
        boardTitleImage: null,
        viewCount: 0,
        commentCount: 0,
        favoriteCount: 0,
        writeDatetime: '2023. 08. 24.',
        writerNickname: '깃허브',
        writerProfileImage: null,
        boardType: BoardType.INFORMATION,
    },
    {
        boardNumber: 3,
        title: '안녕하세요',
        content: '안녕하세요 반갑습니다.',
        boardTitleImage: null,
        viewCount: 0,
        commentCount: 0,
        favoriteCount: 0,
        writeDatetime: '2023. 08. 24.',
        writerNickname: '깃허브',
        writerProfileImage: null,
        boardType: BoardType.INFORMATION,
    },
];

export default top3ListMock;