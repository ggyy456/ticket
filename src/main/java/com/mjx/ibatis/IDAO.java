package com.mjx.ibatis;

import java.util.List;

/**
 * Created by Administrator on 2017-8-18.
 * 暂时先抛出RuntimeException，以后改为自己定义的异常体系
 */
public interface IDAO<T> {

    T getEntity(Object obj) throws RuntimeException;

    List<T> getAll() throws RuntimeException;

    List<T> getList(Object obj) throws RuntimeException;

    Object save(T t) throws RuntimeException;

    int update(T t) throws RuntimeException;

    int remove(Object obj) throws RuntimeException;

    int removeAll() throws RuntimeException;

    Object execute(Object... obj) throws RuntimeException;

    IPaging<T> getPage(IPaging<T> var1) throws RuntimeException;

    int handleBatch(BatchInfo var1) throws RuntimeException;
}
