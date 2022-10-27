package com.jungbu.mybatis_board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jungbu.mybatis_board.dto.BoardDto;
import com.jungbu.mybatis_board.dto.ReplyDto;
import com.jungbu.mybatis_board.dto.SearchDto;
import com.jungbu.mybatis_board.mapper.BoardMapper;

@Service //@AutoWired로 객체 주입 가능
public class BoardService {
	@Autowired
	BoardMapper boardMapper;
	@Autowired
	ReplyService replyService;
	public PageInfo<BoardDto> readBoardPaging(SearchDto search){
		PageHelper.startPage(search.getPage(), search.getRows(), search.getOrderBy());
		return PageInfo.of(boardMapper.list(),search.getNavSize());
	}
	public BoardDto readBoardWithReplysPaging(int boardNo,SearchDto search) {
		BoardDto board=boardMapper.detail(boardNo);
		PageInfo<ReplyDto> replyListPaging=replyService.readReplysPaging(boardNo, search);
		board.setReplyList(replyListPaging);
		return board;
	}
	
}
