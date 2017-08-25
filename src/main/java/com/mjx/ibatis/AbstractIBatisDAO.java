package com.mjx.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import java.util.List;

/**
 * Created by Administrator on 2017-8-18.
 */
public class AbstractIBatisDAO<T> extends SqlMapClientDaoSupport implements IDAO<T>{
    protected String nameSpace;

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
}
