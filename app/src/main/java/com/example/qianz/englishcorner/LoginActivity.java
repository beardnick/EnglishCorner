package com.example.qianz.englishcorner;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.qianz.englishcorner.model.Author;
import com.example.qianz.englishcorner.util.MessageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import shem.com.materiallogin.DefaultLoginView;
import shem.com.materiallogin.DefaultRegisterView;
import shem.com.materiallogin.MaterialLoginView;

public class LoginActivity extends AppCompatActivity {

    MaterialLoginView loginView;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initBmob();
        onBindView();
        oninitEvent();
    }

    public void initBmob(){
        if(getApplicationInfo().packageName.equals(getProcessName())){
            BmobIM.init(LoginActivity.this);
            BmobIM.registerDefaultMessageHandler(new MessageHandler());
        }
    }

    public static String getProcessName(){
        File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String processName = reader.readLine().trim();
            reader.close();
            return processName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onBindView(){
        BmobUser user = BmobUser.getCurrentUser();
        if (user != null) {
           Intent intent = new Intent(LoginActivity.this , FriendsActivity.class);
           startActivity(intent);
           finish();
        }
        loginView = (MaterialLoginView) findViewById(R.id.login);
    }


    public void oninitEvent(){
        ((DefaultLoginView)loginView.getLoginView()).setListener(new DefaultLoginView.DefaultLoginViewListener() {
            @Override
            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
                if (stringUtil(loginUser).length() == 0 || stringUtil(loginPass).length() == 0) {
                    StringBuilder sb = new StringBuilder();
                    if (stringUtil(loginUser).length() == 0) {
                        sb.append("please enter your name");
                    }
                    if (stringUtil(loginPass).length() == 0) {
                        if (sb.toString().length() > 0) {
                            sb.append(",");
                        }
                        sb.append("please enter your password");
                    }
                    Toast.makeText(LoginActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    BmobUser user = new BmobUser();
                    user.setPassword(stringUtil(loginPass));
                    user.setUsername(stringUtil(loginUser));
                    user.login(new SaveListener<Author>() {
                        @Override
                        public void done(Author author, BmobException e) {
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "login successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this , FriendsActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.i(TAG, "done: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });

        ((DefaultRegisterView)loginView.getRegisterView()).setListener(new DefaultRegisterView.DefaultRegisterViewListener() {
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                if(stringUtil(registerPass).equals(stringUtil(registerPassRep))){
                    BmobUser user = new BmobUser();
                    user.setUsername(stringUtil(registerUser));
                    user.setPassword(stringUtil(registerPass));
                    user.signUp(new SaveListener<Author>() {

                        @Override
                        public void done(Author author, BmobException e) {
                            if(e == null){
                                Toast.makeText(LoginActivity.this, "register successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this , FriendsActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Log.i(TAG, "done: " + e.getMessage());
                            }
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this, "two passwords are different", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "pass : " + stringUtil(registerPass) + "\npassrep : " + stringUtil(registerPassRep));
                }

            }
        });
    }

    public String stringUtil(TextInputLayout input){
        return String.valueOf(input.getEditText().getText());
    }
}
