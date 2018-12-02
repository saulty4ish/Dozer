package com.example.a5aulty4ish.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText Name; //定义Plain Test控件第一个输入框的名字
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Name = (EditText) findViewById(R.id.TEXT_NAME); //通过findViewById找到输入框控件对应的id并给它起一个名字
        Button Login = (Button) findViewById(R.id.BTN_Login);//通过findViewById找到按钮控件对应的id并给它起一个名字
        Login.setOnClickListener(new View.OnClickListener() { //监听有没有点击按钮控件 如果点击了就会执行onClick函数
            @Override
            public void onClick(View view) {
                check(Name.getText().toString().trim()); //调用check函数
            }
        });
    }
    public void check(String name) //自定义函数check 这里用来检查用户名和密码是否是hfdcxy和1234
    {
        if(name.equals("gyc"))
        {
            Toast.makeText(MainActivity.this,"登录成功", Toast.LENGTH_SHORT).show();//弹框
        }
        else
            Toast.makeText(MainActivity.this,"登录失败", Toast.LENGTH_SHORT).show();//弹框
    }
}