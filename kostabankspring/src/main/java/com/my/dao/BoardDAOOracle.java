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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select B.rnum, B.* , (select count(*) from board) qa from\n" +
                "    (select rownum as rnum, A.* from (\n" +
                "        select * from board b join users u on b.user_id = u.user_id \n" +
                "        order by b.board_wdate desc ) A where rownum <=?) B where B.rnum >= ?";
        List<Board> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setInt(1,page*num);
            pstmt.setInt(2,(page-1)*num +1);
            rs = pstmt.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setUser_nickname(rs.getString("user_nickname"));
                Board board = new Board();
                board.setUser(user);
                board.setBoard_id(rs.getString("board_id"));
                board.setBoard_subtitle(rs.getString("board_subtitle"));
                board.setBoard_title(rs.getString("board_title"));
                board.setBoard_wdate(rs.getString("board_wdate"));
                board.setBoard_view(rs.getInt("board_view"));
                board.setBoard_file(rs.getString("board_file"));
                board.setBoard_total(rs.getInt("qa"));
                board.setUser(user);
                all.add(board);
            }
            if(all.size() == 0){
                throw new FindException("정보가 하나도 없습니다.");
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public List<BoardComment> CommentAll(String board_id) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }


        // board_id, comment_content, TO_CHAR(comment_wdate, 'YYYY/MM/DD HH:MI')AS comment_wdate,
