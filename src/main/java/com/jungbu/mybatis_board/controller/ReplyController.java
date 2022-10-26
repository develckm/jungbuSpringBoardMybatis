package com.jungbu.mybatis_board.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jungbu.mybatis_board.dto.ReplyDto;
import com.jungbu.mybatis_board.dto.UserDto;
import com.jungbu.mybatis_board.mapper.ReplyMapper;

import lombok.Data;

@RequestMapping("/reply")
@Controller
public class ReplyController {
	@Value("${spring.servlet.multipart.location}")
	String imgPath;
	@Autowired
	ReplyMapper replyMapper;
	@Data
	class CheckStatus{
		private int status;
		//{status : 0:등록실패, 1:성공, -1:로그인하세요,-2: 글쓴이만 수정가능}
	}
	@PostMapping("/insert.do")
	public @ResponseBody CheckStatus insert(
			ReplyDto reply,
			MultipartFile img,
			@SessionAttribute(required=false) UserDto loginUser
			) {
		CheckStatus checkStatus=new CheckStatus();
		if(loginUser!=null) {
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
			checkStatus.setStatus(insert); //0 or 1
		}else {
			checkStatus.setStatus(-1);
		}
		return checkStatus;
	}
	@GetMapping("/update.do")
	public String update(
			@SessionAttribute UserDto loginUser, //status:400
			@RequestParam(required=true) int replyNo,
			Model model,
			HttpServletResponse resp
			) {
		ReplyDto reply=null;
		reply=replyMapper.detail(replyNo);//status: 500
		
		if(loginUser.getUserId().equals(reply.getUserId())) {
			System.out.println(reply);
			model.addAttribute("reply", reply);		
			return "/reply/update"; //status:200
		}else {
			resp.setStatus(401);//Unauthorized
			return null;
		}
	}	
	
	
	@GetMapping("/delete.do")
	public @ResponseBody CheckStatus delete(
			@RequestParam(required=true) int replyNo,
			@SessionAttribute(required=false) UserDto loginUser
			) {
		CheckStatus checkStatus=new CheckStatus();
		if(loginUser==null) {
			checkStatus.setStatus(-1);
			return checkStatus;
		}
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
			}else{
				checkStatus.setStatus(-2);
				return checkStatus;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		checkStatus.setStatus(delete);
		return checkStatus;
	}
	@PostMapping("/update.do")
	public @ResponseBody CheckStatus update(
			ReplyDto reply,
			@SessionAttribute(required=false) UserDto loginUser,
			MultipartFile img
			) {
		CheckStatus checkStatus=new CheckStatus();
		if(loginUser==null) {
			checkStatus.setStatus(-1);
			return checkStatus;
		}else if(!loginUser.getUserId().equals(reply.getUserId())) {
			checkStatus.setStatus(-2);
			return checkStatus;
		}
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
		checkStatus.setStatus(update);
		return checkStatus;
	}
	@GetMapping("/{boardNo}/list.do")
	public String list(
			@RequestParam(defaultValue="1") int page,
			@PathVariable int boardNo,
			Model model
			) {
		Page<ReplyDto> replyList=null;
		int ROWS=5;
		PageHelper.startPage(page, ROWS);
		replyList=replyMapper.list(boardNo);
		
		model.addAttribute("replyList",replyList);
		model.addAttribute("paging",PageInfo.of(replyList,5));

		return "/reply/list";
	}
	
	
	
	
	
}



