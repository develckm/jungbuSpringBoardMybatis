package com.jungbu.mybatis_board.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.jungbu.mybatis_board.dto.ReplyDto;
import com.jungbu.mybatis_board.dto.UserDto;
import com.jungbu.mybatis_board.mapper.ReplyMapper;

@RequestMapping("/reply")
@Controller
public class ReplyController {
	@Value("${spring.servlet.multipart.location}")
	String imgPath;
	@Autowired
	ReplyMapper replyMapper;
	@PostMapping("/insert.do")
	public String insert(
			ReplyDto reply,
			MultipartFile img,
			@SessionAttribute UserDto loginUser
			) {
		reply.setUserId(loginUser.getUserId());
		String contentTypes[]=img.getContentType().split("/"); // image/jpeg, application/json
		int insert=0;
		try {
			if(img!=null&&!img.isEmpty()&&contentTypes[0].equals("image")) {
				String fileName="reply_"+System.currentTimeMillis()+"_"+((int)(Math.random()*10000))+"."+contentTypes[1];
				Path path=Paths.get(imgPath+"/"+fileName);
				img.transferTo(path);	
				reply.setImgPath(fileName);
			}
			insert=replyMapper.insert(reply);
			System.out.println(insert);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/board/detail.do?boardNo="+reply.getBoardNo();
	}
}
