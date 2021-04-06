package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.sql.MyConnection;
import com.my.vo.Board;
import com.my.vo.User;

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

@Repository
public class UserDAOOracle implements UserDAO {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Override
    public User selectById(String user_id) throws FindException {
		SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            User user =session.selectOne("mybatis.userMapper.selectById",user_id);
            if(user == null){
                throw new FindException("유저 아이디에 해당하는 값이 없습니다.");
            }
            return user;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public User selectByNick(String user_nickname) throws FindException {
		SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            User user =session.selectOne("mybatis.userMapper.selectByNick",user_nickname);
            if(user == null){
                throw new FindException("유저 닉네임에 해당하는 값이 없습니다.");
            }
            return user;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public User selectByEmail(String user_email) throws FindException {
		SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            User user =session.selectOne("mybatis.userMapper.selectByEmail",user_email);
            if(user == null){
                throw new FindException("유저 이메일에 해당하는 값이 없습니다.");
            }
            return user;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public String selectTmpByEmail(String email) throws FindException {
		SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            String num =session.selectOne("mybatis.userMapper.selectTmpByEmail",email);
            if(num == null){
                throw new FindException("5분이 지났습니다. 다시 인증해주세요.");
            }
            return num;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public List<User> selectAll(int n) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            List<User> list =session.selectList("mybatis.userMapper.selectAll",n);
            if (list.size() == 0) {
                throw new FindException("회원이 한명도 없습니다..");
             }
            return list;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void insert(User user) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            session.insert("mybatis.userMapper.insertUser",user);
            
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public String insertEmail(String email) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Random rand = new Random();
            String numStr = ""; //난수가 저장될 변수

            for(int i=0;i<8;i++) {
                //0~9 까지 난수 생성
                String ran = Integer.toString(rand.nextInt(10));
                numStr += ran;
            }
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("email", email);
            map.put("check_num", numStr);
            
            session.insert("mybatis.userMapper.insertEmail",map);
            return numStr;
            
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }

    }

    @Override
    public void update(User user) throws ModifyException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            int rowcnt = session.update("mybatis.userMapper.updateUser",user);
            
            if(rowcnt == 0) {
            	throw new ModifyException("해당 정보가 존재하지 않습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new ModifyException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public String updateByEmail(String user_email) throws ModifyException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Random rand = new Random();
            String numStr = ""; //난수가 저장될 변수

            for(int i=0;i<10;i++) {
                //0~9 까지 난수 생성
                String ran = Integer.toString(rand.nextInt(10));
                numStr += ran;
            }
            
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("user_pwd", numStr);
            map.put("user_email", user_email);
            
            int rowcnt = session.update("mybatis.userMapper.updateEmail",map);
            
            if(rowcnt == 0) {
            	throw new ModifyException("해당 정보가 존재하지 않습니다.");
            }
            session.commit();
            return numStr;
        } catch (Exception e) {
            throw  new ModifyException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void delete(String user_id) throws RemoveException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            int rowcnt = session.delete("mybatis.userMapper.deleteUser",user_id);
            
            if(rowcnt == 0) {
            	throw new RemoveException("삭제 실패: 해당 고객이 없습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new RemoveException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void deleteEmail(String email) throws RemoveException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            int rowcnt = session.delete("mybatis.userMapper.deleteEmail",email);
            
            if(rowcnt == 0) {
            	throw new RemoveException("삭제 실패: 해당 이메일이 없습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new RemoveException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }
}
