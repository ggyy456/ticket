package com.mjx.ibatis;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017-8-18.
 */
public interface IDAO<T> {

    T getEntity(Object obj) throws SQLException;

    List<T> getAll() throws SQLException;

    List<T> getList(Object obj) throws SQLException;

    Object save(T t) throws SQLException;

    int update(T t) throws SQLException;

    int remove(Object obj) throws SQLException;

    int removeAll() throws SQLException;

    Object execute(Object... obj) throws SQLException;
}
