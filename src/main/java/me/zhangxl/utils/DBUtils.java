package me.zhangxl.utils;

import com.sun.corba.se.impl.orbutil.HexOutputStream;
import me.zhangxl.config.DBConfig;
import me.zhangxl.entity.Person;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.sql.*;

/**
 * Created by zhangxiaolong on 16/11/7.
 */
public class DBUtils {

    public static boolean containUrl(String url) {
        try {
            String result = getMd5(url);
            Statement statement = DataConnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) from user where hashId=" + "'" +result + "'");
            resultSet.next();
            return resultSet.getInt("count(*)") > 0;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean insertData(Person person) {
        Connection connection = DataConnection.getConnection();
        StringBuilder questionMark = new StringBuilder();
        int size = Person.class.getDeclaredFields().length;
        for (int i = 1; i <= size; i++) {
            questionMark.append("?");
            if (i < size) {
                questionMark.append(",");
            }
        }
        String sql = "insert into user values(" + questionMark.toString() + ")";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,person.getId());
            ps.setString(2,person.getName());
            ps.setString(3,person.getPhotoUrl());
            ps.setString(4,person.getShortIntroduction());
            ps.setString(5,person.getLocation());
            ps.setString(6,person.getBusiness());
            ps.setString(7,person.getGenderAsString());
            ps.setString(8,person.getEmployment());
            ps.setString(9,person.getPosition());
            ps.setString(10,person.getEducation());
            ps.setString(11,person.getEducationExtra());
            ps.setString(12,person.getDescription());
            ps.setInt(13,person.getAgreeNum());
            ps.setInt(14,person.getThanksNum());
            ps.setInt(15,person.getAskNum());
            ps.setInt(16,person.getAnswerNum());
            ps.setInt(17,person.getPostNum());
            ps.setInt(18,person.getCollectionNum());
            ps.setInt(19,person.getLogNum());
            ps.setInt(20,person.getFolloweeNum());
            ps.setInt(21,person.getFollwerNum());
            int result = ps.executeUpdate();
            ps.close();
            return result == 1;
        } catch (Throwable e) {
            throw new IllegalStateException(person.toString(),e);
        }
    }

    public static void init() {
        Connection connection = DataConnection.getConnection();
        try {
            ResultSet result = connection.getMetaData().getTables(null, null, "user", null);
            if (!result.next()) {
                //一个条目都没有，说明不存在这个表
                Statement statement = connection.createStatement();
                statement.execute(DBConfig.createSql);
                System.out.println("user create success");
            } else {
                System.out.println("user table already exists");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getMd5(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(value.getBytes("utf-8"));
            byte[] result = digest.digest();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(result);
            StringWriter writer = new StringWriter();
            OutputStream outputStream = new HexOutputStream(writer);
            IOUtils.copy(inputStream,outputStream);
            return writer.toString();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args){
        System.out.println(getMd5("223444444"));
    }
}
