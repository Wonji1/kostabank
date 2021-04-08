package com.my.controller;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.model.RenamePolicy;
import com.my.service.BoardService;
import com.my.vo.Board;
import com.my.vo.BoardComment;
import com.my.vo.User;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ResponseBody
@Controller
@RequestMapping("/board/*")
@Log4j
public class BoardController {
    @Autowired
    private BoardService service;

    @RequestMapping("/boarddetail")
    public Map<String, Object> boardDetail(@RequestParam(value = "board_id") String board_id
    		, HttpServletRequest request) throws FindException {
        Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        session.setAttribute("board_id", board_id);
        
        Board board;
        board = service.boardById(board_id);
        jacksonMap.put("board_id",board.getBoard_id());
        jacksonMap.put("user_nickname",board.getUser().getUser_nickname());
        jacksonMap.put("board_subtitle",board.getBoard_subtitle());
        jacksonMap.put("board_title",board.getBoard_title());
        jacksonMap.put("board_content",board.getBoard_content());
        jacksonMap.put("board_wdate",board.getBoard_wdate());
        jacksonMap.put("board_view",board.getBoard_view());
        jacksonMap.put("board_file",board.getBoard_file());
        jacksonMap.put("board_up", board.getBoard_up());
        return jacksonMap;

    }
    
    @RequestMapping("/boardlist")
    public List<Map<String, Object>> boardList(@RequestParam(value = "page") int page, @RequestParam(value = "num") int num) throws FindException {

        List<Map<String,Object>> jacksonList = new ArrayList<>();
        
        List<Board> boards = service.boardAll(page,num);
        for(Board board : boards) {
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("board_id",board.getBoard_id());
            jacksonMap.put("user_nickname",board.getUser().getUser_nickname());
            jacksonMap.put("board_subtitle",board.getBoard_subtitle());
            jacksonMap.put("board_title",board.getBoard_title());
            jacksonMap.put("board_content",board.getBoard_content());
            jacksonMap.put("board_wdate",board.getBoard_wdate());
            jacksonMap.put("board_view",board.getBoard_view());
            jacksonMap.put("board_file",board.getBoard_file());
            jacksonMap.put("board_total",board.getBoard_total());
            jacksonList.add(jacksonMap);
        }
        return jacksonList;

    }
    
    @RequestMapping("/boardcomment")
    public List<Map<String, Object>> boardComment(@RequestParam(value = "board_id") String board_id) throws FindException {

        List<Map<String,Object>> jacksonList = new ArrayList<>();
        
		List<BoardComment> comments = service.commentAll(board_id);
		for(BoardComment comment:comments) {
			Map<String,Object> jacksonMap = new HashMap<>();
			jacksonMap.put("user_nickname", comment.getUser());
			jacksonMap.put("comment_wdate", comment.getComment_wdate());
			jacksonMap.put("comment_content", comment.getComment_content());
			jacksonMap.put("status", 1);
			jacksonList.add(jacksonMap);
		}
        
        return jacksonList;

    }
    
    @RequestMapping("/addcomment")
    public Map<String, Object> addboardComment(@RequestParam(value = "board_id") String board_id
    		, @RequestParam(value = "comment_w_content") String comment_w_content
    		, HttpServletRequest request) throws AddException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
		HttpSession session = request.getSession();
        
		String user_id = (String)session.getAttribute("loginInfo");

		User user = new User();
		user.setUser_id(user_id);
		
    	BoardComment comment = new BoardComment();
		comment.setBoard_id(board_id);
		comment.setComment_content(comment_w_content);
		comment.setUser(user);
    	
		service.addBoardComment(comment);
		jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/boardup")
    public Map<String, Object> addboardup(@RequestParam(value = "board_id") String board_id
    		, HttpServletRequest request) throws AddException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
		HttpSession session = request.getSession();
        
		String user_id = (String)session.getAttribute("loginInfo");

		service.addBoardUp(board_id, user_id);
		jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/removeboardup")
    public void removeboardup(@RequestParam(value = "board_id") String board_id
    		, HttpServletRequest request) throws RemoveException {
		
		HttpSession session = request.getSession();
        
		String user_id = (String)session.getAttribute("loginInfo");

		service.removeBoardUp(board_id, user_id);

    }
    
    @RequestMapping("/addboard")
    public Map<String, Object> addboard(HttpServletRequest request
    		) throws AddException, FindException, IOException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
		HttpSession session = request.getSession();
        
		String user_id = (String)session.getAttribute("loginInfo");

		
		int nextBoardId = service.findNextBoardId();
		File uploadFile =  new File(request.getServletContext().getRealPath("/")+ "/boardupload");
		if(!uploadFile.exists()){
			uploadFile.mkdir();
		}
		String saveDirectory = request.getServletContext().getRealPath("boardupload");

