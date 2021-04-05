package com.my.service;

import com.my.dao.BoardDAO;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.Board;
import com.my.vo.BoardComment;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class BoardService {

    @Autowired
    private BoardDAO dao;
    public Board boardById(String board_id) throws FindException {
        return dao.BoardById(board_id);
    }
    public List<Board> boardAll(int page, int num) throws FindException {
        return dao.BoardAll(page,num);
    }
    public void removeBoardByList(String[] board_id_list) throws RemoveException{
        dao.DeleteBoardByList(board_id_list);
    }

    public List<BoardComment> commentAll(String board_id) throws FindException {
        return dao.CommentAll(board_id);
    }
    public void addBoardComment (BoardComment comment) throws AddException {
        dao.CommentInsert(comment);
    }
    public void addBoard(Board board) throws AddException {
        dao.BoardInsert(board);
    }
    public int findNextBoardId() throws FindException{
        return dao.NextId();
    }
    public void modifyBoard(Board board) throws ModifyException {
        dao.BoardUpdate(board);
    }
    public void removeBoardById(String board_id) throws RemoveException{
        dao.BoardDelete(board_id);
    }
    public Board boardViewByBoardId (String board_id) throws FindException {
        return dao.BoardViewChk(board_id);
    }
    public void modifyBoardView (String board_id) throws ModifyException {
        dao.BoardViewUpdate(board_id);
    }
    public void addBoardUp(String board_id, String user_id) throws AddException {
        dao.BoardUpInsert(board_id, user_id);
    }
    public void removeBoardUp(String board_id, String user_id) throws RemoveException{
        dao.BoradUpDelete(board_id, user_id);
    }
    public List<Board> findByBoardTitle(String board_title) throws FindException {
        return dao.BoardByTitle(board_title);
    }
    public void findBoardUp(String user_id, String board_id) throws FindException{
        dao.selectBoardUp(user_id, board_id);
    }
}
