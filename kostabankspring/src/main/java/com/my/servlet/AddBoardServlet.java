package com.my.servlet;
//package com.my.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.my.exception.AddException;
//import com.my.exception.FindException;
//import com.my.model.RenamePolicy;
//import com.my.service.BoardService;
//import com.my.vo.Board;
//import com.my.vo.User;
//import com.oreilly.servlet.MultipartRequest;
//import com.oreilly.servlet.multipart.FileRenamePolicy;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//
//@WebServlet("/addboard")
//public class AddBoardServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.setContentType("application/json; charset=utf-8");
//		PrintWriter out = response.getWriter();
//		Map <String, Object> jacksonMap = new HashMap<>();
//		ObjectMapper mapper = new ObjectMapper();
//		BoardService service = new BoardService();
//		try {
//			int nextBoardId = service.findNextBoardId();
//			File uploadFile =  new File(this.getServletContext().getRealPath("/")+ "/boardupload");
//			if(!uploadFile.exists()){
//				uploadFile.mkdir();
//			}
//			String saveDirectory = getServletContext().getRealPath("boardupload");
//
//			int maxPostSize = 10*1024*1024;
//			String encoding = "UTF-8";
//			FileRenamePolicy policy = new RenamePolicy(String.valueOf(nextBoardId));
//			MultipartRequest mr = new MultipartRequest(request, saveDirectory,maxPostSize, encoding, policy);
//			String board_subtitle = mr.getParameter("subtitle");
//			String board_title = mr.getParameter("title");
//			String board_content = mr.getParameter("board_content");
//			String board_file = mr.getOriginalFileName("board_file");
//
//			HttpSession session = request.getSession();
//			String loginedId = (String)session.getAttribute("loginInfo");
//			User user = new User();
//			Board board = new Board();
//
//			user.setUser_id(loginedId);
//			board.setUser(user); //글쓴이 아이디
//			board.setBoard_id(String.valueOf(nextBoardId));//글번호
//			board.setBoard_title(board_title);//제목
//			board.setBoard_subtitle(board_subtitle);//소제목
//			board.setBoard_content(board_content); //내용
//			board.setBoard_file(board_file);//첨부파일이름
//			service.addBoard(board);
//
//			jacksonMap.put("status", 1);
//			String jsonStr = mapper.writeValueAsString(jacksonMap);
//			out.print(jsonStr);
//
//		} catch (AddException | FindException e) {
//			e.printStackTrace();
//			jacksonMap.put("status", -1);
//			jacksonMap.put("msg", e.getMessage());
//			String jsonStr = mapper.writeValueAsString(jacksonMap);
//			out.print(jsonStr);
//		}
//	}
//}
