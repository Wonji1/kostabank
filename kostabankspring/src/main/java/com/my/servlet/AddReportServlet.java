package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.AddException;
import com.my.service.FeedbackService;
import com.my.vo.Question;
import com.my.vo.Report;
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

@WebServlet(name = "AddReportServlet", value = "/addreport")
public class AddReportServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        FeedbackService service = new FeedbackService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
        String report_title = request.getParameter("report_title");
        String report_content = request.getParameter("report_content");
        String report_question = request.getParameter("report_question");

        try {
            Report report = new Report();
            User user = new User();
            Question question = new Question();
            user.setUser_id(id);
            question.setQuestion_id(report_question);
            report.setUser(user);
            report.setReport_title(report_title);
            report.setReport_content(report_content);
            report.setQuestion(question);
            service.addReport(report);
            jacksonMap.put("status", 1);
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        } catch (AddException e) {
            e.printStackTrace();
            jacksonMap.put("status", -1);
            jacksonMap.put("msg", e.getMessage());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        }
    }

}
