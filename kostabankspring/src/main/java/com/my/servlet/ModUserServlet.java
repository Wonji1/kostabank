package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
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

@WebServlet(name = "ModUserServlet", value = "/moduser")
public class ModUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        UserService service = new UserService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        User user = new User();
        user.setUser_id(id);
        user.setUser_nickname(request.getParameter("user_nickname"));
        user.setUser_pwd(request.getParameter("user_pwd"));

        User userOri;
        try {
            userOri = service.findById(id);
            String originalNickname = userOri.getUser_nickname();
            String originalPwd = userOri.getUser_pwd();
            if(user.getUser_nickname().equals("")){
                user.setUser_nickname(originalNickname);
            }
            if(user.getUser_pwd().equals("")){
                user.setUser_pwd(originalPwd);
            }

            service.modifyUser(user);
            jacksonMap.put("status", 1);
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        } catch (FindException e) {
            jacksonMap.put("status", -1);
            jacksonMap.put("msg","존재하지 않는 아이디 입니다.");
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        } catch (ModifyException e) {
            jacksonMap.put("status", -2);
            jacksonMap.put("msg",e.getMessage());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        }
    }
}
