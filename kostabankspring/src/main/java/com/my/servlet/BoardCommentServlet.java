package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.BoardService;
import com.my.service.UserService;
import com.my.vo.BoardComment;
import com.my.vo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/boardcomment")
public class BoardCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		String board_id = request.getParameter("board_id");
		BoardService service = new BoardService();
		List<Map<String,Object>> jacksonList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();

		HttpSession session = request.getSession();
		String loginedId = (String)session.getAttribute("loginInfo");
		User user = new User();
		UserService uService = new UserService();

		try {
			//댓글 보여주기
			List<BoardComment> comments = service.commentAll(board_id);
			for(BoardComment comment:comments) {
				Map<String,Object> jacksonMap = new HashMap<>();
				jacksonMap.put("user_nickname", comment.getUser());
				jacksonMap.put("comment_wdate", comment.getComment_wdate());
				jacksonMap.put("comment_content", comment.getComment_content());
				jacksonMap.put("status", 1);
				jacksonList.add(jacksonMap);
			}
			String jsonStr = mapper.writeValueAsString(jacksonList);
			out.print(jsonStr);
		} catch (FindException  e) {
			e.printStackTrace();
			Map<String,Object> jacksonMap = new HashMap<>();
			jacksonMap.put("status", -1);
			String jsonStr1 = mapper.writeValueAsString(jacksonMap);
			out.print(jsonStr1);
		}
	}

}
