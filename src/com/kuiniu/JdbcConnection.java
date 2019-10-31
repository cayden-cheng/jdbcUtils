package com.kuiniu;

import java.sql.*;

public class JdbcConnection {
    private String url = "jdbc:mysql://192.168.2.60:3306/demo?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT";
    private String userName = "root";
    private String passWord = "123456";

    public static void main(String[] args) {
        JdbcConnection jdbcConnection = new JdbcConnection();
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;


        try {
            //1.注册驱动
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());

            //2.建立连接
            //方法一  参数一：协议+访问数据库，参数二：用户名，参数三：密码
            connection = DriverManager.getConnection(jdbcConnection.getUrl(), jdbcConnection.getUserName(), jdbcConnection.getPassWord());

            //方法二
//            DriverManager.getConnection("jdbc:msql://localhost/student?user=root&password=password");

            //3.创建statement，跟数据库打交道一定需要这个对象
            st = connection.createStatement();

            //4.执行查询
            String sql = "select * from iot_tb_product";
            rs = st.executeQuery(sql);

            //5.遍历查询每一条记录
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("equ_name");
                int age = rs.getInt("equ_center_num");

                System.out.println("查询成功" + id + name + age);
            }
            //进行资源释放
            connection.close();
            st.close();
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
