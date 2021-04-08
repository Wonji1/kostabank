package com.my.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
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
import com.my.model.RenamePolicy;
import com.my.service.FeedbackService;
import com.my.vo.Qa;
import com.my.vo.Question;
import com.my.vo.Report;
import com.my.vo.User;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import lombok.extern.log4j.Log4j;

@ResponseBody
@Controller
@RequestMapping("/feedback/*")
@Log4j
public class FeedbackController {
	@Autowired
	private FeedbackService service;

    @RequestMapping("/addqa")
    public Map<String, Object> addqa(HttpServletRequest request
    		) throws AddException, IOException, FindException {
        Map<String, Object> jacksonMap = new HashMap<>();
		int nextBoardId = service.findNextQaId();
		File uploadFile =  new File(request.getServletContext().getRealPath("/")+ "/qaupload");
		if(!uploadFile.exists()){
			uploadFile.mkdir();
		}
		String saveDirectory = request.getServletContext().getRealPath("qaupload");
		int maxPostSize = 10*1024*1024;
		String encoding = "UTF-8";
		FileRenamePolicy policy = new RenamePolicy(String.valueOf(nextBoardId));
		MultipartRequest mr = new MultipartRequest(request, saveDirectory,maxPostSize, encoding, policy);

		Qa qa = new Qa();
		HttpSession session = request.getSession();
		User user = new User();
		String loginedId = (String)session.getAttribute("loginInfo");
		user.setUser_id(loginedId);
		qa.setQa_id(String.valueOf(nextBoardId));//글번호
		qa.setUser(user);//로그인된 아이디
		qa.setQa_title(mr.getParameter("qa_title"));//제목
		qa.setQa_content(mr.getParameter("qa_content"));//내용
		qa.setQa_file(mr.getOriginalFileName("qa_file"));//파일

		service.addQa(qa);

		jacksonMap.put("status", 1);
        return jacksonMap;

    }
    
