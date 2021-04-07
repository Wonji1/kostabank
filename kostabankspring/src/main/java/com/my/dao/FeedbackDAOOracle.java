package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class FeedbackDAOOracle implements FeedbackDAO {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
    @Override
    public List<Feedback> selectFeedbackById(String user_id) throws Exception {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            List<Feedback> list =session.selectList("mybatis.feedbackMapper.selectFeedbackById",user_id);
            if (list.size() == 0) {
                throw new FindException("피드백이 없습니다.");
             }
            for(Feedback f: list) {
            	if(f.getFeedback_sort() == -1) {
            		f.setFeedback_sort(0);
            	}else {
            		f.setFeedback_sort(1);
            	}
            }
            
            return list;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public Qa UserQaById(String user_id, int rownum) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("user_id", user_id);
            map.put("rownum", rownum);
            
            Qa qa =session.selectOne("mybatis.feedbackMapper.UserQaById",map);
            if(qa == null){
                throw new FindException("게시판 아이디에 해당하는 값이 없습니다.");
            }
            return qa;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public Qa QaByQaId(String qa_id, int n, int s) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            Qa qa =session.selectOne("mybatis.feedbackMapper.QaByQaId",qa_id);
            if(qa == null){
                throw new FindException("문의사항 아이디에 해당하는 값이 없습니다.");
            }
            NewStatusQaUpdate(session,qa_id,n,s);
            return qa;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }


    @Override
    public List<Qa> QaAll(int page, int num) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rownum1", page*num);
            map.put("rownum2", (page-1)*num +1);
            
            List<Qa> list =session.selectList("mybatis.feedbackMapper.QaAll",map);
            if (list.size() == 0) {
                throw new FindException("문의사항이 없습니다.");
             }
            
            return list;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void QaInsert(Qa qa) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            session.insert("mybatis.feedbackMapper.BoardInsert",qa);
            
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }

    }

    @Override
    public void QaUpdate(Qa qa) throws ModifyException {

    }

    @Override
    public void QaDelete(String qa_id) throws RemoveException {

    }

    @Override
    public Report ReportById(String report_id, int n , int s) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            Report report =session.selectOne("mybatis.feedbackMapper.ReportById",report_id);
            if(report == null){
                throw new FindException("신고 아이디에 해당하는 값이 없습니다.");
            }
            NewStatusQaUpdate(session,report_id,n,s);
            return report;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public List<Report> ReportAll(int page, int num) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rownum1", page*num);
            map.put("rownum2", (page-1)*num +1);
            
            List<Report> list =session.selectList("mybatis.feedbackMapper.ReportAll",map);
            if (list.size() == 0) {
                throw new FindException("문의사항이 없습니다.");
             }
            
            return list;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void ReportInsert(Report report) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            session.insert("mybatis.feedbackMapper.ReportInsert",report);
            
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void ReportUpdate(Report report) throws ModifyException {

    }

    @Override
    public void ReportSolUpdate(String report_id, String content) throws ModifyException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String,Object> map =new HashMap<String, Object>();
            map.put("content", content);
            map.put("report_id", report_id);
            
            int rowcnt = session.update("mybatis.feedbackMapper.ReportSolUpdate",map);
            
            if(rowcnt == 0) {
            	throw new ModifyException("해당 신고가 존재하지 않습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new ModifyException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void QaSolUpdate(String qa_id, String content) throws ModifyException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String,Object> map =new HashMap<String, Object>();
            map.put("content", content);
            map.put("qa_id", qa_id);
            
            int rowcnt = session.update("mybatis.feedbackMapper.QaSolUpdate",map);
            
            if(rowcnt == 0) {
            	throw new ModifyException("해당 문의사항이 존재하지 않습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new ModifyException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void ReportDelete(String report_id) throws RemoveException {

    }

    @Override
    @Transactional(rollbackFor = RemoveException.class)
    public void QADeleteByList(String[] qa_id_list) throws RemoveException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            for(String qa_id: qa_id_list) {
            	session.delete("mybatis.feedbackMapper.QaDeleteByList",qa_id);
            }
            session.commit();
        } catch (Exception e) {
            throw  new RemoveException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public int qaNextId() throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            int next_val = session.selectOne("mybatis.feedbackMapper.qaNextId");

            return next_val;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    public void NewStatusReportUpdate(SqlSession session,String report_id, int n, int s){
    	Map<String,Object> map =new HashMap<String, Object>();
    	map.put("report_id", report_id);
    	map.put("report_new", n);
    	map.put("report_status", s);
    	
		session.update("mybatis.feedbackMapper.NewStatusReportUpdate", map);
    }

    public void NewStatusQaUpdate(SqlSession session,String qa_id, int n, int s){
    	Map<String,Object> map =new HashMap<String, Object>();
    	map.put("qa_id", qa_id);
    	map.put("qa_new", n);
    	map.put("qa_status", s);
    	
		session.update("mybatis.feedbackMapper.NewStatusQaUpdate", map);
    }
}
