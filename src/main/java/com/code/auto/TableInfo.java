package com.code.auto;

import lombok.Data;

@Data
class TableInfo {
    private String columnName;
    private String dataType;
    private String columnComment;
    private String columnKey;
    private String extra;

    TableInfo() {
    }
}
