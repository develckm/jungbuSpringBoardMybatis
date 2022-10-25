package com.jungbu.mybatis_board.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.jungbu.mybatis_board.dto.ReplyDto;

@RequestMapping("/reply")
@Controller
public class ReplyController {
	@Value("${spring.servlet.multipart.location}")
	String imgPath;
	
	@PostMapping("/insert.do")
	public String insert(
			ReplyDto reply,
			MultipartFile img
			) {
		
		//img.
		System.out.println(reply);
		System.out.println(img.isEmpty());
		String contentTypes[]=img.getContentType().split("/"); // image/jpeg, application/json
		System.out.println(Arrays.toString(contentTypes));
		try {
			if(img!=null&&!img.isEmpty()&&contentTypes[0].equals("image")) {
				String fileName="reply_"+System.currentTimeMillis()+"_"+((int)(Math.random()*10000))+"."+contentTypes[1];
				Path path=Paths.get(imgPath+"/"+fileName);
				img.transferTo(path);	
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/board/detail.do?boardNo="+reply.getBoardNo();
	}
}
