package com.jungbu.mybatis_board.dto;

import java.util.Date;

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
	private int boardNo; 
	private String title;    
	private String contents; 
	private java.util.Date postTime;
	private String userId;  
	private int views;    
	private int likes;//select count(*) from board_prefer weher prefer=1 AND board_no=no     
	private int bads; //select count(*) from prefer weher prefer=0 AND board_no=no     

}


