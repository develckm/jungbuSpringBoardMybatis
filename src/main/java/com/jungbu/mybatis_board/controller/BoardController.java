package com.jungbu.mybatis_board.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.fasterxml.jackson.core.io.UTF32Reader;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jungbu.mybatis_board.dto.BoardDto;
import com.jungbu.mybatis_board.dto.SearchDto;
import com.jungbu.mybatis_board.dto.UserDto;
import com.jungbu.mybatis_board.mapper.BoardMapper;
import com.jungbu.mybatis_board.service.BoardService;
@RequestMapping("/board")
@Controller
public class BoardController {
	@Autowired
	BoardMapper boardMapper;
	@Autowired
	BoardService boardService;
	@Value("${spring.servlet.multipart.location}")
	String imgSavePath;
	
	@GetMapping("/list.do")
	public String list(
			Model model,
			SearchDto search
			) {
		final int ROWS=5;
		PageInfo<BoardDto> paging=null; //네비게이션이 포함된 페이징 정보
		try {
			//list 쿼리를 실행하기 전에 PageHelper.startPage만 호출하면 자동으로 Paging과 count 쿼리를 실행 
			if(search.getOrderBy()==null)
				search.setOrderBy("board_no DESC");
			paging=boardService.readBoardPaging(search);
			//System.out.println(paging);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("paging", paging);
		return "/board/list";
	}
	@GetMapping("/detail.do")
	public String detail(
			@RequestParam(required=true)int boardNo,
			Model model,
			SearchDto search
			){
		BoardDto board=null;
		try {
			//PageHeler는 1:N join된 replyList를 페이징 할 수 없다! (join x)
			board=boardService.readBoardWithReplysPaging(boardNo, search);
			boardMapper.viewUpdate(boardNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(board!=null) {			
			model.addAttribute("board", board);
			return "/board/detail";
		}else {
			return "redirect:/board/list.do";
		}
	}
	@GetMapping("/update.do")
	public String update(
			@RequestParam(required=true) int boardNo,
			Model model,
			@SessionAttribute(required=false) UserDto loginUser,
			HttpSession session
			) {
		BoardDto board=null;
		String msg="";
		try {
			if(loginUser!=null) {
				board=boardMapper.detail(boardNo);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(board!=null&&board.getUserId().equals(loginUser.getUserId())) {
			model.addAttribute("board",board);
			return "/board/update";			
		}else {
			msg=(loginUser==null)?"로그인하셔야 이용할 수 있습니다.":"글쓴이만 수정 가능 합니다.";
			session.setAttribute("msg", msg);
			return "redirect:/board/detail.do?boardNo="+boardNo;
		}
	}
	@PostMapping("/update.do")
	public String update(
			BoardDto board,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session
			) {
		int update=0;
		String msg="";
		try {
			if(loginUser==null) {
				msg="로그인 하셔야 합니다.";
			}else {
				if(board.getUserId().equals(loginUser.getUserId())) {
					update=boardMapper.update(board);					
				}else {
					msg="글쓴이만 수정 가능 합니다.";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(update>0) {
			msg="수정 성공";
			session.setAttribute("msg", msg);
			return "redirect:/board/detail.do?boardNo="+board.getBoardNo();			
		}else {
			msg="수정 실패(db 오류)";
			session.setAttribute("msg", msg);
			return "redirect:/board/update.do?boardNo="+board.getBoardNo();			
		}
	}
	@GetMapping("/delete.do")
	public String delete(
			@RequestParam(required = true) int boardNo,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session
			) {
		int delete=0;
		String msg="";
		try {
			if(loginUser!=null) {
				BoardDto board=boardMapper.detail(boardNo);
				if(board.getUserId().equals(loginUser.getUserId())) {
					delete=boardMapper.delete(boardNo);
				}else {
					msg="글쓴이만 삭제 가능";
				}
			}else {
				msg="로그인해야 게시글 삭제 가능";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(delete>0) {
			msg="삭제 성공";
			session.setAttribute("msg", msg);
			return "redirect:/board/list.do";			
		}else {
			msg="삭제 실패(db 오류)";
			session.setAttribute("msg", msg);
			return "redirect:/board/update.do?boardNo="+boardNo;			
		}
	}
	@GetMapping("/insert.do")
	public String insert(
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session
			) {
		String msg="";
		if(loginUser!=null) {
			return "/board/insert";
		}else {
			msg="로그인하셔야 게시글을 작성할 수 있습니다.";
			session.setAttribute("msg", msg);
			return "redirect:/user/login.do";
		}
	}
	@PostMapping("/insert.do")
	public String insert(
			BoardDto board,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session,
			@RequestParam(name="img") MultipartFile [] imgs
			) {
		int insert=0;
		String msg="";
		ArrayList<String> imgPaths= new ArrayList<String>(); //db에 저장할 이름
		try {
			for(MultipartFile img: imgs) {
				if(!img.isEmpty()) {
					String [] contentTypes=img.getContentType().split("/");
					if(contentTypes[0].equals("image")) {
						String fileName="board_"+System.currentTimeMillis()+"_"+((int)(Math.random()*10000))+"."+contentTypes[1];
						Path path=Paths.get(imgSavePath+"/"+fileName);
						img.transferTo(path);
						imgPaths.add(fileName);
					}
				}
			}
			if(loginUser!=null) {
				insert=boardService.registBoardAndBoardImgs(board, imgPaths);		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert>0) {
			msg="게시글 작성 성공";
			session.setAttribute("msg", msg);
			return "redirect:/board/detail.do?boardNo="+board.getBoardNo();			
		}else {
			if(loginUser==null) {
				msg="로그인 한 유저만 게시글 작성 가능합니다.";
			}else {
				msg="게시글 작성 실패(db 오류)";
			}
			session.setAttribute("msg", msg);
			return "redirect:/board/insert.do";
		}
	}
}





