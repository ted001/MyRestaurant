package com.example.restaurant.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.restaurant.R;

public class RegisterActivity extends BaseActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtRePassword;
    private Button mBtnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpToolbar();
        setTitle("手机注册");
        initView();
        initEvent();

    }

    private void initEvent() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

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
