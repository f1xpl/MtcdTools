package com.f1x.mtcdtools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by COMPUTER on 2017-06-19.
 */

public class PlatformChecker {
    public static boolean isPX5Platform() {
        try {
            Class localClass = Class.forName("android.os.ServiceManager");
            Method getService = localClass.getMethod("getService", new Class[] {String.class});

            if(getService != null) {
                return getService.invoke(localClass, new Object[]{"carservice"}) != null;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return false;
    }
}
