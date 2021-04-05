package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.BoardService;
import com.my.vo.Board;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class BoardFindByTitle
 */
@WebServlet("/boardfindbytitle")
public class BoardFindByTitle extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void service(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      response.setContentType("application/json;charset=utf-8");
      PrintWriter out = response.getWriter();
      BoardService service = new BoardService();
      ObjectMapper mapper = new ObjectMapper();
      List<Map> jacksonList = new ArrayList<>();
      String board_title = request.getParameter("board_title");
      
      System.out.println(board_title); //국비
      
      try {
         List<Board> boards = service.findByBoardTitle(board_title);
         // 결국 boarddaooracle에서 반환한 board객체들을 담은 list객체가 반환 됨.
         for (Board board : boards) {
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("board_id", board.getBoard_id());
            jacksonMap.put("user_nickname", board.getUser().getUser_nickname());
            jacksonMap.put("board_subtitle", board.getBoard_subtitle());
            jacksonMap.put("board_title", board_title);
            jacksonMap.put("board_content", board.getBoard_content());
            jacksonMap.put("board_wdate", board.getBoard_wdate());
            jacksonMap.put("board_view", board.getBoard_view());
            jacksonMap.put("board_file", board.getBoard_file());
            jacksonList.add(jacksonMap);
         }
         
         String jsonStr = mapper.writeValueAsString(jacksonList);
         out.print(jsonStr);
         
         System.out.println("boards: " + jsonStr); //boards : [{"board_subtitle" : "kosta~~~~
         
      } catch (FindException e) {
         e.printStackTrace();
         Map<String, Object> jacksonMap = new HashMap<>();
         jacksonMap.put("status", -1);
         jacksonMap.put("msg", e.getMessage());
         String jsonStr = mapper.writeValueAsString(jacksonMap);
         out.print(jsonStr);
      }
   }

}