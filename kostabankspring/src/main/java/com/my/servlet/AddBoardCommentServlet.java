package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.AddException;
import com.my.service.BoardService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class AddBoardCommentServlet
 */
@WebServlet("/addcomment")
public class AddBoardCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/josn; charset=utf-8");
		PrintWriter out = response.getWriter();
		Map<String, Object> jacksonMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();

		String board_id = request.getParameter("board_id");
		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("loginInfo");

		User user = new User();
		user.setUser_id(user_id);
		String comment_content = request.getParameter("comment_w_content");

		BoardComment comment = new BoardComment();
		comment.setBoard_id(board_id);
		comment.setComment_content(comment_content);
		comment.setUser(user);

		BoardService service = new BoardService();
		try {
			service.addBoardComment(comment);
			jacksonMap.put("status", 1);
			String jsonStr = mapper.writeValueAsString(jacksonMap);
			out.print(jsonStr);
		} catch (AddException e) {
			e.printStackTrace();
			jacksonMap.put("status", -1);
			String jsonStr = mapper.writeValueAsString(jacksonMap);
			out.print(jsonStr);
		}
	}
}
