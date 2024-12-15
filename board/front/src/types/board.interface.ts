export enum BoardType {
    INFORMATION = 'INFORMATION',
    TEAM = 'TEAM'
}

export default interface Board {
    boardNumber: number;
    title: string;
    content: string;
    boardImageList: string[];
    writeDatetime: string;
    writerEmail: string;
    writerNickname: string;
    writerProfileImage: string | null;
    boardType: BoardType;
    teamUrl?: string;  // optional for team boards
}