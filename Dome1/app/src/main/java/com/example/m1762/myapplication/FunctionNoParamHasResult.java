package com.example.m1762.myapplication;

public abstract class FunctionNoParamHasResult<T> extends Function {
    public FunctionNoParamHasResult(String funtionName) {
        super(funtionName);
    }
    public abstract T funtion();
}
