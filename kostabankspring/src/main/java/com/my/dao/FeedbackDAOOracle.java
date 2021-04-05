package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.sql.MyConnection;
import com.my.vo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAOOracle implements FeedbackDAO {
    @Override
    public List<Feedback> selectFeedbackById(String user_id) throws Exception {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        String selectByIdSQL = "select * from ( select report_id, report_id id, report_title, report_sol_wdate, report_new from report where report_user = ? and report_new =1 \n" +
                "union all select qa_id,-1, qa_title, qa_sol_wdate, qa_new from qa where user_id= ? and qa_new =1 order by report_sol_wdate desc) where rownum<=10";
        List<Feedback> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,user_id);
            pstmt.setString(2,user_id);
            rs = pstmt.executeQuery();
            int code;
            while(rs.next()){
                if(rs.getString("id").equals("-1")){
                    code = 0;
                }else{code=1;}
                Feedback f = new Feedback(code,rs.getString("report_id"),rs.getString("report_title"),rs.getString("report_sol_wdate"));

                all.add(f);
            }
            if(all.size() == 0){
                throw new Exception("정보가 하나도 없습니다.");
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new Exception(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public Qa UserQaById(String user_id, int rownum) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select B.rnum, B.* , (select count(*) from qa where user_id= ?) qa from\n" +
                "    (select rownum as rnum, A.* from (\n" +
                "        select * from qa q join users u on q.user_id = u.user_id where q.user_id = ? \n" +
                "        order by q.qa_status, q.qa_wdate desc ) A where rownum <=?) B where B.rnum >= ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,user_id);
            pstmt.setString(2,user_id);
            pstmt.setInt(3,rownum);
            pstmt.setInt(4,rownum);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("정보가 하나도 없습니다.");
            }

            Qa qa = new Qa(rs.getString("qa_id"),rs.getString("qa_title"), rs.getString("qa_content"), rs.getString("qa_wdate")
                    ,rs.getString("qa_file"), rs.getInt("qa_new"),rs.getInt("qa_status"),rs.getString("qa_sol_wdate"),rs.getString("qa_sol_content"),rs.getInt("qa"));
            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            qa.setUser(user);
            return qa;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public Qa QaByQaId(String qa_id, int n, int s) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "SELECT * FROM qa q join users u on q.user_id = u.user_id WHERE qa_id = ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,qa_id);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("문의 아이디에 해당하는 값이 없습니다.");
            }
            Qa qa = new Qa(rs.getString("qa_id"),rs.getString("qa_title"), rs.getString("qa_content"), rs.getString("qa_wdate")
                    ,rs.getString("qa_file"), rs.getInt("qa_new"),rs.getInt("qa_status"),rs.getString("qa_sol_wdate"),rs.getString("qa_sol_content"),1);
            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            qa.setUser(user);
            NewStatusQaUpdate(con,qa_id,n,s);
            return qa;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }


    @Override
    public List<Qa> QaAll(int page, int num) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select B.rnum, B.* , (select count(*) from qa) qa from\n" +
                "    (select rownum as rnum, A.* from (\n" +
                "        select * from qa q join users u on q.user_id = u.user_id \n" +
                "        order by q.qa_wdate desc ) A where rownum <=?) B where B.rnum >= ?";
        List<Qa> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setInt(1,page*num);
            pstmt.setInt(2,(page-1)*num +1);
            rs = pstmt.executeQuery();
            while(rs.next()){
                Qa qa = new Qa(rs.getString("qa_id"),rs.getString("qa_title"), rs.getString("qa_content"), rs.getString("qa_wdate")
                        ,rs.getString("qa_file"), rs.getInt("qa_new"),rs.getInt("qa_status"),rs.getString("qa_sol_wdate"),rs.getString("qa_sol_content"),rs.getInt("qa"));
                User user = new User();
                user.setUser_nickname(rs.getString("user_nickname"));
                qa.setUser(user);
                all.add(qa);
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
    public void QaInsert(Qa qa) throws AddException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException(e.getMessage());
        }

        String insertSQL = "INSERT INTO qa(qa_id, user_id, qa_title, qa_content, qa_wdate, qa_file)\r\n" +
                "    	VALUES (?, ?, ?, ?, SYSDATE, ?)";

        try {
            pstmt = con.prepareStatement(insertSQL);
            pstmt.setString(1, qa.getQa_id());
            pstmt.setString(2, qa.getUser().getUser_id());
            pstmt.setString(3, qa.getQa_title());
            pstmt.setString(4, qa.getQa_content());
            pstmt.setString(5, qa.getQa_file());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AddException();
        }finally {
            MyConnection.close(con, pstmt);
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
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "SELECT * FROM report r join users u on r.report_user = u.user_id join question q on r.report_question_id = q.question_id WHERE report_id = ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,report_id);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("신고 아이디에 해당하는 값이 없습니다.");
            }
            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            Question question = new Question();
            question.setContent(rs.getString("content"));
            question.setCorrect_answer(rs.getString("correct_answer"));
            question.setExplanation(rs.getString("explanation"));
            Report report = new Report(rs.getString("report_id"),user,question,rs.getString("report_title"),rs.getString("report_content"), rs.getString("report_wdate"),
                                    rs.getInt("report_new"),rs.getInt("report_status"), rs.getString("report_sol_wdate"),rs.getString("report_sol_content"), 1);
            NewStatusReportUpdate(con,report_id,n,s);
            return report;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public List<Report> ReportAll(int page, int num) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "select B.rnum, B.* , (select count(*) from report) total from\n" +
                "    (select rownum as rnum, A.* from (\n" +
                "        select * from report r join users u on r.report_user = u.user_id join question q on r.report_question_id = q.question_id \n" +
                "        order by r.report_status, r.report_wdate desc ) A where rownum <=?) B where B.rnum >= ?";
        List<Report> all = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setInt(1,page*num);
            pstmt.setInt(2,(page-1)*num +1);
            rs = pstmt.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setUser_nickname(rs.getString("user_nickname"));
                Question question = new Question();
                question.setContent(rs.getString("content"));
                question.setCorrect_answer(rs.getString("correct_answer"));
                question.setExplanation(rs.getString("explanation"));
                Report report = new Report(rs.getString("report_id"),user,question,rs.getString("report_title"),rs.getString("report_content"), rs.getString("report_wdate"),
                        rs.getInt("report_new"),rs.getInt("report_status"), rs.getString("report_sol_wdate"),rs.getString("report_sol_content"),rs.getInt("total"));

                all.add(report);
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
    public void ReportInsert(Report report) throws AddException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException(e.getMessage());
        }

        String insertSQL = "INSERT INTO report(report_user, report_title, report_content, report_question_id)\r\n" +
                "    	VALUES (?, ?, ?, ?)";

        try {
            pstmt = con.prepareStatement(insertSQL);
            pstmt.setString(1, report.getUser().getUser_id());
            pstmt.setString(2, report.getReport_title());
            pstmt.setString(3, report.getReport_content());
            pstmt.setString(4, report.getQuestion().getQuestion_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AddException();
        }finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public void ReportUpdate(Report report) throws ModifyException {

    }

    @Override
    public void ReportSolUpdate(String report_id, String content) throws ModifyException {
        Connection con =null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ModifyException(e.getMessage());
        }

        String updateByIdSQL = "update report set report_sol_content = ?,report_sol_wdate = current_timestamp , report_new =1 ,report_status=2 where report_id= ?";
        try {
            pstmt = con.prepareStatement(updateByIdSQL);
            pstmt.setString(1, content);
            pstmt.setString(2, report_id);
            int rowcnt = pstmt.executeUpdate();
            if(rowcnt !=1){
                throw new ModifyException("수정실패: 해당 신고아이디가 없습니다");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ModifyException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt);
        }
    }

    @Override
    public void QaSolUpdate(String qa_id, String content) throws ModifyException {
        Connection con =null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ModifyException(e.getMessage());
        }

        String updateByIdSQL = "update qa set qa_sol_content = ?,qa_sol_wdate = current_timestamp , qa_new =1 ,qa_status=2 where qa_id= ?";
        try {
            pstmt = con.prepareStatement(updateByIdSQL);
            pstmt.setString(1, content);
            pstmt.setString(2, qa_id);
            int rowcnt = pstmt.executeUpdate();
            if(rowcnt !=1){
                throw new ModifyException("수정실패: 해당 신고아이디가 없습니다");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ModifyException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt);
        }
    }

    @Override
    public void ReportDelete(String report_id) throws RemoveException {

    }

    @Override
    public void QADeleteByList(String[] qa_id_list) throws RemoveException {
        Connection con = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoveException("문의사항 삭제 실패: 이유= " + e.getMessage());
        }
        PreparedStatement pstmt = null;
        String deleteSQL = "DELETE FROM qa WHERE qa_id = ?";
        try {
            for (int i = 0; i < qa_id_list.length; i++) {
                pstmt = con.prepareStatement(deleteSQL);
                pstmt.setString(1, qa_id_list[i]);
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
    public int qaNextId() throws FindException {
        Connection con = null;
        PreparedStatement pstmt =null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
            String selectNextIdSQL = "SELECT AUTO_INCRE_QA.NEXTVAL FROM dual";

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

    public void NewStatusReportUpdate(Connection con,String report_id, int n, int s){
        PreparedStatement pstmt = null;
        String updateByIdSQL = "update report set report_new = ? , report_status=? where report_id= ?";
        try {
            pstmt = con.prepareStatement(updateByIdSQL);
            pstmt.setInt(1,n);
            pstmt.setInt(2,s);
            pstmt.setString(3, report_id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            MyConnection.close(con,pstmt);
        }
    }

    public void NewStatusQaUpdate(Connection con,String qa_id, int n, int s){
        PreparedStatement pstmt = null;
        String updateByIdSQL = "update qa set qa_new = ? , qa_status=? where qa_id= ?";
        try {
            pstmt = con.prepareStatement(updateByIdSQL);
            pstmt.setInt(1,n);
            pstmt.setInt(2,s);
            pstmt.setString(3, qa_id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            MyConnection.close(con,pstmt);
        }
    }
}
