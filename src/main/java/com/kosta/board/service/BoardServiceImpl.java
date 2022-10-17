package com.kosta.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.board.bean.Board;
import com.kosta.board.bean.PageInfo;
import com.kosta.board.dao.BoardDAO;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDAO boardDAO;
	
	@Override
	public void registBoard(Board board) throws Exception{
		Integer boardNum = boardDAO.selectMaxBoardNum();
		if(boardNum == null) {
			boardNum = 1;
		}else {
			boardNum = boardNum + 1;
		}
		board.setBoard_num(boardNum);
		board.setBoard_re_ref(boardNum);
		board.setBoard_re_lev(0);
		board.setBoard_re_seq(0);
		board.setBoard_readcount(0);
		boardDAO.insertBoard(board);
		
	}

	//����¡
	@Override
	public List<Board> getBoardList(int page, PageInfo pageInfo) throws Exception {
		int listCount = boardDAO.selectBoardCount();		//��ü �Խñ� ��
		int maxPage = (int)Math.ceil((double)listCount/10);	//��ü ������ �� (Math.ceil: �Ҽ��� �ø�ó��)
		int startPage = page/10 * 10 + 1;					//���� �������� ������ ���� ������ ��ư(1,11,21 ...)
		int endPage = startPage + 10 - 1;					//���� �������� ������ ������ ������ ��ư(10,20,30 ...)
		if(endPage>maxPage) {
			endPage = maxPage;
		}
		
		pageInfo.setPage(page);
		pageInfo.setListCount(listCount);
		pageInfo.setMaxPage(maxPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		int row = (page-1)*10+1;
		
		return boardDAO.selectBoardList(row);
	}

	@Override
	public Board getBoard(Integer boardNum) throws Exception {
		return boardDAO.selectBoard(boardNum);
	}

	//�Խñ� ����
	@Override
	public void modifyBoard(Board board) throws Exception {
		//���� �� �н����� Ȯ��
		String password = boardDAO.selectBoard(board.getBoard_num()).getBoard_pass();
		if(!password.equals(board.getBoard_pass())) {
			throw new Exception("�������Ѿ���");
		}
		boardDAO.updateBoard(board);
	}

	//�亯 ���
	//�Ķ���ͷ� ���� �� board��ȣ�� �θ�(���� ��)��, �������� �� ��۲�
	@Override
	public void boardReply(Board board) throws Exception {
		Board srcBoard = getBoard(board.getBoard_num());		//���� �� ������ ������
		boardDAO.updateBoardReSeq(srcBoard);
		Integer boardNum = boardDAO.selectMaxBoardNum() + 1;
		board.setBoard_num(boardNum);
		board.setBoard_re_ref(srcBoard.getBoard_re_ref()); 		//re_ref�� ������ ��ȣ�� ��������.
		board.setBoard_re_lev(srcBoard.getBoard_re_lev() + 1);	//����̴� �θ𺸴� + 1 ���ش�.
		board.setBoard_re_seq(srcBoard.getBoard_re_seq() + 1);
		boardDAO.insertBoard(board);
	}

	//���� ����
	@Override
	public void deleteform(Integer boardNum, String password) throws Exception {
		//��й�ȣ ��
		Board board = getBoard(boardNum);
		if(!board.getBoard_pass().equals(password)) {
			throw new Exception("���� ���� ����");
		}else {
			boardDAO.deleteBoard(boardNum);
		}
		
	}

}
