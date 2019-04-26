package com.example.m1762.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//实现万能接口 -- 制作方法为对象
/*

* 1，没有返回值，没有参数
* 2，没有返回值，有参数
* 3，有返回值，没有参数
* 4，有返回值，有参数
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionManager.getInstance().addFunction(new FunctionNoParamNoResult("noparamnoresult") {
            @Override
            public void funtion() {
                Log.e("luosuihan", "send activity");
            }
        });
        FunctionManager.getInstance().addFunction(new FunctionNoParamHasResult<User>("noparamhasresult") {
            @Override
            public User funtion() {
                User user = new User("admin","123456");
                return user;
            }
        });
        FunctionManager.getInstance().addFunction(new FunctionHasParamNoResult<String>("hasparamnoresult") {
            @Override
            public void function(String o) {
//                TODO
            }
        });
        //第一个user表示返回类型，第二个user表示参数
        FunctionManager.getInstance().addFunction(new FunctionHasParamHasResult<User,User>("hasparamhasresult") {
            @Override
            public User function(User o) {
                Log.e("luosuihan", "打印传递过来的参数: "+o.getName()+","+o.getPwd() );
                User user = new User("admin","123456");
                return user;
            }
        });
        Intent intent = new Intent();
        intent.setClass(this,SecondActivity.class);
        startActivity(intent);
    }
}
