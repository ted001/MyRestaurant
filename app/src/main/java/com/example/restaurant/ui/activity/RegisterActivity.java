package com.example.restaurant.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.restaurant.R;
import com.example.restaurant.bean.User;
import com.example.restaurant.biz.UserBiz;
import com.example.restaurant.net.CommonCallback;
import com.example.restaurant.util.T;

public class RegisterActivity extends BaseActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtRePassword;
    private Button mBtnRegister;

    private UserBiz mUserBiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpToolbar();
        setTitle("手机注册");
        initView();
        initEvent();
        mUserBiz = new UserBiz();

    }

    private void initEvent() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                String repassword = mEtRePassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    T.showToast("账号或者密码不能为空");
                    return;
                }

                if (!password.equals(repassword)) {
                    T.showToast("两次输入密码不一致");
                    return;
                }

                startLoadingProgress();

                mUserBiz.register(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        T.showToast(e.getMessage());
                        Log.d("ee", e.getMessage());
                        stopLoadingProgress();
                    }

                    @Override
                    public void onSuccess(User user) {
                        stopLoadingProgress();
                        T.showToast("注册成功，用户名为：" + user.getUsername());
                        Log.d("eeeeeeee", "onSuccess: "+ user.getUsername());
                        LoginActivity.launch(RegisterActivity.this,
                                user.getUsername(), user.getPassword());
                        finish();
                    }
                });
            }
        });
    }

    private void initView() {
        mEtUsername = (EditText) findViewById(R.id.id_et_username);
        mEtPassword = (EditText) findViewById(R.id.id_et_password);
        mEtRePassword = (EditText) findViewById(R.id.id_et_repassword);
        mBtnRegister = (Button) findViewById(R.id.id_btn_register);
    }


}
