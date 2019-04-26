package com.example.m1762.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

//实现万能接口 -- 制作方法为对象
/*

* 1，没有返回值，没有参数
* 2，没有返回值，有参数
* 3，有返回值，没有参数
* 4，有返回值，有参数
* */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FunctionManager.getInstance().invokeFunction("noparamnoresult");
//                User user = FunctionManager.getInstance().invokeFunction("noparamhasresult",User.class);
                User user = FunctionManager.getInstance().invokeFunction("hasparamhasresult",new User("hhaha","ddd"),User.class);
                Log.e("luosuihan", "SecondActivity: username = "+user.getName()+"  ,userpwd = "+user.getPwd() );
            }
        });
    }
}
