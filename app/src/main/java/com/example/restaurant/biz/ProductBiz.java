package com.example.restaurant.biz;

import com.example.restaurant.bean.Product;
import com.example.restaurant.config.Config;
import com.example.restaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class ProductBiz {
    public void listByPage(int currentPage, CommonCallback<List<Product>> callback) {
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "product_find")
                .tag(this)
                .addParams("currentPage", currentPage + "")
                .build()
                .execute(callback);
    }
}
