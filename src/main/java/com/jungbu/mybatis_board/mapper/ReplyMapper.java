package com.jungbu.mybatis_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.jungbu.mybatis_board.dto.ReplyDto;

@Mapper
public interface ReplyMapper {
	Page<ReplyDto> list(int boardNo);
	ReplyDto detail(int replyNo);
	int insert(ReplyDto reply);
	int update(ReplyDto reply);
	int delete(int replyNo);
}
