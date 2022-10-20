package com.jungbu.mybatis_board.dto;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
/*
+---------+--------------+------+-----+-------------------+-------------------+
| Field   | Type         | Null | Key | Default           | Extra             |
+---------+--------------+------+-----+-------------------+-------------------+
| user_id | varchar(255) | NO   | PRI | NULL              |                   |
| name    | varchar(255) | NO   |     | NULL              |                   |
| pw      | varchar(255) | NO   |     | NULL              |                   |
| phone   | varchar(255) | NO   | UNI | NULL              |                   |
| email   | varchar(255) | NO   | UNI | NULL              |                   |
| birth   | date         | NO   |     | NULL              |                   |
| signup  | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
+---------+--------------+------+-----+-------------------+-------------------+
 * */
//@Getter
//@Setter
@Data
public class UserDto {
	private String userId;
	private String pw;
	private String name;
	private String email;
	private String phone;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date birth;	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date signup;
}
