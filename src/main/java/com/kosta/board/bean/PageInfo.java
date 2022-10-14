package com.kosta.board.bean;

public class PageInfo {

	private int page;		//현재페이지
	private int maxPage;	//전체페이지(전체개수/한 화면에 띄울 리스트의 수 -> 1000개/10개 = 100페에지가 나와야함)
	private int startPage;	//시작페이지(처음버튼)
	private int endPage;	//마지막페이지(마지막버튼)
	private int listCount;	//전체 리스트의 개수
	
	public PageInfo() {
		super();
	}
	public PageInfo(int page, int maxPage, int startPage, int endPage, int listCount) {
		super();
		this.page = page;
		this.maxPage = maxPage;
		this.startPage = startPage;
		this.endPage = endPage;
		this.listCount = listCount;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getMaxPage() {
		return maxPage;
	}
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getListCount() {
		return listCount;
	}
	public void setListCount(int listCount) {
		this.listCount = listCount;
	}
}
