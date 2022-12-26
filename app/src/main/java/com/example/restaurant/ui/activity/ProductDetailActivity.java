package com.example.restaurant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurant.R;
import com.example.restaurant.bean.Product;
import com.example.restaurant.config.Config;
import com.example.restaurant.util.T;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends BaseActivity {

    private Product mProduct;
    private static final String KEY_PRODUCT = "key_product";

    private ImageView mIvIcon;
    private TextView mTvTitle;
    private TextView mTvDesc;
    private TextView mTvPrice;


    public static void launch(Context context, Product product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT, product);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setUpToolbar();
        setTitle("详情");
        initIntent();
        initView();

    }

    private void initView() {
        mIvIcon = (ImageView) findViewById(R.id.id_iv_icon);
        mTvTitle = (TextView) findViewById(R.id.id_tv_title);
        mTvDesc = (TextView) findViewById(R.id.id_tv_desc);
        mTvPrice = (TextView) findViewById(R.id.id_tv_price);

        Picasso.with(this)
                .load(Config.baseUrl + mProduct.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvIcon);
        mTvTitle.setText(mProduct.getName());
        mTvDesc.setText(mProduct.getDescription());
        mTvPrice.setText(mProduct.getPrice() + "元/份");
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mProduct = (Product) intent.getSerializableExtra(KEY_PRODUCT);
        if (mProduct == null) {
            T.showToast("商品未取到");
            finish();
            return;
        }
    }
}

