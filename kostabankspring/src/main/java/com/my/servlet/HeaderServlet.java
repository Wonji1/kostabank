package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.FeedbackService;
import com.my.service.UserService;
import com.my.vo.Feedback;
import com.my.vo.User;

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

@WebServlet(name = "HeaderServlet", value = "/header")
public class HeaderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        FeedbackService fService = new FeedbackService();
        UserService uService = new UserService();
        ObjectMapper mapper = new ObjectMapper();

        List<Map> jacksonList = new ArrayList<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
        try {
            User user = uService.findById(id);
            List<Feedback> feedbacks = fService.feedbacksById(id);
            for(Feedback feedback : feedbacks){
                Map<String, Object> jacksonMap = new HashMap<>();
                jacksonMap.put("feedback_sort", feedback.getFeedback_sort());
                jacksonMap.put("feedback_id", feedback.getFeedback_id());
                jacksonMap.put("feedback_title", feedback.getFeedback_title());
                jacksonMap.put("feedback_date", feedback.getFeedback_date());

                jacksonList.add(jacksonMap);
            }
            Map<String,Object> jacksonMap2 = new HashMap<>();
            jacksonMap2.put("user_id",user.getUser_id());
            jacksonMap2.put("user_nickname",user.getUser_nickname());
            jacksonMap2.put("user_email",user.getUser_email());
            jacksonMap2.put("user_adm",user.getUser_adm());
            jacksonMap2.put("feedbacks",jacksonList);

            String jsonStr = mapper.writeValueAsString(jacksonMap2);
            out.print(jsonStr);
        } catch (FindException e) {
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("status", -1);
            jacksonMap.put("msg", "로그인을 진행x");
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        } catch (Exception e) {
            User user = null;
            try {
                user = uService.findById(id);
                Map<String, Object> jacksonMap = new HashMap<>();
                jacksonMap.put("user_id",user.getUser_id());
                jacksonMap.put("user_nickname",user.getUser_nickname());
                jacksonMap.put("user_email",user.getUser_email());
                jacksonMap.put("user_adm",user.getUser_adm());
                jacksonMap.put("status", -2);
                jacksonMap.put("msg", e.getMessage()); // 정보가 없을 때
                String jsonStr = mapper.writeValueAsString(jacksonMap);
                out.print(jsonStr);
            } catch (FindException ex) {
                Map<String, Object> jacksonMap = new HashMap<>();
                jacksonMap.put("status", -1);
                jacksonMap.put("msg", "로그인을 진행 .x");
                String jsonStr = mapper.writeValueAsString(jacksonMap);
                out.print(jsonStr);
            }

        }

    }
}