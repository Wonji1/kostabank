package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.FeedbackService;
import com.my.vo.Qa;

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

@WebServlet(name = "MyQaServlet",value = "/myqa")
public class MyQaServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        FeedbackService service = new FeedbackService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
        int rownum = Integer.parseInt(request.getParameter("rownum"));

        try {
            Qa q = service.qaById(id, rownum);
            jacksonMap.put("qa_id",q.getQa_id());
            jacksonMap.put("user_nickname",q.getUser().getUser_nickname());
            jacksonMap.put("qa_title", q.getQa_title());
            jacksonMap.put("qa_content", q.getQa_content());
            jacksonMap.put("qa_wdate", q.getQa_wdate());
            jacksonMap.put("qa_file", q.getQa_file());
            jacksonMap.put("qa_new", q.getQa_new());
            jacksonMap.put("qa_status", q.getQa_status());
            jacksonMap.put("qa_sol_wdate", q.getQa_sol_wdate());
            jacksonMap.put("qa_sol_content", q.getQa_sol_content());
            jacksonMap.put("qa_total", q.getQa_total());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        } catch (FindException e) {
            e.printStackTrace();
            jacksonMap.put("status", -1);
            jacksonMap.put("msg", e.getMessage());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        }

    }

}
