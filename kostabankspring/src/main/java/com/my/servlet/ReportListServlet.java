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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ReportListServlet",value = "/reportlist")
public class ReportListServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        FeedbackService service = new FeedbackService();
        ObjectMapper mapper = new ObjectMapper();
        List<Map> jacksonList = new ArrayList<>();
        int page = Integer.parseInt(request.getParameter("page"));
        int num = Integer.parseInt(request.getParameter("num"));

        try {
            List<Report> reports = service.reportAll(page,num);
            for(Report report : reports) {
                Map<String, Object> jacksonMap = new HashMap<>();
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
                jacksonMap.put("report_total",report.getReport_total());
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
