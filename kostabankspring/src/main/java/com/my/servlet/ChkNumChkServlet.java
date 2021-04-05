package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.exception.RemoveException;
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

@WebServlet(name = "ChkNumChkServlet", value = "/chknumchk")
public class ChkNumChkServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String email = request.getParameter("email");
        String check_number = request.getParameter("check_number");
        UserService service = new UserService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            String correct_number = service.findTmpByEmail(email);
            if(correct_number.equals(check_number)) {
                jsonMap.put("status", 1);
                String jsonStr = mapper.writeValueAsString(jsonMap);
                out.print(jsonStr);
                service.removeEmailById(email);
            }
            else{
                jsonMap.put("status", -1);
                jsonMap.put("msg","인증번호가 다릅니다.");
                String jsonStr = mapper.writeValueAsString(jsonMap);
                out.print(jsonStr);
            }
        } catch (FindException e) {
            jsonMap.put("status", -2);
            jsonMap.put("msg",e.getMessage());
            String jsonStr = mapper.writeValueAsString(jsonMap);
            out.print(jsonStr);
        } catch (RemoveException e) {
            jsonMap.put("status", -3);
            jsonMap.put("msg",e.getMessage());
            String jsonStr = mapper.writeValueAsString(jsonMap);
            out.print(jsonStr);
        }
    }
}
