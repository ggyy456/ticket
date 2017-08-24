package com.mjx.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

/**
 * Created by Administrator on 2017/8/6 0006.
 */
public class MyInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyInterceptor.class);
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public Object doAround(ProceedingJoinPoint pJoinPoint) throws Throwable {
        Object result = null;
        boolean flag = FLAG_HOLDER.get();
        if (!flag){
            FLAG_HOLDER.set(true);
            LOGGER.error("方法 : " + pJoinPoint.getTarget().getClass() + "." + pJoinPoint.getSignature().getName());
            try {
                result = pJoinPoint.proceed();
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("出现异常");
            } finally {
                LOGGER.error("退出");
                FLAG_HOLDER.remove();
            }
        }

        return result;
    }

}
