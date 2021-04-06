package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.sql.MyConnection;
import com.my.vo.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDAOOracle implements BoardDAO{
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Board BoardById(String board_id) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Board board =session.selectOne("mybatis.boardMapper.BoardById",board_id);
            if(board == null){
                throw new FindException("게시판 아이디에 해당하는 값이 없습니다.");
            }
            return board;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }

    }

    @Override
    public List<Board> BoardAll(int page, int num) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("page", page*num);
            map.put("num", (page-1)*num +1);
            List<Board> list =session.selectList("mybatis.boardMapper.BoardAll",map);
            if (list.size() == 0) {
                throw new FindException("게시글이 없습니다.");
             }
            return list;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }

    }

    @Override
    public List<BoardComment> CommentAll(String board_id) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            List<BoardComment> list =session.selectList("mybatis.boardMapper.CommentAll",board_id);
            if (list.size() == 0) {
                throw new FindException("댓글이 존재하지 않습니다.");
             }
            return list;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void CommentInsert(BoardComment comment) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("board_id",comment.getBoard_id());
            map.put("comment_content",comment.getComment_content());
            map.put("user_id",comment.getUser().getUser_id());
            
            session.insert("mybatis.boardMapper.CommentInsert",map);
            
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void UpCheck(String user_id, String board_id) throws FindException {

    }

    @Override
    public void BoardUpInsert(String board_id, String user_id) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("board_id",board_id);
            map.put("user_id",user_id);
            
            session.insert("mybatis.boardMapper.BoardUpInsert",map);
            
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void BoradUpDelete(String board_id, String user_id) throws RemoveException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("board_id",board_id);
            map.put("user_id",user_id);
            
            int rowcnt = session.delete("mybatis.boardMapper.BoardUpDelete",map);
            
            if(rowcnt == 0) {
            	throw new RemoveException("삭제할 좋아요가 없습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new RemoveException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void BoardInsert(Board board) throws AddException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            session.insert("mybatis.boardMapper.BoardInsert",board);
            
        } catch (Exception e) {
            throw  new AddException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }

    }

    @Override
    public void BoardUpdate(Board board) throws ModifyException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            int rowcnt = session.update("mybatis.boardMapper.BoardUpdate",board);
            
            if(rowcnt == 0) {
            	throw new ModifyException("해당 게시글이 존재하지 않습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new ModifyException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void BoardDelete(String board_id) throws RemoveException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            int rowcnt = session.delete("mybatis.boardMapper.BoardDelete",board_id);
            
            if(rowcnt == 0) {
            	throw new RemoveException("해당 게시글이 존재하지 않습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new RemoveException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public Notice NoticeById(String notice_id) throws FindException {
        return null;
    }

    @Override
    public List<Notice> NoticeAll(int page, int n) throws FindException {
        return null;
    }

    @Override
    public void NoticeInsert(Notice notice) throws AddException {

    }

    @Override
    public void NoticeUpdate(Notice notice) throws ModifyException {

    }

    @Override
    public void NoticeDelete(String notice_id) throws RemoveException {

    }

    @Override
    public int NextId() throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            int next_val = session.selectOne("mybatis.boardMapper.NextId");

            return next_val;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public Board BoardViewChk(String board_id) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Board board = session.selectOne("mybatis.boardMapper.BoardViewChk",board_id);

            return board;
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void BoardViewUpdate(String board_id) throws ModifyException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            int rowcnt = session.update("mybatis.boardMapper.BoardViewUpdate",board_id);
            
            if(rowcnt == 0) {
            	throw new ModifyException("해당 게시글이 존재하지 않습니다.");
            }
            session.commit();
        } catch (Exception e) {
            throw  new ModifyException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }

    @Override
    public List<Board> BoardByTitle(String board_title) throws FindException {
        return null;
    }

    @Override
    @Transactional(rollbackFor = RemoveException.class)
    public void DeleteBoardByList(String[] board_id_list) throws RemoveException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            
            for(String board_id: board_id_list) {
            	session.delete("mybatis.boardMapper.DeleteBoardByList",board_id);
            }
            session.commit();
        } catch (Exception e) {
            throw  new RemoveException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }
    

    @Override
    public void selectBoardUp(String user_id, String board_id) throws FindException {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("user_id",user_id);
            map.put("board_id",board_id);
            Board board =session.selectOne("mybatis.boardMapper.SelectBoardUp",map);
            
        } catch (Exception e) {
            throw  new FindException(e.getMessage());
        }finally {
            if(session != null) session.close();
        }
    }
}
