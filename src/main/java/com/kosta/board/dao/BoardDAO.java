package com.kosta.board.dao;

import java.util.List;

import com.kosta.board.bean.Board;

public interface BoardDAO {

	//�Խñ� insert
	void insertBoard(Board board) throws Exception;
	
	//maxNum��������
	Integer selectMaxBoardNum() throws Exception;
	
	//page��ϰ�������
	List<Board> selectBoardList(Integer row) throws Exception;
	
	Integer selectBoardCount() throws Exception;
	
	Board selectBoard(Integer board_num) throws Exception;
	
	void updateBoard(Board board) throws Exception;
	
	void updateBoardReSeq(Board board) throws Exception;

	void deleteBoard(Integer boardNum) throws Exception;
}
