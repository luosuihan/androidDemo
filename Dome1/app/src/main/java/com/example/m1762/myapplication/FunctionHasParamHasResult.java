package com.example.m1762.myapplication;

public abstract class FunctionHasParamHasResult<T,P> extends Function {
    public FunctionHasParamHasResult(String funtionName) {
        super(funtionName);
    }
    public abstract T function(P p);
}
