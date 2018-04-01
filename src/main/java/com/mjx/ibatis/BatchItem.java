package com.mjx.ibatis;

/**
 * Created by Administrator on 2018/4/1.
 */
public class BatchItem {
    private String statement;
    private Object obj;

    public BatchItem(String statement, Object t) {
        this.statement = statement;
        this.obj = t;
    }

    public Object getObj() {
        return this.obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getStatement() {
        return this.statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}