    @RequestMapping("/addqasol")
    public Map<String, Object> addqasol(@RequestParam(value = "qa_id") String qa_id
    		, @RequestParam(value = "content") String content) throws ModifyException {
        
    	Map<String, Object> jacksonMap = new HashMap<>();
        service.addQaSol(qa_id,content);
        jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/addreport")
    public Map<String, Object> addreport(@RequestParam(value = "report_title") String report_title
    		, @RequestParam(value = "report_content") String report_content
    		, @RequestParam(value = "report_question") String report_question
    		, HttpServletRequest request) throws AddException {
        
    	Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
        
        Report report = new Report();
        User user = new User();
        Question question = new Question();
        user.setUser_id(id);
        question.setQuestion_id(report_question);
        report.setUser(user);
        report.setReport_title(report_title);
        report.setReport_content(report_content);
        report.setQuestion(question);
        service.addReport(report);
        jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/addreportsol")
    public Map<String, Object> addreportsol(@RequestParam(value = "report_id") String report_id
    		, @RequestParam(value = "content") String content) throws ModifyException {
        
    	Map<String, Object> jacksonMap = new HashMap<>();
        service.addReportSol(report_id,content);
        jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/reportlist")
    public List<Map<String, Object>> reportlist(@RequestParam(value = "page") int page
    		, @RequestParam(value = "num") int num) throws FindException {
        
    	List<Map<String, Object>> jacksonList= new ArrayList<>();
    	
        List<Report> reports = service.reportAll(page,num);
        for(Report report : reports) {
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("report_id",report.getReport_id());
            jacksonMap.put("user_nickname",report.getUser().getUser_nickname());
            jacksonMap.put("content",report.getQuestion().getContent());
            jacksonMap.put("correct_answer",report.getQuestion().getCorrect_answer());
            jacksonMap.put("explanation",report.getQuestion().getExplanation());
            jacksonMap.put("report_title",report.getReport_title());
            jacksonMap.put("report_content",report.getReport_content());
            jacksonMap.put("report_wdate",report.getReport_wdate());
            jacksonMap.put("report_new",report.getReport_new());
            jacksonMap.put("report_status",report.getReport_status());
            jacksonMap.put("report_sol_wdate",report.getReport_sol_wdate());
            jacksonMap.put("report_sol_content",report.getReport_sol_content());
            jacksonMap.put("report_total",report.getReport_total());
            jacksonList.add(jacksonMap);
        }
        return jacksonList;

    }
    
    @RequestMapping("/report")
    public Map<String, Object> report(@RequestParam(value = "n") int n
    		, @RequestParam(value = "s") int s
    		, @RequestParam(value = "report_id") String report_id) throws FindException {
        
    	Map<String, Object> jacksonMap = new HashMap<>();
        
        Report report = service.findReportById(report_id,n,s);
        jacksonMap.put("report_id",report.getReport_id());
        jacksonMap.put("user_nickname",report.getUser().getUser_nickname());
        jacksonMap.put("content",report.getQuestion().getContent());
        jacksonMap.put("correct_answer",report.getQuestion().getCorrect_answer());
        jacksonMap.put("explanation",report.getQuestion().getExplanation());
        jacksonMap.put("report_title",report.getReport_title());
        jacksonMap.put("report_content",report.getReport_content());
        jacksonMap.put("report_wdate",report.getReport_wdate());
        jacksonMap.put("report_new",report.getReport_new());
        jacksonMap.put("report_status",report.getReport_status());
        jacksonMap.put("report_sol_wdate",report.getReport_sol_wdate());
        jacksonMap.put("report_sol_content",report.getReport_sol_content());
        
        return jacksonMap;

    }
    
    @RequestMapping("/myqa")
    public Map<String, Object> myqa(@RequestParam(value = "rownum") int rownum
    		, HttpServletRequest request) throws FindException {
        
    	Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
        
        Qa q = service.qaById(id, rownum);
        jacksonMap.put("qa_id",q.getQa_id());
        jacksonMap.put("user_nickname",q.getUser().getUser_nickname());
        jacksonMap.put("qa_title", q.getQa_title());
        jacksonMap.put("qa_content", q.getQa_content());
        jacksonMap.put("qa_wdate", q.getQa_wdate());
        jacksonMap.put("qa_file", q.getQa_file());
        jacksonMap.put("qa_new", q.getQa_new());
        jacksonMap.put("qa_status", q.getQa_status());
        jacksonMap.put("qa_sol_wdate", q.getQa_sol_wdate());
        jacksonMap.put("qa_sol_content", q.getQa_sol_content());
        jacksonMap.put("qa_total", q.getQa_total());
        
        return jacksonMap;

    }
    
    @RequestMapping("/qalist")
    public List<Map<String, Object>> qalist(@RequestParam(value = "page") int page
    		, @RequestParam(value = "num") int num) throws FindException {
        
    	List<Map<String, Object>> jacksonList= new ArrayList<>();
    	
        List<Qa> qa = service.qaAll(page,num);
        for(Qa q : qa) {
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("qa_id",q.getQa_id());
            jacksonMap.put("user_nickname", q.getUser().getUser_nickname());
            jacksonMap.put("qa_title", q.getQa_title());
            jacksonMap.put("qa_content", q.getQa_content());
            jacksonMap.put("qa_wdate", q.getQa_wdate());
            jacksonMap.put("qa_file", q.getQa_file());
            jacksonMap.put("qa_new", q.getQa_new());
            jacksonMap.put("qa_status", q.getQa_status());
            jacksonMap.put("qa_sol_wdate", q.getQa_sol_wdate());
            jacksonMap.put("qa_sol_content", q.getQa_sol_content());
            jacksonMap.put("qa_total",q.getQa_total());
            jacksonList.add(jacksonMap);
        }
        return jacksonList;

    }

    @RequestMapping("/qa")
    public Map<String, Object> qa(@RequestParam(value = "n") int n
    		, @RequestParam(value = "s") int s
    		, @RequestParam(value = "qa_id") String qa_id) throws FindException {
        
    	Map<String, Object> jacksonMap = new HashMap<>();
        
        Qa q = service.findById(qa_id, n,s);
        jacksonMap.put("qa_id",q.getQa_id());
        jacksonMap.put("user_nickname", q.getUser().getUser_nickname());
        jacksonMap.put("qa_title", q.getQa_title());
        jacksonMap.put("qa_content", q.getQa_content());
        jacksonMap.put("qa_wdate", q.getQa_wdate());
        jacksonMap.put("qa_file", q.getQa_file());
        jacksonMap.put("qa_new", q.getQa_new());
        jacksonMap.put("qa_status", q.getQa_status());
        jacksonMap.put("qa_sol_wdate", q.getQa_sol_wdate());
        jacksonMap.put("qa_sol_content", q.getQa_sol_content());
        
        return jacksonMap;

    }
    
    @RequestMapping("/removeqabylist")
    public Map<String, Object> removeqabylist(@RequestParam(value = "qa_id_list") String[] qa_id_list) throws RemoveException {
		
		Map<String, Object> jacksonMap = new HashMap<>();

        service.removeQaByList(qa_id_list);
        jacksonMap.put("status", 1);
		return jacksonMap;

    }
}
