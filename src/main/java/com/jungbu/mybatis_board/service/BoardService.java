package com.jungbu.mybatis_board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jungbu.mybatis_board.dto.BoardDto;
import com.jungbu.mybatis_board.dto.BoardImgDto;
import com.jungbu.mybatis_board.dto.ReplyDto;
import com.jungbu.mybatis_board.dto.SearchDto;
import com.jungbu.mybatis_board.mapper.BoardImgMapper;
import com.jungbu.mybatis_board.mapper.BoardMapper;

@Service //@AutoWired로 객체 주입 가능
public class BoardService {
	@Autowired
	BoardMapper boardMapper;
	@Autowired
	BoardImgMapper boardImgMapper;
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
	public int registBoardAndBoardImgs(BoardDto board,List<String>imgPaths) {
		int insert=0;
		insert=boardMapper.insert(board);
		int imgInsert=0;
		for(String imgPath:imgPaths) {
			BoardImgDto boardImg=new BoardImgDto();
			boardImg.setBoardNo(board.getBoardNo());
			boardImg.setImgPath(imgPath);
			imgInsert+=boardImgMapper.insert(boardImg);
		}
		System.out.println("저장된 이미지 db수 :"+imgInsert);
		return insert;
	}
}
