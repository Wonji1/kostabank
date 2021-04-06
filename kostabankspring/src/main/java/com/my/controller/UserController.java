package com.my.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.model.DeleteEmailThread;
import com.my.model.MyConfig;
import com.my.service.FeedbackService;
import com.my.service.UserService;
import com.my.vo.Board;
import com.my.vo.Feedback;
import com.my.vo.User;

@ResponseBody
@Controller
@RequestMapping("/user/*")
public class UserController {
	@Autowired
	private UserService service;
	
    @RequestMapping("/userinfo")
    public Map<String, Object> userinfo(HttpServletRequest request) throws FindException {
        Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
        
        User user = service.findById(id);
        jacksonMap.put("user_id",user.getUser_id());
        jacksonMap.put("user_nickname",user.getUser_nickname());
        jacksonMap.put("user_email",user.getUser_email());
        return jacksonMap;

    }
    
    @RequestMapping("/nickdupchk")
    public Map<String, Object> nickdupchk(@RequestParam(value = "user_nickname") String user_nickname){
        Map<String, Object> jacksonMap = new HashMap<>();
        
        try {
			service.findByNick(user_nickname);
	        jacksonMap.put("status", -1);
		} catch (FindException e) {
			jacksonMap.put("status", 1);
            jacksonMap.put("msg",e.getMessage());
		}
        return jacksonMap;

    }
    
    @RequestMapping("/emaildupchk")
    public Map<String, Object> emaildupchk(@RequestParam(value = "user_email") String user_email){
        Map<String, Object> jacksonMap = new HashMap<>();
        
        try {
			service.findByEmail(user_email);
	        jacksonMap.put("status", -1);
		} catch (FindException e) {
			jacksonMap.put("status", 1);
            jacksonMap.put("msg",e.getMessage());
		}
        return jacksonMap;

    }
    
    @RequestMapping("/chknumchk")
    public Map<String, Object> chknumchk(@RequestParam(value = "email") String email
    		,@RequestParam(value = "check_number") String check_number){
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            String correct_number = service.findTmpByEmail(email);
            if(correct_number.equals(check_number)) {
                jsonMap.put("status", 1);
                service.removeEmailById(email);
            }
            else{
                jsonMap.put("status", -1);
                jsonMap.put("msg","인증번호가 다릅니다.");
            }
        } catch (FindException e) {
            jsonMap.put("status", -2);
            jsonMap.put("msg",e.getMessage());
        } catch (RemoveException e) {
            jsonMap.put("status", -3);
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;

    }
    
    @RequestMapping("/recentreguser")
    public List<Map<String, Object>> recentreguser() throws FindException {
        List<Map<String, Object>> jacksonList = new ArrayList<>();
        List<User> userList = service.findNAllByN(11);
        for(User user: userList){
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("user_id", user.getUser_id());
            jacksonMap.put("user_date",user.getUser_date());
            jacksonList.add(jacksonMap);
        }
        return jacksonList;

    }
    
    @RequestMapping("/adduser")
    public Map<String, Object> adduser(@RequestParam(value = "user_id") String user_id
    		, @RequestParam(value = "user_nickname") String user_nickname
    		, @RequestParam(value = "user_pwd") String user_pwd
    		, @RequestParam(value = "user_email") String user_email) throws AddException {
        Map<String, Object> jacksonMap = new HashMap<>();

        User user = new User();
        user.setUser_id(user_id);
        user.setUser_nickname(user_nickname);
        user.setUser_pwd(user_pwd);
        user.setUser_email(user_email);
        service.add(user);
        jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/addemail")
    public Map<String, Object> addemail(@RequestParam(value = "email") String email) throws AddException {
        Map<String, Object> jacksonMap = new HashMap<>();

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
        DeleteEmailThread det = new DeleteEmailThread(email);
        det.start();
        
        return jacksonMap;

    }
    
    @RequestMapping("/moduser")
    public Map<String, Object> moduser(@RequestParam(value = "user_nickname") String user_nickname
    		,@RequestParam(value = "user_pwd") String user_pwd
    		, HttpServletRequest request){
        Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        User user = new User();
        user.setUser_id(id);
        user.setUser_nickname(user_nickname);
        user.setUser_pwd(user_pwd);

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
        } catch (FindException e) {
            jacksonMap.put("status", -1);
            jacksonMap.put("msg","존재하지 않는 아이디 입니다.");
        } catch (ModifyException e) {
            jacksonMap.put("status", -2);
            jacksonMap.put("msg",e.getMessage());
        }
        
        return jacksonMap;

    }
    
