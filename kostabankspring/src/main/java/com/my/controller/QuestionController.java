package com.my.controller;

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
import com.my.service.QuestionService;
import com.my.vo.Board;
import com.my.vo.Question;

import lombok.extern.log4j.Log4j;

@ResponseBody
@Controller
@RequestMapping("/question/")
@Log4j
public class QuestionController {
	@Autowired
	private QuestionService service;
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private HttpServletRequest request;
	
    @RequestMapping("/addmn")
    public Map<String, Object> addmn(@RequestParam(value = "question_id_list") String[] question_id_list) throws AddException {
		
		Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        service.addMnById(id,question_id_list);
        jacksonMap.put("status", 1);
		return jacksonMap;

    }
    
    @RequestMapping("/addqtmp")
    public Map<String, Object> addqtmp(@RequestParam(value = "question_year") String question_year
    		) throws AddException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        service.insertQTmpByQYear(id,question_year);
        jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/addrandomqtmp")
    public Map<String, Object> addrandomqtmp() throws AddException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        service.insertRandomQTmp(id);
        jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/mynote")
    public Map<String, Object> mynote(@RequestParam(value = "rownum") int rownum
    		) throws FindException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        Question question = service.mynoteById(id,rownum);
        jacksonMap.put("question_id",question.getQuestion_id());
        jacksonMap.put("content",question.getContent());
        jacksonMap.put("correct_answer",question.getCorrect_answer());
        jacksonMap.put("explanation",question.getExplanation());
        jacksonMap.put("correct_percent",((int)(((double)question.getCorrect_answer_count()/(double)question.getTotal_answer_count())*100)));
        jacksonMap.put("mn_total",question.getMn_total());
        
        return jacksonMap;

    }
    
    @RequestMapping("/qtmp")
    public Map<String, Object> qtmp(@RequestParam(value = "row_num") String row_num
    		) throws FindException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        Question q = service.findQTmpByQId(id,row_num);
        jacksonMap.put("question_id",q.getQuestion_id());
        jacksonMap.put("content",q.getContent());
        jacksonMap.put("explanation",q.getExplanation());
        jacksonMap.put("correct_answer",q.getCorrect_answer());
        jacksonMap.put("correct_percent",((int)(((double)q.getCorrect_answer_count()/(double)q.getTotal_answer_count())*100)));
        jacksonMap.put("total",q.getMn_total());
        
        return jacksonMap;

    }
    
    @RequestMapping("/questioninfo")
    public Map<String, Object> questioninfo(@RequestParam(value = "question_id") String question_id
    		) throws FindException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
    	
        Question question = service.findById(question_id);
        jacksonMap.put("question_id",question.getQuestion_id());
        jacksonMap.put("content",question.getContent());
        jacksonMap.put("correct_answer",question.getCorrect_answer());
        jacksonMap.put("explanation",question.getExplanation());
        jacksonMap.put("correct_percent",((int)(((double)question.getCorrect_answer_count()/(double)question.getTotal_answer_count())*100)));
        
        return jacksonMap;

    }
    
    @RequestMapping("/removemn")
    public Map<String, Object> removemn(@RequestParam(value = "question_id") String question_id
    		) throws RemoveException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
    	
        service.removeMnById(id,question_id);
        jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/scoringq")
    public Map<String, Object> scoringq(@RequestParam(value = "answer_list") String[] answer_list
    		) throws ModifyException {
		
    	Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
    	
        service.modifyQTmpByQId(id,answer_list);
        jacksonMap.put("status", 1);
        
        return jacksonMap;

    }
    
    @RequestMapping("/solved")
    public List<Map<String, Object>> solved() throws FindException {
		
    	List<Map<String, Object>> jacksonList = new ArrayList<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
    	
        List<Question> questionList = service.solvedById(id, 200);
        for(Question question : questionList){
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("question_id",question.getQuestion_id());
            jacksonMap.put("content",question.getContent());
            jacksonMap.put("correct_answer",question.getCorrect_answer());
            jacksonMap.put("explanation",question.getExplanation());
            jacksonMap.put("correct_percent",((int)(((double)question.getCorrect_answer_count()/(double)question.getTotal_answer_count())*100)));
            jacksonMap.put("question_ox",question.getQuestion_ox());
            jacksonMap.put("mn_V",question.getMn_id());
            jacksonList.add(jacksonMap);
        }
        
        return jacksonList;

    }
    
    @RequestMapping("/solveresult")
    public List<Map<String, Object>> solveresult(@RequestParam(value = "question_round") String question_round
    		) throws FindException {
		
    	List<Map<String, Object>> jacksonList = new ArrayList<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
    	
        List<Question> questionList = service.findAllQTmpByRound(id,question_round);
        for(Question question : questionList){
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("question_id",question.getQuestion_id());
            jacksonMap.put("question_ox",question.getQuestion_ox());
            jacksonList.add(jacksonMap);
        }
        
        return jacksonList;

    }
    
    
}
