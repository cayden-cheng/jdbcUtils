package com.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Test {

    public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException {
        //4.执行查询
        String sql = "update iot_tb_product set equ_name = ? where id = ?";
        ResultSet rs = ResultSetUtils.query(sql,"jack",1);
        List<IotTbProduct> result = ResultSetUtils.convertToBean(rs, IotTbProduct.class);
        System.out.println(result.get(0).getId());

        /*//5.遍历查询每一条记录
        while(rs.next()) {
            Object object = rs.getObject(2);
            System.out.println(object.toString());
            int id = rs.getInt("id");
            String name = rs.getString("equ_name");
            int age = rs.getInt("equ_center_num");

            System.out.println("查询成功" + id + name + age);
        }*/

    }
}
