package com.example.m1762.myapplication;

public abstract class FunctionHasParamNoResult<P> extends Function {
    public FunctionHasParamNoResult(String funtionName) {
        super(funtionName);
    }
    public abstract void function(P p);
}