		int maxPostSize = 10*1024*1024;
		String encoding = "UTF-8";
		FileRenamePolicy policy = new RenamePolicy(String.valueOf(nextBoardId));
		MultipartRequest mr = new MultipartRequest(request, saveDirectory,maxPostSize, encoding, policy);
		String board_subtitle = mr.getParameter("subtitle");
		String board_title = mr.getParameter("title");
		String board_content = mr.getParameter("board_content");
		String board_file = mr.getOriginalFileName("board_file");
		
		User user = new User();
		Board board = new Board();

		user.setUser_id(user_id);
		board.setUser(user); //글쓴이 아이디
		board.setBoard_id(String.valueOf(nextBoardId));//글번호
		board.setBoard_title(board_title);//제목
		board.setBoard_subtitle(board_subtitle);//소제목
		board.setBoard_content(board_content); //내용
		board.setBoard_file(board_file);//첨부파일이름
		service.addBoard(board);

		jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/modboard")
    public Map<String, Object> modboard(HttpServletRequest request
    		) throws ModifyException, FindException, IOException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
		HttpSession session = request.getSession();
        
		String user_id = (String)session.getAttribute("loginInfo");
		String board_id = (String)session.getAttribute("board_id");
		
		int nextBoardId = service.findNextBoardId();
		File uploadFile =  new File(request.getServletContext().getRealPath("/")+ "/boardupload");
		if(!uploadFile.exists()){
			uploadFile.mkdir();
		}
		String saveDirectory = request.getServletContext().getRealPath("boardupload");

		int maxPostSize = 10*1024*1024;
		String encoding = "UTF-8";
		FileRenamePolicy policy = new RenamePolicy(String.valueOf(nextBoardId));
		MultipartRequest mr = new MultipartRequest(request, saveDirectory,maxPostSize, encoding, policy);
		Board newBoard = new Board();
		String newSubtitle = mr.getParameter("subtitle"); //수정 소제목
		String newTitle = mr.getParameter("title"); //수정 제목
		String newContent = mr.getParameter("board_content");//수정 내용
		String newFile = mr.getOriginalFileName("board_file"); //수정파일
		Enumeration<String> e = mr.getFileNames(); //파일 이름들만 얻어옴
		while(e.hasMoreElements()) {
			String fileName = e.nextElement();
			System.out.println("fileName = " + fileName);
			System.out.println("OriginalFileName = " + mr.getOriginalFileName(fileName));
		}
		newBoard.setBoard_subtitle(newSubtitle);
		newBoard.setBoard_title(newTitle);
		newBoard.setBoard_content(newContent);
		newBoard.setBoard_file(newFile);
		newBoard.setBoard_id(board_id);//글번호
		User user = new User();
		user.setUser_id(user_id);
		newBoard.setUser(user);//로그인아이디

		Board oldBoard = new Board();

		oldBoard = service.boardById(board_id);
		String oldSubtitle = oldBoard.getBoard_subtitle();
		String oldTitle = oldBoard.getBoard_title();
		String oldContent = oldBoard.getBoard_content();
		String oldFile = oldBoard.getBoard_file();
		oldBoard.setUser(user);
		System.out.println("old : " + oldBoard);

		if(!(oldSubtitle.equals(newSubtitle))) {
			oldBoard.setBoard_subtitle(newSubtitle);
		}
		if(!(oldTitle.equals(newTitle))) {
			oldBoard.setBoard_title(newTitle);
		}
		if(!(oldContent.equals(newContent))) {
			oldBoard.setBoard_content(newContent);
		}
		if(newFile != null) {
			oldBoard.setBoard_file(newFile);
		}else {
			oldBoard.setBoard_file(null);
		}
		service.modifyBoard(oldBoard);

		jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/removeboard")
    public Map<String, Object> removeboard(@RequestParam(value = "board_id") String board_id) throws RemoveException {
		
		Map<String, Object> jacksonMap = new HashMap<>();

		service.removeBoardById(board_id);
		jacksonMap.put("status", 1);
		return jacksonMap;

    }
    
    @RequestMapping("/modboardview")
    public Map<String, Object> modboardview(@RequestParam(value = "board_id") String board_id) throws ModifyException {
		
		Map<String, Object> jacksonMap = new HashMap<>();

		service.modifyBoardView(board_id);
		jacksonMap.put("status", 1);
		return jacksonMap;

    }
    
    @RequestMapping("/removeboardbylist")
    public Map<String, Object> removeboardbylist(@RequestParam(value = "board_id_list") String[] board_id_list) throws RemoveException {
		
		Map<String, Object> jacksonMap = new HashMap<>();

		service.removeBoardByList(board_id_list);
		jacksonMap.put("status", 1);
		return jacksonMap;

    }
    
    @RequestMapping("/findboardup")
    public Map<String, Object> findboardup(@RequestParam(value = "board_id") String board_id
    		, HttpServletRequest request) throws FindException {
		
		Map<String, Object> jacksonMap = new HashMap<>();
		HttpSession session = request.getSession();
        
		String user_id = (String)session.getAttribute("loginInfo");

		service.findBoardUp(user_id, board_id);
		jacksonMap.put("status", 1);
		return jacksonMap;

    }
    
}
