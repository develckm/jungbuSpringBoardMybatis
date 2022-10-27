package com.jungbu.mybatis_board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jungbu.mybatis_board.dto.ReplyDto;
import com.jungbu.mybatis_board.dto.SearchDto;
import com.jungbu.mybatis_board.mapper.ReplyMapper;

@Service
public class ReplyService {
	@Autowired
	ReplyMapper replyMapper;
	public PageInfo<ReplyDto> readReplysPaging(int boardNo,SearchDto search){
		PageHelper.startPage(search.getPage(), search.getRows(), search.getOrderBy());
		return PageInfo.of(replyMapper.list(boardNo));
	}
}
