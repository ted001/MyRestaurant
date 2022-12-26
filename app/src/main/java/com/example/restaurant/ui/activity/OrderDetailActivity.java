package com.example.restaurant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurant.R;
import com.example.restaurant.bean.Order;
import com.example.restaurant.config.Config;
import com.example.restaurant.util.T;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailActivity extends BaseActivity {

    private Order mOrder;
    private static final String KEY_ORDER = "key_order";

    private ImageView mIvIcon;
    private TextView mTvTitle;
    private TextView mTvDesc;
    private TextView mTvPrice;


    public static void launch(Context context, Order order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(KEY_ORDER, order);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setUpToolbar();
        setTitle("订单详情");
        initIntent();
        initView();


    }

    private void initView() {
        mIvIcon = (ImageView) findViewById(R.id.id_iv_icon);
        mTvTitle = (TextView) findViewById(R.id.id_tv_title);
        mTvDesc = (TextView) findViewById(R.id.id_tv_desc);
        mTvPrice = (TextView) findViewById(R.id.id_tv_price);

        Picasso.with(this)
                .load(Config.baseUrl + mOrder.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvIcon);
        mTvTitle.setText(mOrder.getRestaurant().getName());
        List<Order.ProductVo> ps = mOrder.ps;
        StringBuffer sb = new StringBuffer();
        for (Order.ProductVo productVo : ps) {
            sb.append(productVo.product.getName())
                    .append(" * " + productVo.count)
                    .append("\n");
        }
        mTvDesc.setText(sb.toString());
        mTvPrice.setText("共消费：" + mOrder.getPrice() + "元");
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mOrder = (Order) intent.getSerializableExtra(KEY_ORDER);
        if (mOrder == null) {
            T.showToast("商品未取到");
            finish();
            return;
        }
    }
}
