package com.seojihoon.boardback.service.implement;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.seojihoon.boardback.common.BoardType;
import com.seojihoon.boardback.dto.request.board.PatchBoardRequestDto;
import com.seojihoon.boardback.dto.request.board.PostBoardRequestDto;
import com.seojihoon.boardback.dto.request.board.PostCommentRequestDto;
import com.seojihoon.boardback.dto.response.ResponseDto;
import com.seojihoon.boardback.dto.response.board.DeleteBoardResponseDto;
import com.seojihoon.boardback.dto.response.board.GetBoardResponseDto;
import com.seojihoon.boardback.dto.response.board.GetCommentListResponseDto;
import com.seojihoon.boardback.dto.response.board.GetFavoriteListResponseDto;
import com.seojihoon.boardback.dto.response.board.GetLatestBoardListResponseDto;
import com.seojihoon.boardback.dto.response.board.GetSearchBoardListResponseDto;
import com.seojihoon.boardback.dto.response.board.GetTop3BoardListResponseDto;
import com.seojihoon.boardback.dto.response.board.GetUserBoardListResponseDto;
import com.seojihoon.boardback.dto.response.board.IncreaseViewCountResponseDto;
import com.seojihoon.boardback.dto.response.board.PatchBoardResponseDto;
import com.seojihoon.boardback.dto.response.board.PostBoardResponseDto;
import com.seojihoon.boardback.dto.response.board.PostCommentResponseDto;
import com.seojihoon.boardback.dto.response.board.PutFavoriteResponseDto;
import com.seojihoon.boardback.dto.response.board.GetTypeListResponseDto;
import com.seojihoon.boardback.entity.BoardEntity;
import com.seojihoon.boardback.entity.BoardImageEntity;
import com.seojihoon.boardback.entity.BoardViewEntity;
import com.seojihoon.boardback.entity.CommentEntity;
import com.seojihoon.boardback.entity.FavoriteEntity;
import com.seojihoon.boardback.entity.SearchLogEntity;
import com.seojihoon.boardback.entity.TeamBoardDetailEntity;
import com.seojihoon.boardback.entity.UserEntity;
import com.seojihoon.boardback.repository.BoardImageRepository;
import com.seojihoon.boardback.repository.BoardRepository;
import com.seojihoon.boardback.repository.BoardViewRepository;
import com.seojihoon.boardback.repository.CommentRespository;
import com.seojihoon.boardback.repository.FavoriteRepository;
import com.seojihoon.boardback.repository.SearchLogRepository;
import com.seojihoon.boardback.repository.TeamBoardDetailRepository;
import com.seojihoon.boardback.repository.UserRepository;
import com.seojihoon.boardback.repository.resultSet.CommentListResultSet;
import com.seojihoon.boardback.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplement implements BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final TeamBoardDetailRepository teamBoardDetailRepository;
    private final CommentRespository commentRespository;
    private final FavoriteRepository favoriteRepository;
    private final BoardViewRepository boardViewRepository;
    private final SearchLogRepository searchLogRepository;
    private final BoardImageRepository boardImageRepository;

    @Override
    public ResponseEntity<? super PostBoardResponseDto> postBoard(PostBoardRequestDto dto, String email) {
        try {
            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PostBoardResponseDto.notExistUser();

            if (dto.getBoardType() == BoardType.TEAM) {
                if (dto.getTeamUrl() == null || dto.getTeamUrl().trim().isEmpty()) {
                    return PostBoardResponseDto.invalidTeamUrl();
                }
            }

            BoardEntity boardEntity = new BoardEntity(dto, email);
            boardRepository.save(boardEntity);

            if (dto.getBoardType() == BoardType.TEAM && dto.getTeamUrl() != null) {
                TeamBoardDetailEntity teamBoardDetail = new TeamBoardDetailEntity(boardEntity, dto.getTeamUrl());
                teamBoardDetailRepository.save(teamBoardDetail);
            }

            List<String> boardImageList = dto.getBoardImageList();
            List<BoardImageEntity> boardImageEntities = new ArrayList<>();
            for (String boardImage: boardImageList) {
                BoardImageEntity boardImageEntity = new BoardImageEntity(boardEntity.getBoardNumber(), boardImage);
                boardImageEntities.add(boardImageEntity);
            }
            boardImageRepository.saveAll(boardImageEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer boardNumber, String email) {
        
        try {
            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PostCommentResponseDto.notExistBoard();

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PostCommentResponseDto.notExistUser();

            if (dto.getParentCommentNumber() != null) {
                CommentEntity parentComment = commentRespository.findById(dto.getParentCommentNumber())
                    .orElse(null);
                if (parentComment == null) return PostCommentResponseDto.notExistParentComment();
                
                if (parentComment.getParentComment() != null) {
                    return PostCommentResponseDto.notAllowNestedReply();
                }
            }

            CommentEntity commentEntity = new CommentEntity(dto, boardNumber, email);
            
            if (dto.getParentCommentNumber() != null) {
                CommentEntity parentComment = commentRespository.findById(dto.getParentCommentNumber()).get();
                commentEntity.setParentComment(parentComment);
            }

            commentRespository.save(commentEntity);

            boardEntity.increaseCommentCount();
            boardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostCommentResponseDto.success();
    }

	@Override
	public ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber) {
        try {
            BoardViewEntity boardViewEntity = boardViewRepository.findByBoardNumber(boardNumber);
            if (boardViewEntity == null) return GetBoardResponseDto.notExistBoard();

            List<BoardImageEntity> boardImageEntities = boardImageRepository.findByBoardNumber(boardNumber);

            return GetBoardResponseDto.success(boardViewEntity, boardImageEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
	}

    @Override
    public ResponseEntity<? super GetFavoriteListResponseDto> getFavorietList(Integer boardNumber) {
        
        List<UserEntity> userEntities = new ArrayList<>();

        try {

            boolean existedBoard = boardRepository.existsByBoardNumber(boardNumber);
            if (!existedBoard) return GetFavoriteListResponseDto.notExistBoard();

            userEntities = userRepository.findByBoardFavorite(boardNumber);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFavoriteListResponseDto.success(userEntities);

    }

    @Override
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer boardNumber) {
        
        List<CommentListResultSet> resultSets = new ArrayList<>();

        try {
            boolean existedBoard = boardRepository.existsByBoardNumber(boardNumber);
            if (!existedBoard) return GetCommentListResponseDto.notExistBoard();

            resultSets = commentRespository.findByCommentList(boardNumber);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetCommentListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super GetLatestBoardListResponseDto> getLatestBoardList() {

        List<BoardViewEntity> boardViewEntities = new ArrayList<>();
        
        try {

            boardViewEntities = boardViewRepository.findByOrderByWriteDatetimeDesc();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetLatestBoardListResponseDto.success(boardViewEntities);

    }

    @Override
    public ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(String email) {
        
        List<BoardViewEntity> boardViewEntities = new ArrayList<>();

        try {

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return GetUserBoardListResponseDto.notExistUser();
            
            boardViewEntities = boardViewRepository.findByWriterEmailOrderByWriteDatetimeDesc(email);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUserBoardListResponseDto.success(boardViewEntities);

    }
    
    @Override
    public ResponseEntity<? super GetTop3BoardListResponseDto> getTop3BoardList() {
        
        List<BoardViewEntity> boardViewEntities = new ArrayList<>();

        try {

            Date now = Date.from(Instant.now().minus(7, ChronoUnit.DAYS));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sevenDaysAgo = simpleDateFormat.format(now);

            boardViewEntities = boardViewRepository.findTop3ByWriteDatetimeGreaterThanOrderByFavoriteCountDesc(sevenDaysAgo);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetTop3BoardListResponseDto.success(boardViewEntities);

    }

    @Override
    public ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardList(String searchWord, String preSearchWord) {
        
        List<BoardViewEntity> boardViewEntities = new ArrayList<>();

        try {

            boardViewEntities = boardViewRepository.findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(searchWord, searchWord);

            boolean relation = preSearchWord != null;

            SearchLogEntity searchLogEntity = new SearchLogEntity(searchWord, preSearchWord, false);
            searchLogRepository.save(searchLogEntity);

            if (relation) {
                searchLogEntity = new SearchLogEntity(preSearchWord, searchWord, relation);
                searchLogRepository.save(searchLogEntity);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetSearchBoardListResponseDto.success(boardViewEntities);

    }

    @Override
    public ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer boardNumber, String email) {
        
        try {

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PutFavoriteResponseDto.notExistBoard();

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PutFavoriteResponseDto.notExistUser();

            boolean isFavorite = favoriteRepository.existsByUserEmailAndBoardNumber(email, boardNumber);
            
            FavoriteEntity favoriteEntity = new FavoriteEntity(email, boardNumber);

            if (isFavorite) {
                favoriteRepository.delete(favoriteEntity);
                boardEntity.decreaseFavoriteCount();
            }
            else {
                favoriteRepository.save(favoriteEntity);
                boardEntity.increaseFavoriteCount();
            }

            boardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PutFavoriteResponseDto.success();

    }

    @Override
    public ResponseEntity<? super PatchBoardResponseDto> patchBoard(PatchBoardRequestDto dto, Integer boardNumber, String email) {
        try {
            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PatchBoardResponseDto.notExistUser();

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PatchBoardResponseDto.notExistBoard();

            boolean equalWriter = boardEntity.getWriterEmail().equals(email);
            if (!equalWriter) return PatchBoardResponseDto.noPermission();

            if (dto.getBoardType() == BoardType.TEAM) {
                if (dto.getTeamUrl() == null || dto.getTeamUrl().trim().isEmpty()) {
                    return PatchBoardResponseDto.invalidTeamUrl();
                }
            }

            boardEntity.patch(dto);
            boardRepository.save(boardEntity);

            List<String> boardImageList = dto.getBoardImageList();
            boardImageRepository.deleteByBoardNumber(boardNumber);

            List<BoardImageEntity> boardImageEntities = new ArrayList<>();
            for (String boardImage: boardImageList) {
                BoardImageEntity boardImageEntity = new BoardImageEntity(boardNumber, boardImage);
                boardImageEntities.add(boardImageEntity);
            }
            boardImageRepository.saveAll(boardImageEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PatchBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super IncreaseViewCountResponseDto> increaseViewCount(Integer boardNumber) {
        
        try {

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return IncreaseViewCountResponseDto.notExistBoard();

            boardEntity.increaseViewCount();
            boardRepository.save(boardEntity);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return IncreaseViewCountResponseDto.success();

    }

    @Override
    public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Integer boardNumber, String email) {
        
        try {

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return DeleteBoardResponseDto.notExistUser();

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return DeleteBoardResponseDto.notExistBoard();

            boolean isWriter = boardEntity.getWriterEmail().equals(email);
            if (!isWriter) return DeleteBoardResponseDto.noPermission();

            commentRespository.deleteByBoardNumber(boardNumber);
            favoriteRepository.deleteByBoardNumber(boardNumber);
            boardImageRepository.deleteByBoardNumber(boardNumber);
            boardRepository.delete(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return DeleteBoardResponseDto.success();

    }

    @Override
    public ResponseEntity<? super GetTypeListResponseDto> getBoardsByType(BoardType boardType) {
        try {
            List<BoardViewEntity> boardViewEntities = 
                boardViewRepository.findByBoardTypeOrderByWriteDatetimeDesc(boardType);
            
            if (boardViewEntities == null || boardViewEntities.isEmpty()) {
                return GetTypeListResponseDto.noExistBoardList();
            }

            return GetTypeListResponseDto.success(boardViewEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

}
