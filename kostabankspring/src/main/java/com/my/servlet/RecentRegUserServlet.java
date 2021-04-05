package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.UserService;
import com.my.vo.User;

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

@WebServlet(name = "RecentRegUserServlet", value = "/recentreguser")
public class RecentRegUserServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        UserService service = new UserService();
        ObjectMapper mapper = new ObjectMapper();
        List<Map> jacksonList = new ArrayList<>();
        try {
            List<User> userList = service.findNAllByN(11);
            for(User user: userList){
                Map<String, Object> jacksonMap = new HashMap<>();
                jacksonMap.put("user_id", user.getUser_id());
                jacksonMap.put("user_date",user.getUser_date());
                jacksonList.add(jacksonMap);
            }
            String jsonStr = mapper.writeValueAsString(jacksonList);
            out.print(jsonStr);
        } catch (FindException e) {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", -1);
            jsonMap.put("msg", e.getMessage());
            String jsonStr = mapper.writeValueAsString(jsonMap);
            out.print(jsonStr);
        }
    }
}
