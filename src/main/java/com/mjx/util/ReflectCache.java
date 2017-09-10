package com.mjx.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class ReflectCache {
	
	private static ReflectCache reflectCache =new ReflectCache();
	
	private  ConcurrentHashMap <Class,Field[]> classFieldArrayMap ;
	
	private  ConcurrentHashMap <Class,Method[]> classMethodArrayMap ;
	
	private  ConcurrentHashMap <Class,HashMap<String,Method>> classAllValueGetMethod;
 
	private  ConcurrentHashMap <Class,HashMap<String,Method>> classAllValueSetMethod;
	
	public static ReflectCache getInstance(){
		
		return reflectCache;
	}
	private ReflectCache(){
		classFieldArrayMap= new ConcurrentHashMap <Class,Field[]>();
		classMethodArrayMap= new ConcurrentHashMap <Class,Method[]>();
		classAllValueGetMethod= new ConcurrentHashMap <Class,HashMap<String,Method>>();
		classAllValueSetMethod= new ConcurrentHashMap <Class,HashMap<String,Method>>();
	}
	
	
	public Field[] getDeclaredFields(Class c){
		Field[] field=classFieldArrayMap.get(c);
		if (field==null){
			loadClassFieldArray(c);
		}
		return classFieldArrayMap.get(c);
		
	}
	
	public Field getDeclaredField(Class c,String name){
		Field[] f=getDeclaredFields(c);
		if (f!=null){
			for(Field af : f){
				if (af.getName().equals(name)){
					return af;
				}
			}
		}
		return null;
		
	}
	
	
	public Method[] getDeclaredMethods(Class c){
		Method[] Method=classMethodArrayMap.get(c);
		if (Method==null){
			loadClassMethodArray(c);
		}
		return classMethodArrayMap.get(c);
		
	}
	
	public Method getDeclaredMethod(Class c,String name){
		Method[] m=getDeclaredMethods(c);
		if (m!=null){
			for(Method am : m){
				if (am.getName().equals(name)){
					return am;
				}
			}
		}
		return null;
		
	}
	public Method getDeclaredMethod(Class c,String name,Class...type){
		Method[] m=getDeclaredMethods(c);
		Class[] paraClass =null;
		if (m!=null){
			for(Method am : m){
				if (am.getName().equals(name)){
					 paraClass = am.getParameterTypes();
					
					if (paraClass!=null && type ==null){
						 continue;
					}
					if (paraClass==null && type !=null){
						 continue;
					}
					if (paraClass.length!=type.length){
						 continue;
					}
					if (compareClassArr(type,paraClass)){
						paraClass=null;
						return am;
					}
					
				}
				paraClass=null;
			}
		}
		return null;
		
	}
	
	public boolean compareClassArr(Class[] a ,Class[] b){
		for (int i = 0;i<a.length;i++){
			if (!a[i].equals(b[i])){
				return false;
			}
			
		}
		return true;
	}
	
	public HashMap<String,Method> getAllValueGetMethod(Class c){
		
		HashMap<String,Method> methodMap =null;
		
		if (classAllValueGetMethod.containsKey(c)){
			methodMap=	classAllValueGetMethod.get(c);
		}else{
			methodMap=new HashMap<String,Method>();
			String methodName=null;
			String fieldName=null;
			Method[] methods= c.getMethods();
			 
			 for(Method m : methods){
				 
				 methodName=m.getName();
				
				 if ("get".equals(new String(methodName.substring(0,3)))&&m.getParameterTypes().length==0){
				
				 
					fieldName = new String(methodName.substring(3,
							methodName.length()));
					fieldName = new StringBuilder()
							.append(new String(fieldName.substring(0, 1))
									.toLowerCase())
							.append(new String(fieldName.substring(1,
									fieldName.length()))).toString();
					 
					 //System.out.println(m.getName());
					 if ("class".equals(fieldName)){
						 continue;
					 }
					 methodMap.put(fieldName, m);
				 
				 }
				 
				 fieldName=null;
				 methodName=null;
			 }
			 
			 methods =null;
			 classAllValueGetMethod.put(c, methodMap);
		}
		
		
		 return methodMap;
	}
	
	
	protected void loadClassFieldArray(Class c){
		classFieldArrayMap.put(c, c.getDeclaredFields());
		
	}
	
	
	
	protected void loadClassMethodArray(Class c){
		classMethodArrayMap.put(c, c.getDeclaredMethods());
		
	}
}
