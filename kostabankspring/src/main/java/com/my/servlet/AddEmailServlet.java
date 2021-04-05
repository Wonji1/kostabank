package com.my.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.AddException;
import com.my.model.DeleteEmailThread;
import com.my.model.MyConfig;
import com.my.service.UserService;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet(name = "AddEmailServlet",value = "/addemail")
public class AddEmailServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        UserService service = new UserService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();

        String email = request.getParameter("email");
        try {
            String temp = service.addEmail(email);
            MyConfig myconfig = new MyConfig();
            String host = "smtp.naver.com";
            String user = myconfig.getEmail(); // 자신의 네이버 계정
            String password = myconfig.getPassword();// 자신의 네이버 패스워드

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });

            // email 전송
            try {
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(user));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                // 메일 제목
                msg.setSubject("안녕하세요 KostaBank 인증 메일입니다.");
                // 메일 내용
                msg.setText("인증 번호는 :" + temp);

                Transport.send(msg);
                System.out.println("이메일 전송");

            } catch (Exception e) {
                e.printStackTrace();// TODO: handle exception
            }
            jacksonMap.put("status", 1);
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
            out.flush();
            DeleteEmailThread det = new DeleteEmailThread(email);
            det.start();
        } catch (AddException e) {
            jacksonMap.put("status", -1);
            jacksonMap.put("msg", e.getMessage());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        }
    }
}
