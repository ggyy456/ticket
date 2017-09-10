package com.mjx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class ReflectUtil {
	
	public static ReflectCache getCache(){
		
		return ReflectCache.getInstance();
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	public static void setFieldValue(Object target, String fname, Class ftype,
			Object fvalue) {
		if (target == null
				|| fname == null
				|| "".equals(fname)
				|| (fvalue != null && !ftype
						.isAssignableFrom(fvalue.getClass()))) {
			return;
		}
		Class clazz = target.getClass();
		try {
			Method method = getDeclaredMethod(
					clazz,
					new StringBuilder().append("set")
							.append(Character.toUpperCase(fname.charAt(0)))
							.append(new String(fname.substring(1))).toString(),
					ftype);
			if (!Modifier.isPublic(method.getModifiers())) {
				method.setAccessible(true);
			}
			method.invoke(target, fvalue);

		} catch (Exception me) {
			logger.debug(me.getMessage());

			try {
				Field field = getDeclaredField(clazz,fname);
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				field.set(target, fvalue);
			} catch (Exception fe) {
				logger.debug(fe.getMessage());
			}
		}
	}

	public static void invokeMethod(Object target, String fname, Class ftype,
			Object fvalue) {
		if (target == null
				|| fname == null
				|| "".equals(fname)
				|| (fvalue != null && !ftype
						.isAssignableFrom(fvalue.getClass()))) {
			return;
		}
		Class clazz = target.getClass();
		try {

			Method method = getDeclaredMethod(
					clazz,
					new StringBuilder().append("set")
							.append(Character.toUpperCase(fname.charAt(0)))
							.append(fname.substring(1)).toString(), ftype);
			if (!Modifier.isPublic(method.getModifiers())) {
				method.setAccessible(true);
			}
			method.invoke(target, fvalue);

		} catch (Exception me) {
			logger.debug(me.getMessage());

			try {
				Field field = getDeclaredField(clazz,fname);
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				field.set(target, fvalue);
			} catch (Exception fe) {
				logger.debug(fe.getMessage());
			}
		}
	}

	private static Method getMethod(Class clazz, String methodName)
			throws NoSuchMethodException, SecurityException {
		Method m = null;
		try {
			m= getDeclaredMethod(clazz,methodName);
			if (m==null&& !clazz.getSuperclass().equals(Object.class)){
				
				return getMethod(clazz.getSuperclass(), methodName);
			}else{
				throw new NoSuchMethodException();
			}
		} catch (SecurityException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			 
				throw e;
		 
		}

	}

	/**
	 * 异常处理还存在问题,需要加到系统的异常处理中
	 * 
	 * @param target
	 * @param fname
	 * @return
	 */
	public static Object getFieldValue(Object target, String fname) {
		if (target == null || fname == null || "".equals(fname)) {
			return null;
		}
		Object r = null;
		Class clazz = target.getClass();

		try {
			Method method = getMethod(
					clazz,
					new StringBuilder().append("get")
							.append(Character.toUpperCase(fname.charAt(0)))
							.append(fname.substring(1)).toString());
			if (!Modifier.isPublic(method.getModifiers())) {
				method.setAccessible(true);
			}
			r = method.invoke(target);
			// 如果方法无效，试着访问其字段
		} catch (Exception me) {
			logger.debug(me.getMessage());

			try {
				Field field = getDeclaredField(clazz,fname);
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				r = field.get(target);
			} catch (Exception fe) {
				// fe.printStackTrace();
				logger.debug(fe.getMessage());
				throw new RuntimeException("找不到对应"+fname +"的字段或方法.");

			}
		}
		return r;
	}

	public static Object getFieldValue(Object target, Field field) {
		if (target == null || field == null) {
			return null;
		}
		Object r = null;

		Class clazz = target.getClass();
		try {
			Method method = getDeclaredMethod(
					clazz,
					new StringBuilder()
							.append("get")
							.append(Character.toUpperCase(field.getName()
									.charAt(0)))
							.append(field.getName().substring(1)).toString());
			if (!Modifier.isPublic(method.getModifiers())) {
				method.setAccessible(true);
			}
			r = method.invoke(target);

		} catch (Exception me) {
			logger.debug(me.getMessage());
			me.printStackTrace();
			try {
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				field.get(target);
			} catch (Exception fe) {
				logger.debug(fe.getMessage());
			}
		}
		return r;
	}

	
	/***
	 * 2011-1-24 加入方法 去的类中所有的get方法 并返回映射属性值与get方法对应的map 
	 * 排除class 属性即getClass 方法
	 * @param c 类 
	 * @return 方法Map 包括父类
	 */
	public static HashMap<String,Method> getAllValueGetMethod(Class c){
		
		return getCache().getAllValueGetMethod(c);
	}
	
	
	public static Field[] getDeclaredFields(Class c){
 
		return  getCache().getDeclaredFields(c);
	 
	}
	
	public static Method[] getDeclaredMethods(Class c){
 
		return  getCache().getDeclaredMethods(c);
 
	 
	}
	
	public static Field getDeclaredField(Class c,String name) throws SecurityException, NoSuchFieldException{
		return getCache().getDeclaredField(c,name);
		 
	}
	
	public static Method getDeclaredMethod(Class c,String name) throws SecurityException, NoSuchMethodException{
		return getCache().getDeclaredMethod(c,name);
	 
	}
	
	public static Method getDeclaredMethod(Class c,String name,Class... type) throws SecurityException, NoSuchMethodException{
		
		return  getCache().getDeclaredMethod(c,name,type);
	}
}
