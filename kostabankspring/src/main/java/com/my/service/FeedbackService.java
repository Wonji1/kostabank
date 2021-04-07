package com.my.service;

import com.my.dao.FeedbackDAO;
import com.my.dao.FeedbackDAOOracle;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.Feedback;
import com.my.vo.Qa;
import com.my.vo.Report;

import lombok.extern.log4j.Log4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class FeedbackService {
	
	@Autowired
    private FeedbackDAO dao;
    public Qa qaById(String user_id, int rownum) throws FindException{
        return dao.UserQaById(user_id,rownum);
    }
    public Qa findById(String qa_id, int n, int s) throws FindException{
        return dao.QaByQaId(qa_id, n, s);
    }
    public List<Qa> qaAll(int page, int num) throws FindException{
        return dao.QaAll(page,num);
    }
    public void addQa (Qa qa) throws AddException {
        dao.QaInsert(qa);
    }
    public int findNextQaId() throws FindException {
        return dao.qaNextId();
    }

    public Report findReportById(String report_id,int n, int s) throws FindException{
        return dao.ReportById(report_id,  n,  s);
    }

    public List<Report> reportAll(int page, int num) throws  FindException{
        return dao.ReportAll(page,num);
    }
    public void addReport(Report report) throws AddException{
        dao.ReportInsert(report);
    }

    public List<Feedback> feedbacksById(String user_id) throws Exception{
        return dao.selectFeedbackById(user_id);
    }

    public void  addReportSol(String report_id, String content) throws ModifyException{
        dao.ReportSolUpdate(report_id,content);
    }

    public void addQaSol(String qa_id, String content) throws ModifyException{
        dao.QaSolUpdate(qa_id, content);
    }

    public void removeQaByList(String[] qa_id_list) throws RemoveException{
        dao.QADeleteByList(qa_id_list);
    }
}
