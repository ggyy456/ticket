package com.mjx.ibatis;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.mjx.util.ReflectUtil;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017-8-18.
 */
public class AbstractIBatisDAO<T> extends SqlMapClientDaoSupport implements IDAO<T>{
    protected String nameSpace;
    private SqlExecutor sqlExecutor;

    protected void init() {
        if(this.sqlExecutor != null) {
            SqlMapClient sqlMapClient = this.getSqlMapClientTemplate().getSqlMapClient();
            if(sqlMapClient instanceof SqlMapClientImpl) {
                SqlMapClientImpl sqlMapClientImpl = (SqlMapClientImpl)sqlMapClient;
                ReflectUtil.setFieldValue(sqlMapClientImpl.delegate, "sqlExecutor", SqlExecutor.class, this.sqlExecutor);
            } else {
                throw new RuntimeException("sqlMapClient必须是SqlMapClientImpl类型");
            }
        }
    }

    @Override
    public IPaging getPage(IPaging pagingObj) {
        String sql = this.getStatementID("getPage");
        return this.getPage(pagingObj, sql);
    }

    protected IPaging getPage(IPaging pagingObj, String statementID) {
        String sql = statementID;
        String totalsql = statementID + "Total";

        try {
            logger.debug("ibatis queryPage statment id :" + totalsql + " : " + sql);
            if(pagingObj == null) {
                throw new RuntimeException("T不能为空");
            } else {
                int startIndex = 0;//初始偏移量
                int iperpagesize = 0;//初始每页行数变量
                int imax = 0;//初始总行数变量
                int imaxCount = 0;// 初始总页数变量
                Collection datas = null;//分页数据

                startIndex = pagingObj.getStartIndex();
                iperpagesize = pagingObj.getSizePerPage();
                iperpagesize = iperpagesize < 1?0:iperpagesize;
                startIndex = startIndex < 0?0:startIndex;
                startIndex = iperpagesize == 0 ? startIndex = 0 : startIndex;
                imax = (Integer) this.getSqlMapClientTemplate().queryForObject(totalsql , pagingObj);
                logger.debug(totalsql + "执行结果为：" + imax);


                if (imax != 0) {//判断总行数   如果大于0   表示有此次查询有数据
                    if (iperpagesize != 0) {// 如果每页行数 不等于0  则计算总页数
                        imaxCount = imax % iperpagesize > 0 ? (imax / iperpagesize + 1)
                                : (imax / iperpagesize);
                    } else {// 如果每页行数 等于0  则总页数等于1
                        imaxCount = 1;
                        iperpagesize=imax;
                    }

                    //当前页如果大于总页数则当前页等于总页数
                    startIndex = startIndex > imax ? startIndex-iperpagesize : startIndex;
                    datas = getSqlMapClientTemplate().queryForList(sql, pagingObj, startIndex, iperpagesize);
                    if (datas == null || datas.size() == 0) {
                        throw new RuntimeException("数据读取错误");
                    }

                }else{//判断总行数   如果等于0   表示此次查询无数据
                    startIndex=0;
                    datas=new ArrayList();
                }

                pagingObj.setSizePerPage(iperpagesize);
                pagingObj.setTotalSize(imax);
                pagingObj.setData(datas);
                pagingObj.setStartIndex(startIndex);
                pagingObj.computePage();

                return pagingObj;
            }
        } catch (Throwable var10) {
            throw var10;
        }
    }

    @Override
    public T getEntity(Object obj) throws RuntimeException {
        return (T)this.getSqlMapClientTemplate().queryForObject(this.getStatementID("getEntity"), obj);
    }

    @Override
    public List<T> getAll() throws RuntimeException {
        return this.getSqlMapClientTemplate().queryForList(this.getStatementID("getAll"));
    }

    @Override
    public List<T> getList(Object obj) throws RuntimeException {
        return this.getSqlMapClientTemplate().queryForList(this.getStatementID("getList"), obj);
    }

    @Override
    public Object save(T t) throws RuntimeException {
        return this.getSqlMapClientTemplate().insert(this.getStatementID("save"), t);
    }

    @Override
    public int update(T t) throws RuntimeException {
        return this.getSqlMapClientTemplate().update(this.getStatementID("update"), t);
    }

    @Override
    public int remove(Object obj) throws RuntimeException {
        return this.getSqlMapClientTemplate().delete(this.getStatementID("remove"), obj);
    }

    @Override
    public int removeAll() throws RuntimeException {
        return this.getSqlMapClientTemplate().delete(this.getStatementID("removeAll"));
    }

    @Override
    public Object execute(Object... obj) throws RuntimeException{
        if(obj.length < 1) {
            throw new RuntimeException("必须传入功能标识");
        }

        Object result = null;
        String methodName = (String)obj[0];
        Object param = obj.length > 1?obj[1]:null;

        if(methodName.indexOf("save") > -1){
            result = this.getSqlMapClientTemplate().insert(this.getStatementID(methodName), param);
        }
        else if(methodName.indexOf("update") > -1){
            result = this.getSqlMapClientTemplate().update(this.getStatementID(methodName), param);
        }
        else if(methodName.indexOf("remove") > -1){
            result = this.getSqlMapClientTemplate().delete(this.getStatementID(methodName), param);
        }
        else if(methodName.indexOf("getEntity") > -1){
            result = this.getSqlMapClientTemplate().queryForObject(this.getStatementID(methodName), param);
        }
        else if(methodName.indexOf("getList") > -1){
            result = this.getSqlMapClientTemplate().queryForList(this.getStatementID(methodName), param);
        }
        else if(methodName.indexOf("getPage") > -1) {
            result = getPage((IPaging)param, this.getStatementID(methodName));
        }

        return result;
    }

    public String getNameSpace() {
        return this.nameSpace == null?"":this.nameSpace + ".";
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    protected String getStatementID(String statementID) {
        statementID = this.getNameSpace() + statementID;
        return statementID;
    }

    public SqlExecutor getSqlExecutor() {
        return sqlExecutor;
    }

    public void setSqlExecutor(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    public int handleBatch(final BatchInfo batchInfo) throws RuntimeException {
        try {
            this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
                public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                    executor.startBatch();
                    Iterator var3 = batchInfo.getBatchItemList().iterator();

                    while(var3.hasNext()) {
                        BatchItem bi = (BatchItem)var3.next();
                        switch(batchInfo.getType()) {
                            case 1:
                                executor.insert(bi.getStatement(), bi.getObj());
                                break;
                            case 2:
                                executor.update(bi.getStatement(), bi.getObj());
                                break;
                            default:
                                executor.delete(bi.getStatement(), bi.getObj());
                        }
                    }

                    executor.executeBatch();
                    return null;
                }
            });
            return 0;
        } catch (Throwable var3) {
            throw new RuntimeException("批量处理错误");
        }
    }

}
