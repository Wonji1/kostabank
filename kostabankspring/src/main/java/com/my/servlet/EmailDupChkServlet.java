package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "EmailDupChkServlet", value = "/emaildupchk")
public class EmailDupChkServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String email = request.getParameter("user_email");
        UserService service = new UserService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            service.findByEmail(email);
            jsonMap.put("status", -1);
            String jsonStr = mapper.writeValueAsString(jsonMap);
            out.print(jsonStr);
        } catch (FindException e) {
            jsonMap.put("status", 1);
            jsonMap.put("msg",e.getMessage());
            String jsonStr = mapper.writeValueAsString(jsonMap);
            out.print(jsonStr);
        }
    }

}
