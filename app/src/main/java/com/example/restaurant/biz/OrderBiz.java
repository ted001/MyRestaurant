package com.example.restaurant.biz;

import com.example.restaurant.bean.Order;
import com.example.restaurant.bean.Product;
import com.example.restaurant.config.Config;
import com.example.restaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

public class OrderBiz {

    public void onDestory() {
        OkHttpUtils.getInstance().cancelTag(this);
    }

    public void listByPage(int currentPage, CommonCallback<List<Order>> callback) {
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "order_find")
                .tag(this)
                .addParams("currentPage", currentPage + "")
                .build()
                .execute(callback);
    }


    public void add(Order order, CommonCallback callback) {
        Map<Product, Integer> productsMap = order.productsMap;
        StringBuilder sb = new StringBuilder();
        for (Product p : productsMap.keySet()) {
            sb.append(p.getId() + "_" + productsMap.get(p));
            sb.append("|");
        }
        sb = sb.deleteCharAt(sb.length() - 1);

        OkHttpUtils
                .post()
                .url(Config.baseUrl + "order_add")
                .addParams("res_id", order.getRestaurant().getId() + "")
                .addParams("product_str", sb.toString())
                .addParams("count", order.getCount() + "")
                .addParams("price", order.getPrice() + "")
                .tag(this)
                .build()
                .execute(callback);
    }

}
