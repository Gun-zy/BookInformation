package com.qcuncle.bookinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private TextView tv_register;
    private EditText et_username,et_pwd;
    private CheckBox save_pwd;
    private String userName,passWord,spPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init() {
        et_username = (EditText) findViewById(R.id.username);
        et_pwd = (EditText) findViewById(R.id.pwd);
        save_pwd = (CheckBox) findViewById(R.id.save_pwd);
        login = (Button)findViewById(R.id.loginBtn);
        tv_register = (TextView) findViewById(R.id.register);
        //获取记住的账号密码
        getUserInfo();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始登录
                getEditString();
                //对当前用户输入的密码进行MD5加密再进行比对判断, MD5Utils.md5( ); psw 进行加密判断是否一致
                String md5Psw= MD5Utils.md5(passWord);
                // md5Psw ; spPsw 为 根据从SharedPreferences中用户名读取密码
                // 定义方法 readPsw为了读取用户名，得到密码
                spPsw = readPsw(userName);
                // TextUtils.isEmpty
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(passWord)){
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                    // md5Psw.equals(); 判断，输入的密码加密后，是否与保存在SharedPreferences中一致
                }else if(md5Psw.equals(spPsw)){
                    //一致登录成功
                    Toast.makeText(MainActivity.this, "welcome！"+ userName, Toast.LENGTH_SHORT).show();
                    //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名
                    saveLoginInfo(userName,passWord);
                    //getUserInfo();
                    saveLoginStatus(true, userName);
                    //登录成功后关闭此页面进入主页
                    Intent data = new Intent();
                    //data.putExtra( ); name , value ;
                    data.putExtra("isLogin",true);
                    //RESULT_OK为Activity系统常量，状态码为-1
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    setResult(RESULT_OK,data);
                    //销毁登录界面
                    MainActivity.this.finish();
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
                    startActivity(new Intent(MainActivity.this, BookInformationActivity.class));
                    return;
                }else if((spPsw!=null&&!TextUtils.isEmpty(spPsw)&&!md5Psw.equals(spPsw))){
                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(MainActivity.this, "此用户名不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到注册界面，并实现注册功能
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

    }
    private void getEditString(){
        userName = et_username.getText().toString().trim();
        passWord = et_pwd.getText().toString().trim();
    }
    public void saveLoginInfo(String userName, String passWord){
        //获取SharedPreferences对象
        boolean CheckBoxLogin = save_pwd.isChecked();
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        if (CheckBoxLogin){
            //设置参数
            editor.putString("username", userName);
            editor.putString("password", passWord);
            editor.putBoolean("checkboxBoolean",true);
            //提交
            editor.commit();
        }else {
            editor.putString("username", null);
            editor.putString("password", null);
            editor.putBoolean("checkboxBoolean", false);
            editor.commit();
        }
    }

    private String readPsw(String userName){
        //getSharedPreferences("loginInfo",MODE_PRIVATE);
        //"loginInfo",mode_private; MODE_PRIVATE表示可以继续写入
        SharedPreferences sp  = getSharedPreferences("loginInfo", MODE_PRIVATE);
        //sp.getString() userName, "";
        return sp.getString(userName , "");
    }
    /**
     *保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String userName){
        //saveLoginStatus(true, userName);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor = sp.edit();
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status);
        //存入登录状态时的用户名
        editor.putString("loginUserName", userName);
        //提交修改
        editor.commit();
    }
    /**
     * 注册成功的数据返回至此
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */
    @Override
    //显示数据， onActivityResult
    //startActivityForResult(intent, 1); 从注册界面中获取数据
    //int requestCode , int resultCode , Intent data
    // LoginActivity -> startActivityForResult -> onActivityResult();
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //是获取注册界面回传过来的用户名
            // getExtra().getString("***");
            String userName=data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                //设置用户名到 et_user_name 控件
                et_username.setText(userName);
                //et_user_name控件的setSelection()方法来设置光标位置
                et_username.setSelection(userName.length());
            }
        }
    }
    public  void getUserInfo(){
        SharedPreferences sp = null;
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sp.getBoolean("checkboxBoolean", false))
        {
            et_username.setText(sp.getString("username", null));
            et_pwd.setText(sp.getString("password", null));
            save_pwd.setChecked(true);
        }else{
            et_username.setText(sp.getString("username", userName));
            et_pwd.setText(sp.getString("password", passWord));
            save_pwd.setChecked(false);
        }
    }
    public void onBackPressed() {
        MainActivity.this.finish();
    }
}
