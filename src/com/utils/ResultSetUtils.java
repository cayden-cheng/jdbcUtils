package com.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chengliang
 */
public class ResultSetUtils {

    private static Statement st;

    public static Integer getInteger(ResultSet rs, String field) throws SQLException {
        if (rs == null) {
            return null;
        }
        Integer result = null;
        while (rs.next()) {
            result = rs.getInt(field);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public static List<Integer> getIntegerList(ResultSet rs, String field) throws SQLException {
        if (rs == null) {
            return new ArrayList<>();
        }

        List<Integer> result = null;
        while (rs.next()) {
            Integer rs1 = rs.getInt(field);
            result.add(rs1);
        }
        return result;
    }

    public static <T> List<T> convertToBean(ResultSet rs, Class clazz) throws SQLException, IllegalAccessException, InstantiationException {
        if (rs == null) {
            return new ArrayList<>();
        }

        List list = new ArrayList();
        Object result = null;
        Object obj = clazz.newInstance();
        ResultSetMetaData rsmd = rs.getMetaData();
        int culCount = rsmd.getColumnCount();
        Field[] fields = clazz.getDeclaredFields();

        while (rs.next()) {
            obj = clazz.newInstance();
            for (int i = 1; i < culCount + 1; i++) {
                result = rs.getObject(i);
                //寻找属性
                for (int j = 0; j < fields.length; j++) {
                    Field f = fields[j];
                    String columnName = UnderlineToHump(rsmd.getColumnName(i));
                    if (f.getName().equalsIgnoreCase(columnName)) {
                        boolean flag = f.isAccessible();
                        try {
                            f.setAccessible(true);
                            f.set(obj, result);
                            f.setAccessible(flag);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            T t = (T) clazz.newInstance();
            t = (T) obj;
            list.add(t);
        }
        DbUtils.release(DbUtils.getConnection(), st, rs);
        return list;
    }

    private static String sqlTransformation(String sql, Object... args) {
        sql += " ";
        checkOutValueIsRight(sql, args);
        try {
            char[] sqlChar = sql.toCharArray();

            int argsIndex = 0;
            List<Integer> indexArr = new ArrayList<>();
            for (int i = 0; i < sqlChar.length; i++) {
                char value = sqlChar[i];
                if (value == '?') {
                    indexArr.add(i);
                }
            }

            int fast = 0;
            for (Integer indexOut : indexArr) {
                indexOut += fast;
                System.out.println(sql.length());
                String sqlLast = "";
                if (indexOut == sql.length() - 1) {
                    sqlLast = sql.substring(indexOut);
                } else {
                    sqlLast = sql.substring(indexOut + 1, sql.length());
                }

                String startSql = sql.substring(0, indexOut);
                String valueSql = args[argsIndex].toString();
                fast += valueSql.length() - 1;
                sql = startSql + valueSql + sqlLast;
                argsIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(sql);
        }
        return sql;
    }


    public static ResultSet query(String sql, Object... args) throws SQLException {
        st = DbUtils.getStatement();
        ResultSet rs = null;

        sql = sqlTransformation(sql, args);
        rs = st.executeQuery(sql);
        return rs;
    }

    public static Boolean update(String sql, Object... args) throws SQLException {
        st = DbUtils.getStatement();
        ResultSet rs = null;
        sql = sqlTransformation(sql, args);
        int result = st.executeUpdate(sql);
        DbUtils.release(DbUtils.getConnection(), st, rs);
        return result > 0;
    }

    private static void checkOutValueIsRight(String sql, Object... args) {
        String newSql = sql;
        int count = sql.length() - newSql.replace("?", "").length();
        int argsCount = args.length;

        if (argsCount != count) {
            throw new ValueLackException("值匹配异常,请检查");
        }
    }


    public static void main(String[] args) {
        /*String test = "test_api";
        String s = UnderlineToHump(test);
        System.out.println(s);*/

        String sql = "select * from xxx where id = ? and sql = ? and c = ?";
        System.out.println(sqlTransformation(sql, "aaa", "ccc", "bbb"));
    }

    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String UnderlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("_");
        for (String s : a) {
            if (!para.contains("_")) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String HumpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

}
