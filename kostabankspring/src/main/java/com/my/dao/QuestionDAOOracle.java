package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.sql.MyConnection;
import com.my.vo.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionDAOOracle implements QuestionDAO {
    @Override
    public Question selectById(String question_id) throws FindException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "SELECT * FROM question WHERE question_id = ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1, question_id);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new FindException("문제 아이디에 해당하는 값이 없습니다.");
            }
            Question question = new Question();
            question.setQuestion_id(rs.getString("question_id"));
            question.setContent(rs.getString("content"));
            question.setCorrect_answer(rs.getString("correct_answer"));
            question.setExplanation(rs.getString("explanation"));
            question.setTotal_answer_count(Integer.parseInt(rs.getString("total_answer_count")));
            question.setCorrect_answer_count(Integer.parseInt(rs.getString("correct_answer_count")));
            return question;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public Question selectMNById(String user_id, int rownum) throws FindException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select B.rnum, B.* , (select count(*) from my_note where user_id= ?) mn from\n" +
                "    (select rownum as rnum, A.* from (\n" +
                "        select q.* from question q join my_note mn on q.QUESTION_ID = mn.QUESTION_ID where mn.user_id = ? \n" +
                "        order by mn.mn_date desc ) A where rownum <=?) B where B.rnum >= ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1, user_id);
            pstmt.setString(2, user_id);
            pstmt.setInt(3, rownum);
            pstmt.setInt(4, rownum);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new FindException("아이디에 해당하는 값이 없습니다.");
            }
            Question question = new Question();
            question.setQuestion_id(rs.getString("question_id"));
            question.setContent(rs.getString("content"));
            question.setCorrect_answer(rs.getString("correct_answer"));
            question.setExplanation(rs.getString("explanation"));
            question.setTotal_answer_count(Integer.parseInt(rs.getString("total_answer_count")));
            question.setCorrect_answer_count(Integer.parseInt(rs.getString("correct_answer_count")));
            question.setMn_total(Integer.parseInt(rs.getString("mn")));
            return question;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public void insertMNById(String user_id, String[] question_id_list) throws AddException {
        Connection con = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException("노트 추가 실패: 이유= " + e.getMessage());
        }
        PreparedStatement pstmt = null;
        String insertSQL = "INSERT INTO my_note (user_id,question_id) VALUES (?,?)";
        try {
            for (int i = 0; i < question_id_list.length; i++) {
                pstmt = con.prepareStatement(insertSQL);
                pstmt.setString(1, user_id);
                pstmt.setString(2, question_id_list[i]);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                throw new AddException("이미 존재하는 정보 입니다.");
            } else {
                e.printStackTrace();
            }
        } finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public void deleteMNById(String user_id, String question_id) throws RemoveException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            throw new RemoveException(e.getMessage());
        }
        String deleteSQL = "delete from my_note where user_id = ? and question_id = ?";
        try {
            pstmt = con.prepareStatement(deleteSQL);
            pstmt.setString(1, user_id);
            pstmt.setString(2, question_id);
            int rowcnt = pstmt.executeUpdate();
            if (rowcnt != 1) {
                throw new RemoveException("삭제실패: 해당 문제가 없습니다");
            }
        } catch (SQLException e) {
            throw new RemoveException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public List<Question> selectSQById(String user_id, int n) throws FindException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select * from (select * from question_solved qs join question q on qs.question_id=q.question_id left join my_note mn on qs.question_id = mn.question_id and mn.user_id=? where qs.user_id=? order by qs.solved_date desc) where rownum<=?";
        List<Question> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1, user_id);
            pstmt.setString(2, user_id);
            pstmt.setInt(3, n);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setQuestion_id(rs.getString("question_id"));
                question.setContent(rs.getString("content"));
                question.setCorrect_answer(rs.getString("correct_answer"));
                question.setExplanation(rs.getString("explanation"));
                question.setTotal_answer_count(Integer.parseInt(rs.getString("total_answer_count")));
                question.setCorrect_answer_count(Integer.parseInt(rs.getString("correct_answer_count")));
                question.setQuestion_ox(Integer.parseInt(rs.getString("question_ox")));
                if (rs.getString("mn_date") == null) {
                    question.setMn_id("");
                } else {
                    question.setMn_id("V");
                }
                all.add(question);
            }
            if (all.size() == 0) {
                throw new FindException("문제가 하나도 없습니다.");
            }

            return all;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public List<Question> selectAll() throws FindException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select * from question";
        List<Question> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setQuestion_id(rs.getString("question_id"));
                question.setContent(rs.getString("content"));
                question.setCorrect_answer(rs.getString("correct_answer"));
                question.setExplanation(rs.getString("explanation"));
                question.setTotal_answer_count(Integer.parseInt(rs.getString("total_answer_count")));
                question.setCorrect_answer_count(Integer.parseInt(rs.getString("correct_answer_count")));
                all.add(question);
            }
            if (all.size() == 0) {
                throw new FindException("문제가 하나도 없습니다.");
            }

            return all;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public void insertQTmpByQYear(String user_id, String question_year) throws AddException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException(e.getMessage());
        }
        String deleteByIdSQL = "DELETE FROM question_tmp WHERE user_id = ?";
        String selectByIdSQL = "SELECT * FROM question WHERE substr(question_id,0,6) = ? order by question_id";
        String insertByIdSQL = "INSERT INTO question_tmp (question_id, user_id, question_answer) VALUES (?,?,?)";

        List<Question> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(deleteByIdSQL);
            pstmt.setString(1, user_id);
            pstmt.executeUpdate();

            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1, question_year);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setQuestion_id(rs.getString("question_id"));
                question.setCorrect_answer(rs.getString("correct_answer"));
                all.add(question);
            }
            if (all.size() == 0) {
                throw new AddException("문제가 하나도 없습니다.");
            }

            pstmt = con.prepareStatement(insertByIdSQL);
            for (Question q : all) {
                pstmt.setString(1, q.getQuestion_id());
                pstmt.setString(2, user_id);
                pstmt.setString(3, q.getCorrect_answer());

                pstmt.addBatch();
                pstmt.clearParameters();
            }
            pstmt.executeBatch();
            pstmt.clearParameters();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AddException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public void insertRandomQTmp(String user_id) throws AddException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException(e.getMessage());
        }
        String deleteByIdSQL = "DELETE FROM question_tmp WHERE user_id = ?";
        String selectByIdSQL = "select * from (select q.question_id,q.correct_answer, count(*) cnt from question q left join question_solved qs on \n" +
                "q.question_id = qs.question_id  and qs.user_id =? where substr(q.question_id,8,1) = ?  group by q.question_id,q.correct_answer order by cnt) where rownum <= 100";
        String insertByIdSQL = "INSERT INTO question_tmp (question_id, user_id, question_answer) VALUES (?,?,?)";

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
        try {
            pstmt = con.prepareStatement(deleteByIdSQL);
            pstmt.setString(1, user_id);
            pstmt.executeUpdate();

            pstmt = con.prepareStatement(selectByIdSQL);
            for(int i =1;i<=5;i++) {
                List<Question> all = new ArrayList<>();
                pstmt.setString(1, user_id);
                pstmt.setInt(2, i);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Question question = new Question();
                    question.setQuestion_id(rs.getString("question_id"));
                    question.setCorrect_answer(rs.getString("correct_answer"));
                    all.add(question);
                }
                if (all.size() == 0) {
                    throw new AddException("문제가 하나도 없습니다.");
                }
                for(int j=0;j<20;j++){
                    tmpAll.add(all.get(randomArr[j]));
                }
            }
            pstmt = con.prepareStatement(insertByIdSQL);
            for (Question q : tmpAll) {
                pstmt.setString(1, q.getQuestion_id());
                pstmt.setString(2, user_id);
                pstmt.setString(3, q.getCorrect_answer());

                pstmt.addBatch();
                pstmt.clearParameters();
            }
            pstmt.executeBatch();
            pstmt.clearParameters();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AddException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public Question selectQTmpByQId(String user_id, String row_num) throws FindException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

