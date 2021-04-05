package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.FeedbackService;
import com.my.vo.Report;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ReportServlet", value = "/report")
public class ReportServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        FeedbackService service = new FeedbackService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();
        String report_id = request.getParameter("report_id");
        int New = Integer.parseInt(request.getParameter("n"));
        int status = Integer.parseInt(request.getParameter("s"));

        try {
            Report report = service.findReportById(report_id,New,status);
            jacksonMap.put("report_id",report.getReport_id());
            jacksonMap.put("user_nickname",report.getUser().getUser_nickname());
            jacksonMap.put("content",report.getQuestion().getContent());
            jacksonMap.put("correct_answer",report.getQuestion().getCorrect_answer());
            jacksonMap.put("explanation",report.getQuestion().getExplanation());
            jacksonMap.put("report_title",report.getReport_title());
            jacksonMap.put("report_content",report.getReport_content());
            jacksonMap.put("report_wdate",report.getReport_wdate());
            jacksonMap.put("report_new",report.getReport_new());
            jacksonMap.put("report_status",report.getReport_status());
            jacksonMap.put("report_sol_wdate",report.getReport_sol_wdate());
            jacksonMap.put("report_sol_content",report.getReport_sol_content());
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
