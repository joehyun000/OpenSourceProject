package com.seojihoon.boardback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seojihoon.boardback.entity.BoardEntity;
import com.seojihoon.boardback.common.BoardType;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    
    boolean existsByBoardNumber(Integer boardNumber);

    BoardEntity findByBoardNumber(Integer boardNumber);

    List<BoardEntity> findByBoardType(BoardType boardType);
    List<BoardEntity> findByBoardTypeOrderByWriteDatetimeDesc(BoardType boardType);

}