//        String selectByIdSQL = "select * from (select B.rnum br, B.* , (select count(*) from question_tmp where user_id= ?) cnt from (select rownum as rnum, A.* from \n" +
//                "(select q.* from question q join question_tmp qt on q.QUESTION_ID = qt.QUESTION_ID where qt.user_id = ? \n" +
//                "order by substr(q.question_id,8,1)) A where rownum <=100) B where B.rnum <= ?) C where C.br>=?";
        String selectByIdSQL = "select A.* , (select count(*) from question_tmp where user_id= ?) cnt from \n" +
                "(select q.* from question q join question_tmp qt on q.QUESTION_ID = qt.QUESTION_ID where qt.user_id = ? and substr(q.question_id,8,1)= ?\n" +
                ") A  order by question_id";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            List<Question> all = new ArrayList<>();
            for(int i =1; i<=5 ; i++) {
                pstmt.setString(1, user_id);
                pstmt.setString(2, user_id);
                pstmt.setInt(3, i);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Question question = new Question();
                    question.setQuestion_id(rs.getString("question_id"));
                    question.setContent(rs.getString("content"));
                    question.setCorrect_answer(rs.getString("correct_answer"));
                    question.setExplanation(rs.getString("explanation"));
                    question.setTotal_answer_count(Integer.parseInt(rs.getString("total_answer_count")));
                    question.setCorrect_answer_count(Integer.parseInt(rs.getString("correct_answer_count")));
                    question.setMn_total(Integer.parseInt(rs.getString("cnt")));
                    all.add(question);
                }
                if (all.size() == 0) {
                    throw new FindException("문제가 하나도 없습니다.");
                }
            }

            return all.get(Integer.parseInt(row_num)-1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public void updateQTmpByQ(String user_id, String[] question_answer_list) throws ModifyException {
        Connection con = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ModifyException("채점 실패: 이유= " + e.getMessage());
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectByIdSQL = "SELECT qt.question_answer,qt.question_id, q.total_answer_count, q.correct_answer_count FROM question_tmp qt join question q on qt.QUESTION_ID = q.question_id WHERE user_id= ? and substr(q.question_id,8,1)= ? order by question_id";
        String updateByIdSQL1 = "update question_tmp set QUESTION_OX=? where question_id = ? and user_id=?";
        String updateByIdSQL2 = "update question set total_answer_count = ?, correct_answer_count = ? where question_id = ?";
        String insertByIdSQL = " INSERT INTO question_solved (user_id, question_id, question_ox) VALUES (?,?,?)";
        List<Question> qall = new ArrayList<>();
        List<Question> ox = new ArrayList<>();
        try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(selectByIdSQL);
            for (int j =1; j<=5;j++) {
                pstmt.setString(1, user_id);
                pstmt.setInt(2, j);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Question q = new Question();
                    q.setQuestion_id(rs.getString("question_id"));
                    q.setCorrect_answer(rs.getString("question_answer"));
                    q.setTotal_answer_count(rs.getInt("total_answer_count"));
                    q.setCorrect_answer_count(rs.getInt("correct_answer_count"));
                    qall.add(q);
                }
                if (qall.size() == 0) {
                    throw new ModifyException("문제가 하나도 없습니다.");
                }
                for (int i = 0; i < question_answer_list.length; i++) {
                    Question question = qall.get(i);
                    if (question_answer_list[i].equals(question.getCorrect_answer())) {
                        question.setQuestion_ox(1);
                    } else {
                        question.setQuestion_ox(0);
                    }
                    ox.add(question);
                }
            }
            pstmt = con.prepareStatement(updateByIdSQL1);
            for (Question q : ox) {
                pstmt.setInt(1, q.getQuestion_ox());
                pstmt.setString(2, q.getQuestion_id());
                pstmt.setString(3, user_id);

                pstmt.addBatch();
                pstmt.clearParameters();
            }
            pstmt.executeBatch();
            pstmt.clearParameters();

            pstmt = con.prepareStatement(updateByIdSQL2);
            for (Question q : ox) {
                pstmt.setInt(1, q.getTotal_answer_count()+1);
                if(q.getQuestion_ox() == 1){
                    pstmt.setInt(2, q.getCorrect_answer_count() +1);
                }else {
                    pstmt.setInt(2, q.getCorrect_answer_count());
                }
                pstmt.setString(3, q.getQuestion_id());

                pstmt.addBatch();
                pstmt.clearParameters();
            }
            pstmt.executeBatch();
            pstmt.clearParameters();

            pstmt = con.prepareStatement(insertByIdSQL);

            for (Question q : ox) {
                pstmt.setString(1, user_id);
                pstmt.setString(2, q.getQuestion_id());
                pstmt.setInt(3, q.getQuestion_ox());

                pstmt.addBatch();
                pstmt.clearParameters();
            }
            pstmt.executeBatch();
            pstmt.clearParameters();

            con.commit();
        } catch (SQLException e) {
            try {
                con.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if (e.getErrorCode() == 1) {
                throw new ModifyException("이미 존재하는 정보 입니다.");
            } else {
                e.printStackTrace();
            }
        } finally {
            MyConnection.close(con, pstmt);
        }

    }

    @Override
    public List<Question> selectAfterSolveQByRound(String user_id, String question_round) throws FindException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
                con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select * from question_tmp where substr(question_id,7,2) = ? and user_id = ? order by question_id";
        List<Question> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1, question_round);
            pstmt.setString(2, user_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setQuestion_id(rs.getString("question_id"));
                question.setQuestion_ox(rs.getInt("question_ox"));
                all.add(question);
            }
            if (all.size() == 0) {
                throw new FindException("문제가 하나도 없습니다.");
            }

            return all;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        } finally {
            MyConnection.close(con, pstmt, rs);
        }
    }
}