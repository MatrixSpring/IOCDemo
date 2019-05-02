package com.dawn.libioc.iocutils;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.dawn.libioc.annotation.BindView;
import com.dawn.libioc.annotation.SetContentView;
import com.dawn.libioc.annotation.EventBase;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtils {
    private static final String SetContentView = "setContentView";
    private static final String FindViewById = "findViewById";
    private static final String SetOnClickListener = "setOnClickListener";

    public static void inject(Activity activity) {
        setContentView(activity);
        setViewById(activity);
        setOnClick(activity);
    }

    public static void setContentView(Activity activity) {
        Class<? extends Activity> tClass = activity.getClass();
        SetContentView annotations = tClass.getAnnotation(SetContentView.class);
        int layoutId = annotations.value();
        try {
            Method method = tClass.getMethod(SetContentView, int.class);
            method.invoke(activity, layoutId);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void setViewById(Activity activity) {
        Class<? extends Activity> tClass = activity.getClass();
        Field[] fields = tClass.getDeclaredFields();
        try {
            for (Field field : fields) {
                BindView bindView = field.getAnnotation(BindView.class);
                int viewId = bindView.value();
                Method method = tClass.getMethod(FindViewById, int.class);

                Object view = method.invoke(activity, viewId);
                field.setAccessible(true);
                field.set(activity, view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void setOnClick(Activity activity) {
        Class<? extends Activity> tClass = activity.getClass();
        Method[] methods = tClass.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                //获取EventBase这个注解
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (null != annotationType) {
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (null != eventBase) {
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listenerType = eventBase.listennerType();
                        String callBackMethod = eventBase.callBackMethod();

                        //获取需要注入事件的所有控件
                        try {
                            Method valueMethod = annotationType.getDeclaredMethod("value");

                            int[] viewIds = (int[]) valueMethod.invoke(annotation, null);

                            ListenerInvocationHandler invocationHandler = new ListenerInvocationHandler(activity);
                            invocationHandler.addMethod(callBackMethod, method);

                            //获取监听代理对象
                            Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, invocationHandler);

                            //给控件一一赋值
                            for(int viewId: viewIds){
                                View view = activity.findViewById(viewId);
                                Method methodSetting = view.getClass().getMethod(listenerSetter, listenerType);
                                methodSetting.invoke(view, listener);
                            }

                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
