package com.werun.back.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class F2C {

    public static void father2child(Object father,Object child) throws Exception {
        if (child.getClass().getSuperclass()!=father.getClass()){
            throw new Exception("child 不是 father 的子类");
        }
        Class<?> fatherClass = father.getClass();
        Field[] declaredFields = fatherClass.getDeclaredFields();

        for(int i =0;i<declaredFields.length;i++){
            Field field=declaredFields[i];
            if (field.toString().contains("UID")) continue;
            Method method=fatherClass.getDeclaredMethod("get"+upperHeadChar(field.getName()));
            Object obj = method.invoke(father);
            field.setAccessible(true);
            field.set(child,obj);
        }
    }
    public static void father2childByHWG(Object father,Object child) throws Exception {
        if (child.getClass().getSuperclass()!=father.getClass()){
            throw new Exception("child 不是 father 的子类");
        }
        Class<?> fatherClass = father.getClass();
        Field[] declaredFields = fatherClass.getDeclaredFields();

        for(int i =0;i<declaredFields.length;i++){
            Field field=declaredFields[i];
            if (field.toString().contains("UID")) continue;
            Method method=fatherClass.getDeclaredMethod("get"+field.getName());
            Object obj = method.invoke(father);
            field.setAccessible(true);
            field.set(child,obj);
        }
    }

    public static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        String out = head.toUpperCase() + in.substring(1, in.length());
        return out;
    }

}
