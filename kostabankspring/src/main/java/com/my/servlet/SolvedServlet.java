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

@WebServlet(name = "SolvedServlet",value = "/solved")
public class SolvedServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        QuestionService service = new QuestionService();
        ObjectMapper mapper = new ObjectMapper();

        List<Map> jacksonList = new ArrayList<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        try {
            List<Question> questionList = service.solvedById(id, 200);
            for(Question question : questionList){
                Map<String, Object> jacksonMap = new HashMap<>();
                jacksonMap.put("question_id",question.getQuestion_id());
                jacksonMap.put("content",question.getContent());
                jacksonMap.put("correct_answer",question.getCorrect_answer());
                jacksonMap.put("explanation",question.getExplanation());
                jacksonMap.put("correct_percent",((int)(((double)question.getCorrect_answer_count()/(double)question.getTotal_answer_count())*100)));
                jacksonMap.put("question_ox",question.getQuestion_ox());
                jacksonMap.put("mn_V",question.getMn_id());
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
