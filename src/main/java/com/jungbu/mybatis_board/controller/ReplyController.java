package com.jungbu.mybatis_board.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		int insert=0;
		try {
			if(img!=null&&!img.isEmpty()) {
				String contentTypes[]=img.getContentType().split("/"); // image/jpeg, application/json
				if(contentTypes[0].equals("image")) {
					String fileName="reply_"+System.currentTimeMillis()+"_"+((int)(Math.random()*10000))+"."+contentTypes[1];
					Path path=Paths.get(imgPath+"/"+fileName);
					img.transferTo(path);	
					reply.setImgPath(fileName);					
				}
			}
			insert=replyMapper.insert(reply);
			System.out.println(insert);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/board/detail.do?boardNo="+reply.getBoardNo();
	}
	@GetMapping("/update.do")
	public void update(
			@RequestParam(required=true) int replyNo,
			Model model) {
		ReplyDto reply=null;
		try {
			reply=replyMapper.detail(replyNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("reply", reply);
	}	
	
	
	@GetMapping("/delete.do")
	public String delete(
			@RequestParam(required=true) int replyNo,
			@SessionAttribute UserDto loginUser,
			HttpSession session
			) {
		ReplyDto reply=null;
		int delete=0;
		try {
			reply=replyMapper.detail(replyNo);
			if(reply.getUserId().equals(loginUser.getUserId())) {
				delete=replyMapper.delete(replyNo);
				if(delete>0&&reply.getImgPath()!=null) {
					File imgFile=new File(imgPath+"/"+reply.getImgPath());
					System.out.println("이미지 파일 삭제:"+imgFile.delete());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msg=(delete>0)?"삭제 성공":"삭제 실패";
		session.setAttribute("msg", msg);
		return "redirect:/board/detail.do?boardNo="+reply.getBoardNo();
	}
	@PostMapping("/update.do")
	public String update(
			ReplyDto reply,
			@SessionAttribute UserDto loginUser,
			HttpSession session,
			MultipartFile img
			) {
		int update=0;
		try {
			if(img!=null&& !img.isEmpty()) {
				String[]contensTypes=img.getContentType().split("/");
				if(contensTypes[0].equals("image")) {
					String fileName="reply_"+System.currentTimeMillis()+"_"+((int)(Math.random()*10000))+"."+contensTypes[1];
					Path path=Paths.get(imgPath+"/"+fileName);
					img.transferTo(path);	
					if(reply.getImgPath()!=null) {
						File imgFile=new File(imgPath+"/"+reply.getImgPath());
						System.out.println("이미지 파일 삭제:"+imgFile.delete());
					}
					reply.setImgPath(fileName);					
				}
			}
			update=replyMapper.update(reply);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msg=(update>0)?"수정 성공":"수정 실패";
		session.setAttribute("msg", msg);
		return "redirect:/board/detail.do?boardNo="+reply.getBoardNo();

	}
}



