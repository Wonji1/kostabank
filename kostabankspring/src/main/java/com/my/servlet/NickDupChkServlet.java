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

@WebServlet(name = "NickDupChkServlet",value = "/nickdupchk")
public class NickDupChkServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String nick = request.getParameter("user_nickname");
        UserService service = new UserService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            service.findByNick(nick);
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
