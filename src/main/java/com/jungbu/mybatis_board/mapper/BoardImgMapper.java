package com.jungbu.mybatis_board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.jungbu.mybatis_board.dto.BoardImgDto;
//com.jungbu.mybatis_board.mapper.BoardImgMapper
@Mapper
public interface BoardImgMapper {
	int insert(BoardImgDto boardImg);
}
