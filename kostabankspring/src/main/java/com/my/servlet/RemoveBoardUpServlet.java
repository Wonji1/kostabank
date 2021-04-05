package com.my.servlet;

import com.my.exception.RemoveException;
import com.my.service.BoardService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class RemoveBoardUpServlet
 */
@WebServlet("/removeboardup")
public class RemoveBoardUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset= utf-8");

		String board_id = request.getParameter("board_id");
		BoardService service = new BoardService();

		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("loginInfo");

		try {
			service.removeBoardUp(board_id, user_id);
		} catch (RemoveException e) {
			e.printStackTrace();
		}
	}

}
