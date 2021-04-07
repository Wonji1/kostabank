package com.my.service;

import com.my.dao.UserDAO;
import com.my.dao.UserDAOOracle;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.User;

import lombok.extern.log4j.Log4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class UserService {
	
	@Autowired
    private UserDAO dao;
    public User findById(String user_id) throws FindException{
        return dao.selectById(user_id);
    }
    public void findByNick(String user_nick) throws FindException{
        dao.selectByNick(user_nick);
    }
    public User findByEmail(String user_email) throws FindException{
        return dao.selectByEmail(user_email);
    }

    public String findTmpByEmail(String email) throws FindException{
        return dao.selectTmpByEmail(email);
    }
    public List<User> findNAllByN(int n) throws FindException{
        return dao.selectAll(n);
    }
    public void add(User user) throws AddException{
        dao.insert(user);
    }
    public String addEmail(String email) throws AddException{
        return dao.insertEmail(email);
    }
    public void modifyUser(User user) throws ModifyException{
        dao.update(user);
    }

    public User login(String id, String pwd) throws FindException{
        try {
            User user = dao.selectById(id);
            if(user.getUser_pwd().equals(pwd)) {
                return user;
            }else {
                throw new FindException("로그인  실패");
            }
        }catch(FindException e) {
            throw new FindException("로그인  실패");
        }
    }

    public String modifyPwdByEmail(String user_email) throws ModifyException{
        return dao.updateByEmail(user_email);
    }

    public void removeById(String user_id) throws RemoveException{
        dao.delete(user_id);
    }
    public void removeEmailById(String email) throws RemoveException{
        dao.deleteEmail(email);
    }


}
