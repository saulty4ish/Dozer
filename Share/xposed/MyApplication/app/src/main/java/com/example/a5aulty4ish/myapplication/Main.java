package com.example.a5aulty4ish.myapplication;

import org.json.JSONObject;

import javax.xml.transform.ErrorListener;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Main implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        String packageName = lpparam.packageName;

        //限定包，可以减少管理的类
        if (Main.class.getPackage().getName().equals(packageName)) {
            XposedBridge.log("Loaded app:" + packageName);
        }else{
            return;
        }
        //第一个参数是className，表示被注入的方法所在的类
        //第二个参数是类加载器，照抄就行
        //第三个参数是被注入的方法名
        //第四五个参数是第三个参数的两个形参的类型
        //最后一个参数是匿名内部类
        XposedHelpers.findAndHookMethod("com.example.a5aulty4ish.myapplication.MainActivity", lpparam.classLoader, "check", String.class, new XC_MethodHook() {
            /**
             * 该方法在checkLogin方法调用之前被调用，我们输出一些日志，并且捕获参数的值。
             * 最后两行的目的是改变参数的值。也就是说无论参数是什么值，都会被替换为123
             * @param param
             * @throws Throwable
             */
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("开始劫持~");
                XposedBridge.log("param1="+param.args[0]);
                param.args[0] = "gyc";
            }

            /**
             * 该方法在checkLogin方法调用之后被调用
             * @param param
             * @throws Throwable
             */
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("结束劫持~");
                XposedBridge.log("param1="+param.args[0]);
            }
        });
    }


}
