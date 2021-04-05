package com.my.servlet;
//package com.my.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.my.exception.AddException;
//import com.my.exception.FindException;
//import com.my.model.RenamePolicy;
//import com.my.service.FeedbackService;
//import com.my.vo.Qa;
//import com.my.vo.User;
//import com.oreilly.servlet.MultipartRequest;
//import com.oreilly.servlet.multipart.FileRenamePolicy;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//
//@WebServlet("/addqa")
//public class AddQaServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.setContentType("application/json; charset=utf-8");
//		PrintWriter out = response.getWriter();
//		Map<String, Object> jacksonMap = new HashMap<>();
//		ObjectMapper mapper = new ObjectMapper();
//
//		FeedbackService service = new FeedbackService();
//		try {
//			int nextBoardId = service.findNextQaId();
//			File uploadFile =  new File(this.getServletContext().getRealPath("/")+ "/qaupload");
//			if(!uploadFile.exists()){
//				uploadFile.mkdir();
//			}
//			String saveDirectory = getServletContext().getRealPath("qaupload");
//			int maxPostSize = 10*1024*1024;
//			String encoding = "UTF-8";
//			FileRenamePolicy policy = new RenamePolicy(String.valueOf(nextBoardId));
//			MultipartRequest mr = new MultipartRequest(request, saveDirectory,maxPostSize, encoding, policy);
//
//			Qa qa = new Qa();
//			HttpSession session = request.getSession();
//			User user = new User();
//			String loginedId = (String)session.getAttribute("loginInfo");
//			user.setUser_id(loginedId);
//			qa.setQa_id(String.valueOf(nextBoardId));//글번호
//			qa.setUser(user);//로그인된 아이디
//			qa.setQa_title(mr.getParameter("qa_title"));//제목
//			qa.setQa_content(mr.getParameter("qa_content"));//내용
//			qa.setQa_file(mr.getOriginalFileName("qa_file"));//파일
//
//			service.addQa(qa);
//
//			jacksonMap.put("status", 1);
//			String jsonStr = mapper.writeValueAsString(jacksonMap) ;
//			out.print(jsonStr);
//
//		} catch (AddException | FindException e) {
//			e.printStackTrace();
//			jacksonMap.put("status", -1);
//			jacksonMap.put("msg", e.getMessage());
//			String jsonStr = mapper.writeValueAsString(jacksonMap);
//			out.print(jsonStr);
//		}
//	}
//}
