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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "SolveResultServlet", value = "/solveresult")
public class SolveResultServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        QuestionService service = new QuestionService();
        ObjectMapper mapper = new ObjectMapper();
        List<Map> jacksonList = new ArrayList<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
        String question_round = request.getParameter("question_round");

        try {
            List<Question> questionList = service.findAllQTmpByRound(id,question_round);
            for(Question question : questionList){
                Map<String, Object> jacksonMap = new HashMap<>();
                jacksonMap.put("question_id",question.getQuestion_id());
                jacksonMap.put("question_ox",question.getQuestion_ox());
                jacksonList.add(jacksonMap);
            }
            String jsonStr = mapper.writeValueAsString(jacksonList);
            out.print(jsonStr);
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
