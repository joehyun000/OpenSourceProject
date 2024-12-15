import { create } from 'zustand';
import { BoardType } from 'types/board.interface';

interface BoardStore {
    title: string;
    contents: string;
    images: File[];
    boardType: BoardType;
    teamUrl: string;

    setTitle: (title: string) => void;
    setContents: (contents: string) => void;
    setImages: (images: File[]) => void;
    setBoardType: (boardType: BoardType) => void;
    setTeamUrl: (teamUrl: string) => void;

    resetBoard: () => void;
}

const useBoardStore = create<BoardStore>((set) => ({
    title: '',
    contents: '',
    images: [],
    boardType: BoardType.INFORMATION,
    teamUrl: '',

    setTitle: (title: string) => {set((state) => ({ ...state, title }))},
    setContents: (contents: string) => {set((state) => ({ ...state, contents }))},
    setImages: (images: File[]) => {set((state) => ({ ...state, images }))},
    setBoardType: (boardType: BoardType) => {set((state) => ({ ...state, boardType }))},
    setTeamUrl: (teamUrl: string) => {set((state) => ({ ...state, teamUrl }))},

    resetBoard: () => {
        set((state) => ({ 
            ...state, 
            title: '', 
            contents: '', 
            images: [],
            boardType: BoardType.INFORMATION,
            teamUrl: ''
        }))
    }
}));

export default useBoardStore;