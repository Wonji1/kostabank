package com.my.sql;

import com.my.model.MyConfig;

import java.sql.*;

public class MyConnection {
    static{
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("JDBC드라이버 로드 성공");
    }

    public static Connection getConnection() throws Exception{
        String url = "jdbc:oracle:thin:@52.231.38.22:1521:xe";
        String user = "kosta";
        String password = new MyConfig().getDbPwd();
        Connection con = DriverManager.getConnection(url,user,password);

        return  con;
    }
    public static void close(Connection con, Statement stmt){
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close(con);
    }

    public static void close(Connection con, Statement stmt, ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close(con,stmt);
    }
    public static void close(Connection con){
        if(con!=null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
