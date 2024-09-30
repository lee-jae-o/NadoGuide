package spring.tree.service;

import spring.tree.dto.BoardDTO;

import java.util.List;

public interface IBoardService {

    void insertBoard(BoardDTO pDTO) throws Exception;

    List<BoardDTO> getBoardList();

    BoardDTO getBoard(BoardDTO pDTO, boolean type) throws Exception;

    void updateBoard(BoardDTO pDTO) throws Exception;


    void deleteBoard(BoardDTO pDTO) throws Exception;

}
