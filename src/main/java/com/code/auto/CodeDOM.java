package com.code.auto;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeDOM {

    private final String SLASH = System.getProperty("os.name").toLowerCase().contains("windows") ? "\\" : "/";
    private String tableName;
    private String package_;
    private String basePath;
    private String dealTableName;


    public CodeDOM(String tableName) {

        this.dealTableName = StringUtil.captureName(StringUtil.camelCaseName(tableName));
        this.tableName = tableName;
        basePath = FileUtil.readProperties("auto.code.gen.path");
        package_ = FileUtil.readProperties("auto.code.gen.package");
    }



    protected void createPojo(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "model" + SLASH + dealTableName + ".java");

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(
                "package " + package_ + ".model;\n" +
                        "\n" +
                        "import lombok.Data;\n" +
                        "import javax.persistence.*;\n" +
                        "import java.io.Serializable;\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "@Entity\n" +
                        "@Table(name = \"" + tableName + "\")\n" +
                        "@Data\n"
        );

        //主键个数
        int priNum = 0;
        StringBuffer stringBufferC = new StringBuffer();
        stringBufferC.append("public class " + dealTableName + " implements Serializable {\n");
        for (TableInfo tableInfo : tableInfos) {
            //主键
            if ("PRI".equals(tableInfo.getColumnKey())) {
                stringBufferC.append("    @Id\n");
                priNum ++;
            }
            //自增
            if ("auto_increment".equals(tableInfo.getExtra())) {
                stringBufferC.append("    @GeneratedValue(strategy= GenerationType.IDENTITY)\n");
            }
            stringBufferC.append("    private " + StringUtil.typeMapping(tableInfo.getDataType()) + " " + StringUtil.camelCaseName(tableInfo.getColumnName()) + ";//" + tableInfo.getColumnComment() + "\n\n");
        }


        if(priNum > 1)
        {
            File file1 = FileUtil.createFile(basePath + "model" + SLASH + dealTableName + "PK.java");
            StringBuffer stringBuffer1 = new StringBuffer();
            stringBuffer1.append("package " + package_ +".model;\n" +
                    "\n" +
                    "import lombok.Data;\n" +
                    "import javax.persistence.Id;\n" +
                    "import java.io.Serializable;\n" +
                    "\n" +
                    "@Data\n" +
                    "public class " + dealTableName + "PK implements Serializable {\n" +
                    "\n");

            tableInfos.stream().filter(tableInfo -> "PRI".equals(tableInfo.getColumnKey())).forEach(tableInfo -> {
                stringBuffer1.append("    @Id\n");
                stringBuffer1.append("    private " + StringUtil.typeMapping(tableInfo.getDataType()) + " " + StringUtil.camelCaseName(tableInfo.getColumnName()) + ";//" + tableInfo.getColumnComment() + "\n\n");

            });
            stringBuffer1.append("}");
            FileUtil.fileWriter(file1, stringBuffer1);

            stringBuffer.append("@IdClass(" + dealTableName + "PK.class)\n");
        }


        stringBuffer.append(stringBufferC);
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);

    }
    /**
     * 创建vo类
     */
    protected void createVo(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "vo" + SLASH + dealTableName + "Vo.java");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(
                "package " + package_+".vo;\n" +
                        "\n" +
                        "import lombok.Data;\n" +
                        "import java.io.Serializable;\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "@Data\n" +
                        "public class " + dealTableName + "Vo implements Serializable {\n"
        );
        //遍历设置属性
        for (TableInfo tableInfo : tableInfos) {
            stringBuffer.append("    private " + StringUtil.typeMapping(tableInfo.getDataType()) + " " + StringUtil.camelCaseName(tableInfo.getColumnName()) + ";//" + tableInfo.getColumnComment() + "\n\n");
        }
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);
    }

    /**
     * 创建repository类
     */
    protected void createRepository(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "repository" +SLASH + dealTableName + "Repository.java");
        StringBuffer stringBuffer = new StringBuffer();
        String t = "String";
        //遍历属性
        for (TableInfo tableInfo : tableInfos) {
            //主键
            if ("PRI".equals(tableInfo.getColumnKey())) {
                t = StringUtil.typeMapping(tableInfo.getDataType());
            }
        }
        stringBuffer.append(
                "package " + package_ + ".repository;\n" +
                        "\n" +
                        "import " + package_ + ".repository.*;\n" +
                        "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                        "import " + package_ + ".model." + dealTableName + ";\n" +
                        "import org.springframework.stereotype.Repository;\n" +
                        "\n" +
                        "@Repository\n" +
                        "public interface " + dealTableName + "Repository extends JpaRepository<" + dealTableName + ", " + t + "> {"
        );
        stringBuffer.append("\n");
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);
    }

    /**
     * 创建service类
     */
    protected void createService(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "service" + SLASH + dealTableName + "Service.java");
        StringBuffer stringBuffer = new StringBuffer();
        String t = "String";
        //遍历属性
        for (TableInfo tableInfo : tableInfos) {
            //主键
            if ("PRI".equals(tableInfo.getColumnKey())) {
                t = StringUtil.typeMapping(tableInfo.getDataType());
            }
        }
        stringBuffer.append(
                "package " + package_ + ".service;\n" +
                        "\n" +
                        "import " + package_ + ".service.*;\n" +
                        "import " + package_ + ".model." + dealTableName + ";\n" +
                        "import " + package_ + ".vo." + dealTableName + "Vo;\n" +
                        "\n" +
                        "public interface " + dealTableName + "Service {"
        );
        stringBuffer.append("\n");
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);

        //Impl
        File file1 = FileUtil.createFile(basePath + "service" + SLASH + dealTableName + "ServiceImpl.java");
        StringBuffer stringBuffer1 = new StringBuffer();
        stringBuffer1.append(
                "package " + package_.replaceAll(SLASH, ".") + "service;\n" +
                        "\n" +
                        "import " + package_ + ".service.*;\n" +
                        "import " + package_ + ".model." + dealTableName + ";\n" +
                        "import " + package_ + ".vo." + dealTableName + "Vo;\n" +
                        "import " + package_ + ".repository." + dealTableName + "Repository;\n" +
                        "import org.springframework.stereotype.Service;\n" +
                        "import javax.annotation.Resource;\n" +
                        "import org.springframework.transaction.annotation.Transactional;\n" +
                        "import javax.persistence.EntityManager;\n" +
                        "import javax.persistence.PersistenceContext;\n" +
                        "\n" +
                        "@Service\n" +
                        "@Transactional\n" +
                        "public class " + dealTableName + "ServiceImpl implements " + dealTableName + "Service {"
        );
        stringBuffer1.append("\n\n");
        stringBuffer1.append(
                "    @PersistenceContext\n" +
                        "    private EntityManager em;\n\n");

        stringBuffer1.append("" +
                "    @Resource\n" +
                "    private " + dealTableName + "Repository " + StringUtil.camelCaseName(tableName) + "Repository;\n");
        stringBuffer1.append("}");
        FileUtil.fileWriter(file1, stringBuffer1);
    }

    /**
     * 创建controller类
     */
    protected void createController(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "controller" + SLASH + dealTableName + "Controller.java");
        StringBuffer stringBuffer = new StringBuffer();
        String t = "String";
        //遍历属性
        for (TableInfo tableInfo : tableInfos) {
            //主键
            if ("PRI".equals(tableInfo.getColumnKey())) {
                t = StringUtil.typeMapping(tableInfo.getDataType());
            }
        }
        stringBuffer.append(
                "package " + package_.replaceAll(SLASH, ".") + "controller;\n" +
                        "\n" +
                        "import " + package_ + ".service.*;\n" +
                        "import " + package_ + ".model." + dealTableName + ";\n" +
                        "import " + package_ + ".vo." + dealTableName + "Vo;\n" +
                        "import " + package_ + ".service." + dealTableName + "Service;\n" +
                        "import javax.annotation.Resource;\n" +
                        "import org.springframework.web.bind.annotation.*;\n" +
                        "\n" +
                        "@RestController\n" +
                        "@RequestMapping(\"/sys/" + StringUtil.camelCaseName(tableName) + "/\")\n" +
                        "public class " + dealTableName + "Controller {"
        );
        stringBuffer.append("\n");
        stringBuffer.append("" +
                "    @Resource\n" +
                "    private " + dealTableName + "Service " + StringUtil.camelCaseName(tableName) + "Service;\n");
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);
    }

    /**
     * 获取表结构信息
     */
    protected List<TableInfo> getTableInfo() {
        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<TableInfo> list = new ArrayList<>();
        try {
            conn = dbConnectionUtil.getConnection();
            String sql = "select column_name,data_type,column_comment,column_key,extra from information_schema.columns where table_name=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tableName);
            rs = ps.executeQuery();
            while (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                //列名，全部转为小写
                tableInfo.setColumnName(rs.getString("column_name").toLowerCase());
                //列类型
                tableInfo.setDataType(rs.getString("data_type"));
                //列注释
                tableInfo.setColumnComment(rs.getString("column_comment"));
                //主键
                tableInfo.setColumnKey(rs.getString("column_key"));
                //主键类型
                tableInfo.setExtra(rs.getString("extra"));

                list.add(tableInfo);
            }
        } catch (SQLException e) {
            //输出到日志文件中
            e.printStackTrace();
        } finally {
            assert rs != null;
            DBConnectionUtil.close(conn, ps, rs);
        }
        return list;
    }








    /**
     * 快速创建，供外部调用，调用之前先设置一下项目的基础路径
     */
    protected String create(boolean vo, boolean repository, boolean service, boolean controller) {
        List<TableInfo> tableInfo = getTableInfo();


        createPojo(tableInfo);

//        if(vo)
//        {
//            createVo(tableInfo);
//        }

        if(repository)
        {
            createRepository(tableInfo);
        }

        if(service)
        {
            createService(tableInfo);
        }

        if(controller)
        {
            createController(tableInfo);
        }

        System.out.println("生成路径位置：" + basePath);
        return tableName + " 后台代码生成完毕！";
    }

    public static void main(String[] args) {
        String[] tables = {"p_pker", "p_pker_award", "p_pker_category", "p_pker_config", "p_pker_level", "p_pker_match", "p_pker_rank", "p_pker_reward", "p_pker_score", "p_pker_topic", "p_pker_user"};
        Arrays.stream(tables).forEach(t -> {
            String msg = new CodeDOM(t).create(true, true, true, true);

            System.out.println(msg);
        });

    }
}