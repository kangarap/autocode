package com.code.auto;

/**
 * 字符串处理工具类
 */
class StringUtil {
    /**
     * 数据库类型->JAVA类型
     *
     * @param dbType 数据库类型
     * @return JAVA类型
     */
    protected static String typeMapping(String dbType) {
        String javaType;
        if ("int|integer|mediumint".contains(dbType)) {
            javaType = "long";
        } else if ("float|double|decimal|real".contains(dbType)) {
            javaType = "Double";
        } else if ("date|time|datetime|timestamp".contains(dbType)) {
            javaType = "Date";
        } else if("tinyint".contains(dbType)){
            javaType = "byte";
        }else if ("smallint".contains(dbType)) {
            javaType = "short";
        } else
        {
            javaType = "String";
        }
        return javaType;
    }

    /**
     * 驼峰转换为下划线
     */
    protected static String underscoreName(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 首字母大写
     */
    protected static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);

    }

    /**
     * 下划线转换为驼峰
     */
    protected static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }
}
