package com.mjx.ibatis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/1.
 */
public class BatchInfo {
    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    private int type;
    private List<BatchItem> batchItemList;

    public BatchInfo(int type) {
        this(type, 10);
    }

    public BatchInfo(int type, int size) {
        this.type = type;
        this.batchItemList = new ArrayList(size);
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void addBatch(String statement, Object paraObj) {
        this.batchItemList.add(new BatchItem(statement, paraObj));
    }

    public List<BatchItem> getBatchItemList() {
        return this.batchItemList;
    }

    public void setBatchItemList(List<BatchItem> batchItemList) {
        this.batchItemList = batchItemList;
    }
}
