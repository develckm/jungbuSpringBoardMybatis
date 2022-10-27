package com.jungbu.mybatis_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jungbu.mybatis_board.dto.BoardImgDto;
//com.jungbu.mybatis_board.mapper.BoardImgMapper
@Mapper
public interface BoardImgMapper {
	List<BoardImgDto> list();
	int insert(BoardImgDto boardImg);
}
