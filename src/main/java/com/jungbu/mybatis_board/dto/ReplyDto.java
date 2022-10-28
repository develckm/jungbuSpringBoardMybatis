package com.jungbu.mybatis_board.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
/*
+-------------+--------------+------+-----+-------------------+-------------------+
| Field       | Type         | Null | Key | Default           | Extra             |
+-------------+--------------+------+-----+-------------------+-------------------+
| reply_no    | int          | NO   | PRI | NULL              | auto_increment    |
| title       | varchar(255) | NO   |     | NULL              |                   |
| contents    | varchar(255) | YES  |     |                   |                   |
| post_time   | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| img_path    | varchar(255) | YES  |     | NULL              |                   |
| board_no    | int          | NO   | MUL | NULL              |                   |
| user_id     | varchar(255) | NO   | MUL | NULL              |                   |
| fk_reply_no | int          | YES  | MUL | NULL              |                   |
+-------------+--------------+------+-----+-------------------+-------------------+*/
@Data
public class ReplyDto {
	private int replyNo; //PK 
	private int boardNo; //FK: baord.baord_no 
	private String title;    
	private String contents; 
	private String imgPath; 
	private java.util.Date postTime;
	private String userId;//FK: user.user_id
	private int likes;//select count(*) from reply_prefer weher prefer=1 AND board_no=no     
	private int bads; //select count(*) from reply_prefer weher prefer=0 AND board_no=no     
	
	private Integer fkReplyNo; //FK : self join(reply.reply_no is null)
	private List<ReplyDto> replyList; //reply : reply = 1 : N   대 댓글
}


