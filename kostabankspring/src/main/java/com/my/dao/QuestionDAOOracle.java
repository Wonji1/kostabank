package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class QuestionDAOOracle implements QuestionDAO {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
    @Override
    public Question selectById(String question_id) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Question question =session.selectOne("mybatis.questionMapper.selectById",question_id);
            if(question == null){
                throw new FindException("문제 아이디에 해당하는 값이 없습니다.");
            }
            return question;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public Question selectMNById(String user_id, int rownum) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", user_id);
            map.put("rownum", rownum);
            
            Question question =session.selectOne("mybatis.questionMapper.selectMNById",map);
            if(question == null){
                throw new FindException("고객 아이디에 해당하는 값이 없습니다.");
            }
            return question;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void insertMNById(String user_id, String[] question_id_list) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            for(String question_id: question_id_list) {
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("user_id", user_id);
            	map.put("question_id", question_id);
            	session.insert("mybatis.questionMapper.insertMNById",map);
            }
            session.commit();
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void deleteMNById(String user_id, String question_id) throws RemoveException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("user_id",user_id);
            map.put("question_id",question_id);
            
            int rowcnt = session.delete("mybatis.questionMapper.deleteMNById",map);
            
            if(rowcnt == 0) {
            	throw new RemoveException("해당 문제가 존재하지 않습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new RemoveException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    
    @Override
    public List<Question> selectSQById(String user_id, int n) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user_id", user_id);
            map.put("n", n);
            List<Question> list =session.selectList("mybatis.questionMapper.selectSQById",map);
            if (list.size() == 0) {
                throw new FindException("문제가 없습니다.");
             }
            for(Question question : list) {
                if (question.getMn_id() == null) {
                    question.setMn_id("");
                } else {
                    question.setMn_id("V");
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
    public List<Question> selectAll() throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            List<Question> list =session.selectList("mybatis.questionMapper.selectSQById");
            if (list.size() == 0) {
                throw new FindException("문제가 없습니다.");
             }

            return list;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    @Transactional(rollbackFor = {RemoveException.class, AddException.class})
    public void insertQTmpByQYear(String user_id, String question_year) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.delete("mybatis.questionMapper.insertQTmpDelete",user_id);
            List<Question> list =session.selectList("mybatis.questionMapper.insertQTmpSelect",question_year);
            if (list.size() == 0) {
                throw new FindException("문제가 없습니다.");
             }
            for(Question question:list) {
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("question_id", question.getQuestion_id());
            	map.put("user_id", user_id);
            	map.put("question_answer", question.getCorrect_answer());
            	
            	session.insert("mybatis.questionMapper.insertQTmpInsert",map);
            }
            session.commit();
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    
    

    @Override
    @Transactional(rollbackFor = {RemoveException.class, AddException.class})
    public void insertRandomQTmp(String user_id) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.delete("mybatis.questionMapper.insertRandomQTmpDelete",user_id);
            
            Random rand = new Random();

            int[] randomArr = new int[20];
            for(int i=0;i<20;i++) {
                //0~9 까지 난수 생성
                int ran = rand.nextInt(100);
                randomArr[i] = ran;
                for(int j=0;j<i;j++){
                    if(randomArr[j] == ran){
                        i--;
                        break;
                    }
                }
            }
            
            List<Question> tmpAll = new ArrayList<>();
            
            for(int i=1; i<=5;i++) {
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("user_id", user_id);
            	map.put("n", i);
            	List<Question> list =session.selectList("mybatis.questionMapper.insertRandomQTmpSelect",map);
                if (list.size() == 0) {
                    throw new FindException("문제가 없습니다.");
                }
                for(int j=0;j<20;j++){
                	tmpAll.add(list.get(randomArr[j]));
                }
            }
            for(Question question: tmpAll) {
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("question_id", question.getQuestion_id());
            	map.put("user_id", user_id);
            	map.put("question_answer", question.getCorrect_answer());
            	
            	session.insert("mybatis.questionMapper.insertRandomQTmpInsert",map);
            }
            session.commit();
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public Question selectQTmpByQId(String user_id, String row_num) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            List<Question> all = new ArrayList<Question>();
            for(int i=1;i<=5;i++) {
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("user_id", user_id);
            	map.put("n", i);
            	
	            List<Question> list =session.selectList("mybatis.questionMapper.selectSQById");
	            if (list.size() == 0) {
	                throw new FindException("문제가 없습니다.");
	            }
	            for(Question question : list) {
	            	all.add(question);
	            }

            }

            return all.get(Integer.parseInt(row_num)-1);
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    @Transactional(rollbackFor = {ModifyException.class, AddException.class})
    public void updateQTmpByQ(String user_id, String[] question_answer_list) throws ModifyException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            List<Question> ox = new ArrayList<>();
            for (int i =1; i<=5;i++) {
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("user_id", user_id);
            	map.put("n", i);
                List<Question> qall =session.selectList("mybatis.questionMapper.updateQTmpByQSelect",map);
                
                if (qall.size() == 0) {
                    throw new ModifyException("문제가 하나도 없습니다.");
                }
                for (int j = 0; j < qall.size(); j++) {
                    Question question = qall.get(j);
                    if (question_answer_list[j+20*(i-1)].equals(question.getCorrect_answer())) {
                        question.setQuestion_ox(1);
                    } else {
                        question.setQuestion_ox(0);
                    }
                    ox.add(question);
                }
            }
            
            for (Question q : ox) {
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("question_ox", q.getQuestion_ox());
            	map.put("question_id", q.getQuestion_id());
            	map.put("user_id", user_id);

                int rowcnt = session.update("mybatis.questionMapper.updateQTmpByQUpdate1",map);
                
                if(rowcnt == 0) {
                	throw new ModifyException("해당 문제가 존재하지 않습니다.");
                }
            }

            for (Question q : ox) {
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("total_answer_count", q.getTotal_answer_count()+1);
                if(q.getQuestion_ox() == 1){
                	map.put("correct_answer_count", q.getCorrect_answer_count()+1);
                }else {
                	map.put("correct_answer_count", q.getCorrect_answer_count());
                }
            	map.put("question_id", q.getQuestion_id());
            	 int rowcnt = session.update("mybatis.questionMapper.updateQTmpByQUpdate2",map);
                 
                 if(rowcnt == 0) {
                 	throw new ModifyException("해당 문제가 존재하지 않습니다.");
                 }
            }

            for (Question q : ox) {
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("user_id", user_id);
            	map.put("question_id", q.getQuestion_id());
            	map.put("question_ox", q.getQuestion_ox());
            	
            	session.insert("mybatis.questionMapper.updateQTmpByQInsert",map);
            }
        } catch (Exception e) {
            throw  new ModifyException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }

    }

    @Override
    public List<Question> selectAfterSolveQByRound(String user_id, String question_round) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user_id", user_id);
            map.put("question_round", question_round);
            List<Question> list =session.selectList("mybatis.questionMapper.selectAfterSolveQByRound",map);
            if (list.size() == 0) {
                throw new FindException("문제가 없습니다.");
             }

            return list;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }
}