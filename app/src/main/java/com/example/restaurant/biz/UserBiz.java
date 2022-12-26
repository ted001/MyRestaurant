package com.example.restaurant.biz;

import com.example.restaurant.bean.User;
import com.example.restaurant.config.Config;
import com.example.restaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

public class UserBiz {
    public void onDestory() {
        OkHttpUtils.getInstance().cancelTag(this);
    }

    public void register(String username, String password, CommonCallback<User> callBack) {
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "user_register")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(callBack);
    }


    public void login(String username, String password, CommonCallback<User> callBack) {

        OkHttpUtils
                .post()
                .url(Config.baseUrl + "user_login")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(callBack);
    }
}
