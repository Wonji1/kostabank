package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.QuestionService;
import com.my.vo.Question;

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

@WebServlet(name = "QTmpServlet", value = "/qtmp")
public class QTmpServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        QuestionService service = new QuestionService();
        ObjectMapper mapper = new ObjectMapper();

        Map<String,Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
        String row_num = request.getParameter("row_num");

        try {
            Question q = service.findQTmpByQId(id,row_num);
            jacksonMap.put("question_id",q.getQuestion_id());
            jacksonMap.put("content",q.getContent());
            jacksonMap.put("explanation",q.getExplanation());
            jacksonMap.put("correct_answer",q.getCorrect_answer());
            jacksonMap.put("correct_percent",((int)(((double)q.getCorrect_answer_count()/(double)q.getTotal_answer_count())*100)));
            jacksonMap.put("total",q.getMn_total());
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
