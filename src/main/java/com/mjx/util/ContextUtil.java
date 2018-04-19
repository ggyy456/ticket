package com.mjx.util;

import java.lang.Object;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
/**
 *查询spring配置bean实例。
 *
 * @author zhangzb
 */
public class ContextUtil implements InitializingBean, ApplicationContextAware{

    private static final Logger log = LoggerFactory.getLogger(ContextUtil.class);
    private static ApplicationContext applicationContext = null;
    private static Properties classProps = null;

    public ContextUtil() {
    }

    /**
     *初始化方法，将对象做为单态属性存放起来。
     */
    public void afterPropertiesSet() throws Exception {
        if(applicationContext == null){
            log.error("ApplicationContext初始化失败");
            //throw  new ContextException(GlobalConstants.MESSAGE_CODE_COMMON_HANDLE_FAILURE, "ApplicationContext初始化");
        } else
            log.info("ApplicationContext初始化成功");
    }

    /**
     *查找方法,根据class 查找环境中的bean,返回一个bean 对象
     *
     *@exception ContextException
     */
    public static Object get(Class clz) throws RuntimeException{
        log.debug("系统使用了ContextUtil.get(Class) 方法查找Class 为 "+ clz +" 的对象.");
        Map map = null;
        try{
            map = applicationContext.getBeansOfType(clz);
        } catch (Throwable e) {
            //	throw new ContextException(GlobalConstants.MESSAGE_CODE_UTIL_CONTEXT_GET_BEAN_ERROR, clz);
            return null;
        }
        Object []arr = map.keySet().toArray();

        if(arr.length == 0){
            log.warn("ContextUtil.get(Class) 没有找到符合条件的对象.");
            return null;
        }

        // 配置中有其信息直接按配置的名字查找
        // 即使有名字也要其类型匹配
        if(classProps != null ){
            String  name = classProps.getProperty(clz.getName());
            if(name != null){
                Object o = map.get(name);
                if(o == null){
                    log.warn("ContextUtil.get(Class) 没有找到符合条件的对象.");
                    return null;
                }
                return o;
            }
        }

        if(arr.length == 1)
            return map.get(arr[0]);
        else{
            log.warn("ContextUtil.get(Class) 找到多个符合条件的对象,不能判断返回那一个.");
            return null;
        }
    }

    /**
     *查找方法,根据配置文件中的名字查找环境中的bean对象
     *
     *@exception ContextException
     */
    public static Object get(String objName) throws RuntimeException{
        log.debug("系统使用了ContextUtil.get(Name ) 方法查找名字为 "+objName +" 的对象.");

        try{
            return applicationContext.getBean(objName);
        } catch (Throwable e) {
            return null;
            //throw new ContextException(GlobalConstants.MESSAGE_CODE_UTIL_CONTEXT_GET_BEAN_ERROR, objName);
        }

    }

    /**
     *查看对象是否可以访问，内部屏蔽异常
     *
     *@exception ContextException
     */
    public static boolean peek(String objName){
        log.debug("系统使用了ContextUtil.peek(Name ) 方法查看名字为 "+objName +" 的对象.");

        try{
            return applicationContext.getBean(objName)!=null?true:false;
        } catch (Throwable e) {
            return false;
        }

    }



    /**
     *查找方法,根据配置文件中的名字查找环境中的bean对象
     */
    public static List query(Class clazz){
        log.debug("系统使用了ContextUtil.query(Class) 方法查找Class为 "+clazz +" 的对象列表.");
        Map map = null;
        try{
            map = applicationContext.getBeansOfType(clazz);
        } catch (Throwable e) {
            //throw new ContextException(GlobalConstants.MESSAGE_CODE_UTIL_CONTEXT_GET_BEAN_ERROR, clazz);
        }

        Object []keysArr = map.keySet().toArray();

        List objList = new ArrayList();

        for(int i=0; i<keysArr.length ; i++){
            objList.add(map.get(keysArr[i]));
        }

        return objList;
    }


    /**
     *  接口 ApplicationContextAware 的实现方法
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public  Properties getClassProps() {
        return classProps;
    }

    public  void setClassProps(Properties aClassProps) {
        classProps = aClassProps;
    }


}
