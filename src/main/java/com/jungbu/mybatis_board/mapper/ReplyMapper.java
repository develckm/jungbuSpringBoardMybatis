package com.jungbu.mybatis_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jungbu.mybatis_board.dto.ReplyDto;

@Mapper
public interface ReplyMapper {
	List<ReplyDto> list(int boardNo,int startRow,int rows);
	ReplyDto detail(int replyNo);
	int insert(ReplyDto reply);
	int update(ReplyDto reply);
	int delete(int replyNo);
}
