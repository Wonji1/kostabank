package com.my.service;

import com.my.dao.QuestionDAO;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.Question;

import lombok.extern.log4j.Log4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class QuestionService {
	
	@Autowired
    private QuestionDAO dao;
    public Question findById(String question_id) throws FindException{
        return dao.selectById(question_id);
    }

    public Question mynoteById(String user_id, int row) throws FindException{
        return dao.selectMNById(user_id, row);
    }
    public void removeMnById(String user_id, String question_id) throws RemoveException{
        dao.deleteMNById(user_id, question_id);
    }

    public List<Question> solvedById(String user_id,int n) throws FindException{
        return dao.selectSQById(user_id,n);
    }
    public void addMnById(String user_id, String[] question_id_list) throws AddException{
        dao.insertMNById(user_id, question_id_list);
    }

    public void insertQTmpByQYear(String user_id,String question_year) throws AddException{
        dao.insertQTmpByQYear(user_id, question_year);
    }

    public Question findQTmpByQId(String user_id, String row_num) throws FindException{
        return dao.selectQTmpByQId(user_id, row_num);
    }
    public void modifyQTmpByQId(String user_id,String[] question_answer_list) throws ModifyException{
        dao.updateQTmpByQ(user_id, question_answer_list);
    }
    public List<Question> findAllQTmpByRound(String user_id,String question_round) throws FindException{
        return dao.selectAfterSolveQByRound(user_id, question_round);
    }
    public void insertRandomQTmp(String user_id) throws AddException{
        dao.insertRandomQTmp(user_id);
    }
}
