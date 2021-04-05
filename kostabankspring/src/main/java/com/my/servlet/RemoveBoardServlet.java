package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.RemoveException;
import com.my.service.BoardService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class RemoveBoardServlet
 */
@WebServlet("/removeboard")
public class RemoveBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset:utf-8");
		PrintWriter out = response.getWriter();
		Map<String, Object> jacksonMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();

		String board_id = request.getParameter("board_id");
		BoardService service = new BoardService();

		try {
			service.removeBoardById(board_id);
			jacksonMap.put("status", 1);
			String jsonStr = mapper.writeValueAsString(jacksonMap);
			out.print(jsonStr);
		} catch (RemoveException e) {
			e.printStackTrace();
			jacksonMap.put("status", -1);
			jacksonMap.put("msg", e.getMessage());
			String jsonStr = mapper.writeValueAsString(jacksonMap);
			out.print(jsonStr);
		}

	}

}
