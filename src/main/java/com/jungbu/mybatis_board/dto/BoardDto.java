package com.jungbu.mybatis_board.dto;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageInfo;

import lombok.Data;
/*
+-----------+--------------+------+-----+-------------------+-------------------+
| Field     | Type         | Null | Key | Default           | Extra             |
+-----------+--------------+------+-----+-------------------+-------------------+
| board_no  | int          | NO   | PRI | NULL              | auto_increment    |
| title     | varchar(255) | NO   |     | NULL              |                   |
| contents  | varchar(255) | YES  |     |                   |                   |
| post_time | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| user_id   | varchar(255) | NO   | MUL | NULL              |                   |
| views     | int          | NO   |     | 0                 |                   |
+-----------+--------------+------+-----+-------------------+-------------------+*/
@Data
public class BoardDto {
	private int boardNo; //PK
	private String title;    
	private String contents; 
	private java.util.Date postTime;
	private String userId; 
	private UserDto user; //FK:user_id //board : user = 1 : N
	private int views;    
	private int likes;//select count(*) from board_prefer weher prefer=1 AND board_no=no     
	private int bads; //select count(*) from board_prefer weher prefer=0 AND board_no=no     
	private PageInfo<ReplyDto> replyList; //board : reply = 1 : N
	private List<BoardImgDto> boardImgList;//board : boardImg = 1 : N
}










