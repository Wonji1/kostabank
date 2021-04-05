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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UserInfoServlet", value = "/userinfo")
public class UserInfoServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
        UserService service = new UserService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();
        try {
            User user = service.findById(id);
            jacksonMap.put("user_id",user.getUser_id());
            jacksonMap.put("user_nickname",user.getUser_nickname());
            jacksonMap.put("user_email",user.getUser_email());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        } catch (FindException e) {
            out.print("{\"status\" : -1}");
        }

    }
}