//		TO_CHAR(comment_wdate, 'YYYY/MM/DD HH:MI')AS comment_wdate,
        String selectSQL = "SELECT bc.board_id, bc.user_id, comment_content, comment_wdate, (SELECT u.user_nickname \r\n" +
                "                                                                FROM users u\r\n" +
                "                                                                WHERE u.user_id=bc.user_id)AS user_nickname\r\n" +
                "FROM board_comment bc  \r\n" +
                "WHERE board_id=?\r\n" +
                "ORDER BY comment_wdate asc";

        List<BoardComment> list = new ArrayList();
        try {
            pstmt = con.prepareStatement(selectSQL);
            pstmt.setString(1, board_id);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                BoardComment board_comment = new BoardComment();
                String comment_content = rs.getString("comment_content");
                String comment_wdate = rs.getString("comment_wdate");
                String user_nickname = rs.getString("user_nickname");

                User user = new User();
                user.setUser_nickname(user_nickname);
                board_comment.setComment_content(comment_content);
                board_comment.setComment_wdate(comment_wdate);

                board_comment.setUser(user);

                list.add(board_comment);

            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public void CommentInsert(BoardComment comment) throws AddException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = MyConnection.getConnection();
            String insertSQL = "INSERT INTO board_comment(board_id, comment_content, comment_wdate, user_id)\r\n" +
                    "VALUES (?, ?, SYSDATE, ?)";

            pstmt = con.prepareStatement(insertSQL);

            pstmt.setString(1, comment.getBoard_id());
            pstmt.setString(2, comment.getComment_content());
            pstmt.setString(3, comment.getUser().getUser_id());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException();
        } finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public void UpCheck(String user_id, String board_id) throws FindException {

    }

    @Override
    public void BoardUpInsert(String board_id, String user_id) throws AddException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException(e.getMessage());
        }
        String insertSQL = "INSERT INTO board_up (board_id, user_id) VALUES(?,?)";
        try {
            pstmt = con.prepareStatement(insertSQL);
            pstmt.setString(1, board_id);
            pstmt.setString(2, user_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new AddException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public void BoradUpDelete(String board_id, String user_id) throws RemoveException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
            String deleteSQL = "DELETE board_up WHERE board_id =? AND user_id = ?";
            pstmt = con.prepareStatement(deleteSQL);
            pstmt.setString(1, board_id);
            pstmt.setString(2, user_id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoveException(e.getMessage());
        }finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public void BoardInsert(Board board) throws AddException {
        Connection con = null;
        PreparedStatement pstmt=null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException(e.getMessage());
        }
        try {
            String insertSQL = "INSERT INTO board(board_id,     board_subtitle, board_title, board_content, user_id, board_wdate, board_file)\r\n" +
                    "			VALUES (? ,?,             ?,           ?,             ?,       sysdate,     ?)";
            pstmt = con.prepareStatement(insertSQL);

            pstmt.setString(1, board.getBoard_id());
            pstmt.setString(2, board.getBoard_subtitle());
            pstmt.setString(3, board.getBoard_title());
            pstmt.setString(4, board.getBoard_content());
            pstmt.setString(5, board.getUser().getUser_id());
            pstmt.setString(6, board.getBoard_file());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new AddException();
        } finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public void BoardUpdate(Board board) throws ModifyException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String updateSQL = "UPDATE board SET board_subtitle = ?, board_title=?, "
                + "board_content = ?, board_file = ?, user_id = ?, board_wdate =?"
                + "WHERE board_id = ?";

        try {
            pstmt = con.prepareStatement(updateSQL);
            pstmt.setString(1, board.getBoard_subtitle());
            pstmt.setString(2, board.getBoard_title());
            pstmt.setString(3, board.getBoard_content());
            pstmt.setString(4, board.getBoard_file());
            pstmt.setString(5, board.getUser().getUser_id());
            pstmt.setString(6, board.getBoard_wdate());
            pstmt.setString(7, board.getBoard_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ModifyException();
        } finally {
            MyConnection.close(con,pstmt);
        }
    }

    @Override
    public void BoardDelete(String board_id) throws RemoveException {
        Connection con = null;
        PreparedStatement pstmt =null;

        try {
            con = MyConnection.getConnection();

            String deleteSQL = "delete board where board_id = ?";

            pstmt = con.prepareStatement(deleteSQL);
            pstmt.setString(1, board_id);
            int rowcnt = pstmt.executeUpdate();
            if(rowcnt == 0 ) {
                throw new RemoveException("삭제실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoveException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt);
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
        Connection con = null;
        PreparedStatement pstmt=null;
        ResultSet rs = null;
        try {
            con = MyConnection.getConnection();

            String selectNextIdSQL = "SELECT AUTO_INCRE_BOARD.NEXTVAL FROM dual";

            pstmt = con.prepareStatement(selectNextIdSQL);
            rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public Board BoardViewChk(String board_id) throws FindException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con=MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }
        String selectSQL = "SELECT board_view FROM board WHERE board_id =?";

        try {
            pstmt = con.prepareStatement(selectSQL);
            pstmt.setString(1, board_id);
            rs = pstmt.executeQuery();
            Board board = new Board();
            if(!rs.next()) {
                board.setBoard_view(rs.getInt("board_view"));
            }
            return board;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);

        }
    }

    @Override
    public void BoardViewUpdate(String board_id) throws ModifyException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con=MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ModifyException(e.getMessage());
        }

        String updateSQL = "UPDATE board SET board_view = board_view+1 WHERE board_id = ?";

        try {
            pstmt = con.prepareStatement(updateSQL);
            pstmt.setString(1, board_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ModifyException(e.getMessage());
        }finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public List<Board> BoardByTitle(String board_title) throws FindException {
        return null;
    }

    @Override
    public void DeleteBoardByList(String[] board_id_list) throws RemoveException {
        Connection con = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoveException("게시판 삭제 실패: 이유= " + e.getMessage());
        }
        PreparedStatement pstmt = null;
        String deleteSQL = "DELETE FROM board WHERE board_id = ?";
        try {
            for (int i = 0; i < board_id_list.length; i++) {
                pstmt = con.prepareStatement(deleteSQL);
                pstmt.setString(1, board_id_list[i]);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                throw new RemoveException("이미 존재하는 정보 입니다.");
            } else {
                e.printStackTrace();
            }
        } finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public void selectBoardUp(String user_id, String board_id) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select * from board_up where user_id = ? and board_id = ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1, user_id);
            pstmt.setString(2, board_id);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("아이디에 해당하는 값이 없습니다.");
            }

        } catch (SQLException e) {
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }
}