    @RequestMapping("/modpwdbyemail")
    public Map<String, Object> modpwdbyemail(@RequestParam(value = "user_email") String user_email){
        Map<String, Object> jacksonMap = new HashMap<>();

        try {
            String temp = service.modifyPwdByEmail(user_email);
            User u =service.findByEmail(user_email);
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
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user_email));

                // 메일 제목
                msg.setSubject("안녕하세요 KostaBank 아이디/비밀번호 메일입니다.");
                // 메일 내용
                msg.setText("아이디 : " + u.getUser_id() + "\n" + "비밀번호 : "+ temp);

                Transport.send(msg);
                System.out.println("이메일 전송");

            } catch (Exception e) {
                e.printStackTrace();// TODO: handle exception
            }
            jacksonMap.put("status", 1);
        } catch (ModifyException e) {
            e.printStackTrace();
            jacksonMap.put("status", -1);
            jacksonMap.put("msg", e.getMessage());
        } catch (FindException e) {
            e.printStackTrace();
            jacksonMap.put("status", -2);
            jacksonMap.put("msg", e.getMessage());
        }
        
        return jacksonMap;

    }

    @RequestMapping("/removeuser")
    public Map<String, Object> removeuser(HttpServletRequest request) throws RemoveException{
        Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        service.removeById(id);
        jacksonMap.put("status", 1);
        return jacksonMap;

    }
    
    @RequestMapping("/header")
    public Map<String, Object> header(HttpServletRequest request){
        List<Map<String,Object>> jacksonList = new ArrayList<>();
        HttpSession session = request.getSession();
        FeedbackService fService = new FeedbackService();
        String id = (String)session.getAttribute("loginInfo");
        try {
            User user = service.findById(id);
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
            
            return jacksonMap2;
        } catch (FindException e) {
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("status", -1);
            jacksonMap.put("msg", "로그인을 진행x");
            
            return jacksonMap;
        } catch (Exception e) {
            User user = null;
            try {
                user = service.findById(id);
                Map<String, Object> jacksonMap = new HashMap<>();
                jacksonMap.put("user_id",user.getUser_id());
                jacksonMap.put("user_nickname",user.getUser_nickname());
                jacksonMap.put("user_email",user.getUser_email());
                jacksonMap.put("user_adm",user.getUser_adm());
                jacksonMap.put("status", -2);
                jacksonMap.put("msg", e.getMessage()); // 정보가 없을 때
                
                return jacksonMap;
            } catch (FindException ex) {
                Map<String, Object> jacksonMap = new HashMap<>();
                jacksonMap.put("status", -1);
                jacksonMap.put("msg", "로그인을 진행 .x");
                return jacksonMap;
            }

        }

    }
    
    @RequestMapping("/iddupchk")
    public Map<String, Object> iddupchk(@RequestParam(value = "user_id") String user_id){
        Map<String, Object> jacksonMap = new HashMap<>();
        
        try {
			service.findById(user_id);
	        jacksonMap.put("status", -1);
		} catch (FindException e) {
	        jacksonMap.put("status", 1);
		}
        return jacksonMap;

    }
    
    @RequestMapping("/login")
    public Map<String, Object> login(@RequestParam(value = "user_id") String user_id
    		, @RequestParam(value = "user_pwd") String user_pwd
    		, HttpServletRequest request) throws FindException{
        Map<String, Object> jacksonMap = new HashMap<>();
        
        User user = service.login(user_id,user_pwd);
        HttpSession session = request.getSession();
        session.setAttribute("loginInfo", user.getUser_id());

        jacksonMap.put("status", 1);
        jacksonMap.put("user_adm", user.getUser_adm());
        
        return jacksonMap;

    }
    
    @RequestMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request){
    	HttpSession session = request.getSession();
        session.removeAttribute("loginInfo");
        Map<String, Object> jacksonMap = new HashMap<>();

        jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    
}
