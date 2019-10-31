package com.utils;

import java.sql.*;

/**
 * @author chengliang
 * 2019/10/28
 */
public class DbUtils {
    private static Connection connection = null;
    private static String url = "jdbc:mysql://192.168.2.60:3306/demo?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT";
    private static String userName = "root";
    private static String passWord = "123456";

    static {
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2.获得数据库的连接
            connection = DriverManager.getConnection(url, userName, passWord);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        return connection;
    }


    public static Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    /**
     * 资源释放
     * @param connection
     * @param st
     * @param rs
     */
    public static void release(Connection connection, Statement st, ResultSet rs) {
        closeConn(connection);
        closeRs(rs);
        closeSt(st);
    }

    private static void closeRs(ResultSet rs) {
        try {
            if(rs!=null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            rs = null;
            System.out.println("关闭ResultSet");
        }
    }

    private static void closeSt(Statement st) {
        try {
            if(st!=null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            st = null;
            System.out.println("关闭Statement");
        }
    }

    private static void closeConn(Connection connection) {
        try {
            if(connection!=null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connection = null;
            System.out.println("关闭connection");
        }
    }
}
