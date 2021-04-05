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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "IdDupChkServlet", value = "/iddupchk")
public class IdDupChkServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String id = request.getParameter("user_id");
        UserService service = new UserService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();
        try {
            User user = service.findById(id);
//            jacksonMap.put("user_id",user.getUser_id());
//            jacksonMap.put("user_nickname",user.getUser_nickname());
//            jacksonMap.put("user_pwd",user.getUser_pwd());
//            jacksonMap.put("user_email",user.getUser_email());
//            jacksonMap.put("user_date",user.getUser_date());
//            jacksonMap.put("user_adm",user.getUser_adm());
//            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print("{\"status\" : -1}");
        } catch (FindException e) {
            out.print("{\"status\" : 1}");
        }
    }
}
