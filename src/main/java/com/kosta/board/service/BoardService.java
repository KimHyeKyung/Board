package com.kosta.board.service;

import java.util.List;

import com.kosta.board.bean.Board;
import com.kosta.board.bean.PageInfo;

public interface BoardService {

	void registBoard(Board board) throws Exception;
	
	List<Board> getBoardList(int page, PageInfo pageInfo) throws Exception;
	
	Board getBoard(Integer boardNum) throws Exception;
	
	void modifyBoard(Board board) throws Exception;

	void boardReply(Board board) throws Exception;

	void deleteform(Integer boardNum, String password) throws Exception;
}
