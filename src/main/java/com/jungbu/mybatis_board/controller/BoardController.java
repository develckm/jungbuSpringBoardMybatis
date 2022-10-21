package com.jungbu.mybatis_board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jungbu.mybatis_board.dto.BoardDto;
import com.jungbu.mybatis_board.mapper.BoardMapper;
@RequestMapping("/board")
@Controller
public class BoardController {
	@Autowired
	BoardMapper boardMapper;
	@GetMapping("/list.do")
	public String list(
			Model model,
			@RequestParam(defaultValue="1")int page
			) {
		final int ROWS=10;
		int starRow=(page-1)*ROWS;
		List<BoardDto> boardList=null;
		try {
			boardList=boardMapper.list(starRow, ROWS);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("boardList", boardList);
		return "/board/list";
	}
	@GetMapping("/detail.do")
	public String detail(
			@RequestParam(required=true)int boardNo,
			Model model
			){
		BoardDto board=null;
		try {
			board=boardMapper.detail(boardNo);
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
			Model model
			) {
		BoardDto board=null;
		try {
			board=boardMapper.detail(boardNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(board!=null) {
			model.addAttribute("board",board);
			return "/board/update";			
		}else {
			return "redirect:/board/detail.do?boardNo="+boardNo;
		}
	}
}





