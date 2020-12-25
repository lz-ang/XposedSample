package com.ang.xposedsample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedInit implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("Hook已经成功了");

        if (!lpparam.packageName.equals("com.ang.target")) {
            return;
        }

        if (lpparam.packageName.equals("com.ang.target")) {
            XposedHelpers.findAndHookMethod("com.ang.target.MainActivity",
                    lpparam.classLoader, "onCreate",  Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            //不能通过Class.forName()来获取Class ，在跨应用时会失效
                            Class c = lpparam.classLoader.loadClass("com.ang.target.MainActivity");
                            Field field = c.getDeclaredField("tv_hello");
                            field.setAccessible(true);
                            //param.thisObject 为执行该方法的对象，在这里指MainActivity
                            TextView textView = (TextView) field.get(param.thisObject);
                            textView.setText("Hello Xposed");
                        }
                    });
        }
    }
}
