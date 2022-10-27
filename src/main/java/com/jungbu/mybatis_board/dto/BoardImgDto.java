package com.jungbu.mybatis_board.dto;

import lombok.Data;
/*
+--------------+--------------+------+-----+---------+----------------+
| Field        | Type         | Null | Key | Default | Extra          |
+--------------+--------------+------+-----+---------+----------------+
| board_img_no | int          | NO   | PRI | NULL    | auto_increment |
| board_no     | int          | NO   | MUL | NULL    |                |
| img_path     | varchar(255) | NO   |     | NULL    |                |
+--------------+--------------+------+-----+---------+----------------+
 * */
@Data
public class BoardImgDto {
	private int boardImgNo;
	private int boardNo;
	private String imgPath;
}
