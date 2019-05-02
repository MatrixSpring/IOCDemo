package com.dawn.libioc.iocutils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ListenerInvocationHandler implements InvocationHandler {

    //目标对象
    private Object target;

    //拦截的方法列表
    private HashMap<String, Method> methodHashMap = new HashMap<>();


    public ListenerInvocationHandler(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if(target != null){
            String methodName = method.getName();
            method = methodHashMap.get(methodName);
            method.setAccessible(true);
            if(method != null){
                return method.invoke(target, objects);
            }

        }
        return null;
    }

    //添加需要拦截的方法
    public void addMethod(String key, Method method){
        methodHashMap.put(key, method);
    }

}
