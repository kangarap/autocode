package com.code.auto;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * file工具类
 */
public class FileUtil {


    public static String readProperties(String key){
        try {
            //读取配置文件
            InputStream is = FileUtil.class.getClassLoader().getResourceAsStream("src/main/resources/application.properties");
            Properties properties = new Properties();
            properties.load(is);

            return properties.getOrDefault(key, "").toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 创建文件
     *
     * @param pathNameAndFileName 路径跟文件名
     * @return File对象
     */
    protected static File createFile(String pathNameAndFileName) {
        File file = new File(pathNameAndFileName);

        try {
            //获取父目录
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                boolean res = fileParent.mkdirs();
            }
            //创建文件
            if (!file.exists()) {
                boolean res = file.createNewFile();
            }
        } catch (Exception e) {
            file = null;
            System.err.println("新建文件操作出错");
            //输出到日志文件中
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 字符流写入文件
     *
     * @param file         file对象
     * @param stringBuffer 要写入的数据
     */
    protected static void fileWriter(File file, StringBuffer stringBuffer) {
        //字符流
        try {
            FileWriter resultFile = new FileWriter(file, false);//true,则追加写入 false,则覆盖写入
            PrintWriter myFile = new PrintWriter(resultFile);
            //写入
            myFile.println(stringBuffer.toString());

            myFile.close();
            resultFile.close();
        } catch (Exception e) {
            System.err.println("写入操作出错");
            //输出到日志文件中
            e.printStackTrace();
        }
        System.out.println(file.getAbsolutePath());
    }
}

