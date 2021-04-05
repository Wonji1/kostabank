package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.sql.MyConnection;
import com.my.vo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserDAOOracle implements UserDAO {
    @Override
    public User selectById(String user_id) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByIdSQL = "SELECT * FROM USERS WHERE user_id = ?";
        try {
            pstmt = con.prepareStatement(selectByIdSQL);
            pstmt.setString(1,user_id);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("아이디에 해당하는 값이 없습니다.");
            }
            User user = new User(rs.getString("user_id"),rs.getString("user_nickname"),rs.getString("user_pwd"),rs.getString("user_email"), rs.getString("user_date"),rs.getInt("user_adm"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public User selectByNick(String user_nick) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByNickSQL = "SELECT * FROM USERS WHERE user_nickname = ?";
        try {
            pstmt = con.prepareStatement(selectByNickSQL);
            pstmt.setString(1,user_nick);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("닉네임에 해당하는 값이 없습니다.");
            }
            User user = new User(rs.getString("user_id"),rs.getString("user_nickname"),rs.getString("user_pwd"),rs.getString("user_email"), rs.getString("user_date"),rs.getInt("user_adm"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public User selectByEmail(String user_email) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByEmailSQL = "SELECT * FROM USERS WHERE user_email = ?";
        try {
            pstmt = con.prepareStatement(selectByEmailSQL);
            pstmt.setString(1,user_email);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("이메일에 해당하는 값이 없습니다.");
            }
            User user = new User(rs.getString("user_id"),rs.getString("user_nickname"),rs.getString("user_pwd"),rs.getString("user_email"), rs.getString("user_date"),rs.getInt("user_adm"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public String selectTmpByEmail(String email) throws FindException {
        Connection con =null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectByEmailSQL = "SELECT * FROM EMAIL_TMP WHERE email = ?";
        try {
            pstmt = con.prepareStatement(selectByEmailSQL);
            pstmt.setString(1,email);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                throw new FindException("5분이 지났습니다. 다시 인증번호를 발송해주세요.");
            }
            return rs.getString("check_number");
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new FindException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt,rs);
        }
    }

    @Override
    public List<User> selectAll(int n) throws FindException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }

        String selectSQL = "select B.rnum, B.user_id, B.user_nickname, B.user_pwd, B.user_email, B.user_date, B.user_adm\n" +
                "from\n" +
                "    (select rownum as rnum, A.user_id, A.user_nickname, A.user_pwd, A.user_email, A.user_date, A.user_adm\n" +
                "    from (\n" +
                "        select user_id, user_nickname, user_pwd, user_email, user_date, user_adm\n" +
                "        from users\n" +
                "        order by user_date desc ) A\n" +
                "    where rownum <= ? ) B\n" +
                "where B.rnum >= 1";
        try {
            pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1,n);
            rs = pstmt.executeQuery();
            List<User> list = new ArrayList<>();
            while(rs.next()){
                String id = rs.getString("user_id");
                String nickname = rs.getString("user_nickname");
                String pwd = rs.getString("user_pwd");
                String email = rs.getString("user_email");
                String date = rs.getString("user_date");
                int adm = rs.getInt("user_adm");
                User user = new User(id,nickname,pwd,email,date,adm);
                list.add(user);
            }
            if(list.size() == 0){
                throw new FindException("회원이 한명도 없습니다");
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException(e.getMessage());
        }finally {
            MyConnection.close(con, pstmt, rs);
        }
    }

    @Override
    public void insert(User user) throws AddException {
        Connection con = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException("고객 추가 실패: 이유= "+ e.getMessage());
        }
        PreparedStatement pstmt = null;
        String insertSQL = "INSERT INTO users (user_id,user_nickname,user_pwd,user_email) VALUES (?,?,?,?)";
        try {
            pstmt = con.prepareStatement(insertSQL);
            pstmt.setString(1,user.getUser_id());
            pstmt.setString(2,user.getUser_nickname());
            pstmt.setString(3,user.getUser_pwd());
            pstmt.setString(4,user.getUser_email());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if(e.getErrorCode() == 1){
                throw new AddException("이미 존재하는 정보 입니다.");
            }else {
                e.printStackTrace();
            }
        } finally {
            MyConnection.close(con,pstmt);
        }
    }

    @Override
    public String insertEmail(String email) throws AddException {
        Connection con = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddException("이메일 인증 실패: 이유= "+ e.getMessage());
        }
        PreparedStatement pstmt = null;
        String insertSQL = "INSERT INTO email_tmp (email,check_number) VALUES (?,?)";
        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for(int i=0;i<8;i++) {
            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }
        try {
            pstmt = con.prepareStatement(insertSQL);
            pstmt.setString(1,email);
            pstmt.setString(2,numStr);
            pstmt.executeUpdate();
            return numStr;
        } catch (SQLException e) {
            if(e.getErrorCode() == 1){
                throw new AddException("이미 인증번호가 발송된 이메일 입니다.");
            }else {
                throw new AddException(e.getMessage());
            }
        } finally {
            MyConnection.close(con,pstmt);
        }
    }

    @Override
    public void update(User user) throws ModifyException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new ModifyException("유저 수정 실패 이유:" +e.getMessage());
        }
        String updateSQL = "UPDATE users SET user_nickname = ?, user_pwd =? WHERE user_id = ?";
        try {
            pstmt = con.prepareStatement(updateSQL);
            pstmt.setString(1,user.getUser_nickname());
            pstmt.setString(2,user.getUser_pwd());
            pstmt.setString(3,user.getUser_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw  new ModifyException(e.getMessage());
        }finally {
            MyConnection.close(con, pstmt);
        }
    }

    @Override
    public String updateByEmail(String user_email) throws ModifyException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new ModifyException("유저 수정 실패 이유:" +e.getMessage());
        }

        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for(int i=0;i<10;i++) {
            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }
        String updateSQL = "UPDATE users SET user_pwd =? WHERE user_email = ?";
        try {
            pstmt = con.prepareStatement(updateSQL);
            pstmt.setString(1,numStr);
            pstmt.setString(2,user_email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw  new ModifyException("존재하지 않는 이메일입니다.");
        }finally {
            MyConnection.close(con, pstmt);
        }
        return numStr;
    }

    @Override
    public void delete(String user_id) throws RemoveException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            throw new RemoveException(e.getMessage());
        }
        String deleteSQL = "DELETE FROM users WHERE user_id=?";
        try {
            pstmt = con.prepareStatement(deleteSQL);
            pstmt.setString(1,user_id);
            int rowcnt = pstmt.executeUpdate();
            if(rowcnt !=1){
                throw new RemoveException("삭제실패: 아이디에 해당 고객이 없습니다");
            }
        } catch (SQLException e) {
            throw new RemoveException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt);
        }
    }

    @Override
    public void deleteEmail(String email) throws RemoveException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = MyConnection.getConnection();
        } catch (Exception e) {
            throw new RemoveException(e.getMessage());
        }
        String deleteSQL = "DELETE FROM email_tmp WHERE email=?";
        try {
            pstmt = con.prepareStatement(deleteSQL);
            pstmt.setString(1,email);
            int rowcnt = pstmt.executeUpdate();
            if(rowcnt !=1){
                throw new RemoveException("삭제실패: 해당 이메일이 없습니다");
            }
        } catch (SQLException e) {
            throw new RemoveException(e.getMessage());
        }finally {
            MyConnection.close(con,pstmt);
        }
    }
}
