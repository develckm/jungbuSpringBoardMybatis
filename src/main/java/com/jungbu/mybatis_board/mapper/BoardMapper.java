package com.jungbu.mybatis_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.jungbu.mybatis_board.dto.BoardDto;
//com.jungbu.mybatis_board.mapper.BoardMapper
@Mapper
public interface BoardMapper {
	//Page=ArrayList+Page정보
	Page<BoardDto> list();
	int listCount();
	BoardDto detail(int boardNo);
	BoardDto detailReplyPaging(int boardNo, int startRow, int rows);
	int insert(BoardDto board);
	int update(BoardDto board);
	int viewUpdate(int boardNo);
	int delete(int boardNo);
}
