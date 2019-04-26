package com.example.m1762.myapplication;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class FunctionManager {
    private static FunctionManager instance;
    //没有参数没有返回值
    private Map<String,FunctionNoParamNoResult> mNoParamNoResult;
    //没有参数有返回值
    private Map<String,FunctionNoParamHasResult> mNoParamHasResult;
    //有参数没有返回值
    private Map<String,FunctionHasParamNoResult> mHasParamNoResult;
    //有参数有返回值
    private Map<String,FunctionHasParamHasResult> mHasParamHasResult;

    private FunctionManager(){
        mNoParamNoResult = new HashMap<>();
        mNoParamHasResult = new HashMap<>();
        mHasParamNoResult = new HashMap<>();
        mHasParamHasResult = new HashMap<>();
    }
    public static FunctionManager getInstance(){
        if(instance == null){
            instance = new FunctionManager();
        }
        return instance;
    }
    //将存储的方法进行添加
    //1.添加没有参数，没有返回值的
    public void addFunction(FunctionNoParamNoResult function){
        mNoParamNoResult.put(function.funtionName,function);
    }
    public void invokeFunction(String functionName){
        if(TextUtils.isEmpty(functionName)){
            return;
        }
        if(mNoParamNoResult != null){
            FunctionNoParamNoResult f = mNoParamNoResult.get(functionName);
            if(f != null){
                f.funtion();
            }else{
                Log.e("luosuihan", "没有找到该方法: ");
            }
        }
    }
    //2，添加有返回值，没有参数
    public void addFunction(FunctionNoParamHasResult function){
        mNoParamHasResult.put(function.funtionName,function);
    }
    public <T> T invokeFunction(String functionName,Class<T> t){
        if(TextUtils.isEmpty(functionName)){
            return null;
        }
        if (mNoParamHasResult != null){
            FunctionNoParamHasResult f = mNoParamHasResult.get(functionName);
            if(f != null){
                if(t != null){
                    return t.cast(f.funtion());
                }
            }
        }else {
            Log.e("luosuihan", "没有找到该方法:1 ");
        }
        return null;
    }
    //3,有参数，没有返回值
    public void  addFunction(FunctionHasParamNoResult function){
        mHasParamNoResult.put(function.funtionName,function);
    }
    public void invokeFunction(String functionName,String param){
        if(TextUtils.isEmpty(functionName)){
            return ;
        }
        if(mHasParamNoResult != null){
            FunctionHasParamNoResult f = mHasParamNoResult.get(functionName);
            if (f != null){
                f.function(param);
            }
        }else {
            Log.e("luosuihan", "没有找到该方法: ");
        }
    }
    //有参数，有返回值
    public void addFunction(FunctionHasParamHasResult function){
        mHasParamHasResult.put(function.funtionName,function);
    }
    public <T,P> T invokeFunction(String functionName,P p,Class<T> t){
        if(TextUtils.isEmpty(functionName)){
            return null;
        }
        if(mHasParamHasResult != null){
            FunctionHasParamHasResult f = mHasParamHasResult.get(functionName);
            if(f != null){
                if(t != null){
                    return t.cast(f.function(p));
                }
            }
        }
        return null;
    }
}
