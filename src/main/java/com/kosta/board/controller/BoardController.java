package com.kosta.board.controller;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.board.bean.Board;
import com.kosta.board.bean.PageInfo;
import com.kosta.board.service.BoardService;

@Controller
public class BoardController {

	@Autowired
	ServletContext servletContext;
	
	@Autowired
	BoardService boardService;
	
	@RequestMapping(value = "/writeform",method = RequestMethod.GET)
	public String writeform() {
		return "/board/writeform";
	}
	
	//2.요즘방식
	@RequestMapping(value = "/boardwrite",method = RequestMethod.POST)
	public ModelAndView boardwrite(@ModelAttribute Board board) {
		ModelAndView mav = new ModelAndView();
		try {
			String path = servletContext.getRealPath("/upload/");//getRealPath: webapp의 실제경로를 얻어옴.
			MultipartFile file = board.getFile(); //파일 자체를 가져옴
			if(!file.isEmpty()) {
				File destFile = new File(path + file.getOriginalFilename());//file을 destFile로 옮겨라.
				file.transferTo(destFile);
				board.setBoard_file(file.getOriginalFilename());//파일의 이름을 넣어주기위해 따로 설정
			}
			boardService.registBoard(board);
			mav.setViewName("redirect:/boardList");
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("/board/err");
		}
		return mav;
	}
	
	//1.예전방식 enctype="multipart/form-data"를 받을때는 꼭 MultipartRequest형태로 받는다
	/*
	@RequestMapping(value = "/boardwrite",method = RequestMethod.POST)
	public ModelAndView boardwrite(MultipartHttpServletRequest multi) {
		ModelAndView mav = new ModelAndView();
		//getRealPath: webapp의 실제 경로를 얻어온다.
		String path = servletContext.getRealPath("/upload/");
		try {
			Board board = new Board();
			MultipartFile file = multi.getFile("file");
			if(!file.isEmpty()) {
				File destFile = new File(path + file.getOriginalFilename());//file을 destFile로 옮겨라.
				file.transferTo(destFile);
				board.setBoard_file(file.getOriginalFilename());
			}
			
			board.setBoard_name(multi.getParameter("board_name"));
			board.setBoard_pass(multi.getParameter("board_pass"));
			board.setBoard_subject(multi.getParameter("board_subject"));
			board.setBoard_content(multi.getParameter("board_content"));
			
			boardService.registBoard(board);
			mav.setViewName("/board/listform");
			
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("/board/err");
		}
		return mav;
	}	
	*/
	
	//게시판 요청
	@RequestMapping(value = "/boardList",method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView boardList(@RequestParam(value = "page",required = false, defaultValue = "1")Integer page) {
		ModelAndView mav = new ModelAndView();
		PageInfo pageInfo = new PageInfo();
		try {
			List<Board> articleList = boardService.getBoardList(page, pageInfo);
			mav.addObject("articleList",articleList);
			mav.addObject("pageInfo", pageInfo);
			mav.setViewName("/board/listform");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", e.getMessage());
			mav.setViewName("/board/err");
		}
		return mav;
	}
	
	//선택한 게시글의 detail 페이지
	@RequestMapping(value = "/boarddetail", method = RequestMethod.GET)
	public ModelAndView boarddetail(@RequestParam("board_num")Integer boardNum, @RequestParam(value = "page", required=false, defaultValue="1") Integer page) {
		ModelAndView mav = new ModelAndView();
		try {
			Board board = boardService.getBoard(boardNum);
			mav.addObject("article", board);
			mav.addObject("page", page);
			mav.setViewName("/board/viewform");
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("/board/err");
		}
		return mav;
	}
	
	//수정페이지 이동
	@RequestMapping(value = "/modifyform", method = RequestMethod.GET)
	public ModelAndView modifyform(@RequestParam("board_num")Integer boardNum) {
		ModelAndView mav = new ModelAndView();
		try {
			Board board = boardService.getBoard(boardNum);
			mav.addObject("article", board);
			mav.setViewName("/board/modifyform");
			
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "조회 실패");
			mav.setViewName("/board/err");
		}
		return mav;
	}
	
	//수정!
	@RequestMapping(value = "/boardmodify", method = RequestMethod.POST)
	public ModelAndView boardmodify(@ModelAttribute Board board) {
		ModelAndView mav = new ModelAndView();
		try {
			boardService.modifyBoard(board);
			mav.addObject("board_num", board.getBoard_num());
			mav.setViewName("redirect:/boarddetail");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "조회 실패");
			mav.setViewName("/board/err");
		}
		return mav;
	}
	
	//답변 페이지로 이동
	@RequestMapping(value = "replyform", method = RequestMethod.GET)
	public ModelAndView replyform(@RequestParam("board_num")Integer boardNum,
								  @RequestParam(value = "page", required=false,defaultValue="1") Integer page) {
		//페이지 정보를 답변을 누를때 가져가야한다.(원글에 대한 번호(답글을 달 때 re_ref로 가져가기 위해서), 몇번째 페이지인지)
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("boardNum", boardNum);
			mav.addObject("page", page);
			mav.setViewName("/board/replyform");
			
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "조회 실패");
			mav.setViewName("/board/err");
		}
		return mav;
	}
	
	//답변 등록
	@RequestMapping(value = "/boardreply", method = RequestMethod.POST)
	public ModelAndView boardreply(@ModelAttribute Board board,
								   @RequestParam(value = "page", required=false,defaultValue="1") Integer page) {
		ModelAndView mav = new ModelAndView();
		try {
			boardService.boardReply(board);
			mav.addObject("page", page);
			mav.setViewName("redirect:/boardList");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "조회 실패");
			mav.setViewName("/board/err");
		}
		return mav;
	}
	
	//삭제페이지 이동
	@RequestMapping(value = "/deleteform", method = RequestMethod.GET)
	public ModelAndView deleteform(@RequestParam("board_num") Integer boardNum,
								   @RequestParam(value = "page", required=false,defaultValue="1") Integer page) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.addObject("board_num", boardNum);
			mav.addObject("page", page);
			mav.setViewName("/board/deleteform");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "조회 실패");
			mav.setViewName("/board/err");
		}
		return mav;
	}
	
	//삭제 수행
	@RequestMapping(value = "/boarddelete", method = RequestMethod.POST)
	public ModelAndView boarddelete(@RequestParam("board_num") Integer boardNum,
								    @RequestParam(value = "page", required=false,defaultValue="1") Integer page,
								    @RequestParam("board_pass") String password) {
		ModelAndView mav = new ModelAndView();
		try {
			boardService.deleteform(boardNum,password);
			mav.addObject("page", page);
			mav.setViewName("redirect:/boardList");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "삭제 실패");
			mav.setViewName("/board/err");
		}
		return mav;
	}
		
	
}