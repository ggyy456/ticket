package com.atomikos.tomcat;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.apache.naming.ResourceRef;
import org.apache.naming.factory.Constants;

import com.atomikos.beans.PropertyUtils;
import com.atomikos.jdbc.AtomikosDataSourceBean;

public class BeanFactory implements ObjectFactory
{

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws NamingException
    {
        if (obj instanceof ResourceRef) {
            try {
                Reference ref = (Reference) obj;
                String beanClassName = ref.getClassName();
                Class beanClass = null;
                ClassLoader tcl = Thread.currentThread().getContextClassLoader();
                if (tcl != null) {
                    try {
                        beanClass = tcl.loadClass(beanClassName);
                    } catch (ClassNotFoundException e) {
                    }
                } else {
                    try {
                        beanClass = Class.forName(beanClassName);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (beanClass == null) {
                    throw new NamingException("Class not found: " + beanClassName);
                }
                if (!AtomikosDataSourceBean.class.isAssignableFrom(beanClass)) {
                    throw new NamingException("Class is not a AtomikosDataSourceBean: " + beanClassName);
                }

                AtomikosDataSourceBean bean = (AtomikosDataSourceBean) beanClass.newInstance();

                int i = 0;
                Enumeration en = ref.getAll();
                while (en.hasMoreElements()) {
                    RefAddr ra = (RefAddr) en.nextElement();
                    String propName = ra.getType();

                    if (propName.equals(Constants.FACTORY) || propName.equals("singleton") || propName.equals("description") || propName.equals("scope") || propName.equals("auth")) {
                        continue;
                    }

                    String value = (String) ra.getContent();

                    PropertyUtils.setProperty(bean, propName, value);

                    i++;
                }

                bean.init();
                return bean;

            } catch (Exception ex) {
                throw (NamingException) new NamingException("error creating AtomikosDataSourceBean").initCause(ex);
            }

        } else {
            return null;
        }
    }
}