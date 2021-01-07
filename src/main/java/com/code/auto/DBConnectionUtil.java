package com.code.auto;

import lombok.Data;

import java.sql.*;

import static com.code.auto.FileUtil.readProperties;


/**
 * JDBC连接数据库工具类
 */
@Data
public class DBConnectionUtil {

    public String url;
    public String username;
    public String password;
    public String driver_name;

    public DBConnectionUtil() throws RuntimeException{

        if("".equals(url) || "".equals(username) || "".equals(password) || "".equals(driver_name))
        {
            throw new RuntimeException("sql params not set!");
        }

        String url = readProperties("auto.code.mysql.url");
        String username = readProperties("auto.code.mysql.username");
        String password = readProperties("auto.code.mysql.password");
        String driver_name = readProperties("auto.code.mysql.driver-class");

        System.out.println(driver_name);
        this.url = url;
        this.username = username;
        this.password = password;
        this.driver_name = driver_name;
    }



    /**
     * 返回一个Connection连接
     */
    Connection getConnection() {

        Connection conn = null;

        // 1、加载驱动
        try {
            Class.forName(this.driver_name);
        } catch (ClassNotFoundException e) {
            //输出到日志文件中
            e.printStackTrace();
        }

        // 2、连接数据库
        try {
            conn = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            //输出到日志文件中
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭Connection，Statement连接
     */
    public static void close(Connection conn, Statement stmt) {
        try {
            conn.close();
            stmt.close();
        } catch (SQLException e) {
            //输出到日志文件中
            e.printStackTrace();
        }
    }

    /**
     * 关闭Connection，Statement，ResultSet连接
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            close(conn, stmt);
            rs.close();
        } catch (SQLException e) {
            //输出到日志文件中
            e.printStackTrace();
        }
    }
}
