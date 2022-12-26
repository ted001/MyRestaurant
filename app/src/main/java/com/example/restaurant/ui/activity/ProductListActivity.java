package com.example.restaurant.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant.R;
import com.example.restaurant.bean.Order;
import com.example.restaurant.bean.Product;
import com.example.restaurant.biz.OrderBiz;
import com.example.restaurant.biz.ProductBiz;
import com.example.restaurant.net.CommonCallback;
import com.example.restaurant.ui.adapter.ProductItem;
import com.example.restaurant.ui.adapter.ProductListAdapter;
import com.example.restaurant.ui.view.refresh.SwipeRefresh;
import com.example.restaurant.ui.view.refresh.SwipeRefreshLayout;
import com.example.restaurant.util.T;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ProductListAdapter mAdapter;
    private List<ProductItem> mDatas = new ArrayList<>();
    private SwipeRefreshLayout mRefreshLayout;

    private TextView mTvCount;
    private Button mBtnPay;

    private ProductBiz mProductBiz = new ProductBiz();
    private OrderBiz mOrderBiz = new OrderBiz();
    private int mCurrentPage = 0;
    private float mTotalPrice;
    private int mTotalCount;

    private Order mOrder = new Order();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setUpToolbar();
        setTitle("订餐");
        loadData();
        initViews();
        initEvent();

    }

    private void initEvent() {
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTotalCount == 0) {
                    T.showToast("你还没选商品呢~~~");
                    return;
                }

                mOrder.setCount(mTotalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDatas.get(0).getRestaurant());
                Log.e("zhy", "mDatas.get(0).getRestaurant() = " + mDatas.get(0).getRestaurant().getId());

                startLoadingProgress();
                mOrderBiz.add(mOrder, new CommonCallback<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                        toLoginActivity();
                    }

                    @Override
                    public void onSuccess(String response) {
                        stopLoadingProgress();
                        T.showToast("生成订单成功~~~");
                        finish();

                    }
                });

            }
        });
    }

    private void loadData() {
        startLoadingProgress();
        mProductBiz.listByPage(0, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                T.showToast(e.getMessage());
                stopLoadingProgress();
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                mDatas.clear();
                for (Product p : response) {
                    mDatas.add(new ProductItem(p));
                    Log.e("zhy", p.getRestaurant().getId() + "");
                }
                mAdapter.notifyDataSetChanged();

                mTotalPrice = 0;
                mTotalCount = 0;
            }
        });
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mTvCount = (TextView) findViewById(R.id.id_tv_count);
        mBtnPay = (Button) findViewById(R.id.id_btn_pay);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_refresh_layout);


        //设置开关
        mRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        //设置颜色
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.BLUE, Color.GRAY);
        mRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        mRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });

        mAdapter = new ProductListAdapter(this, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalCount++;
                mTvCount.setText("数量：" + mTotalCount);
                mTotalPrice += productItem.getPrice();
                mBtnPay.setText(mTotalPrice + "元 立即支付");
                mOrder.addProduct(productItem);

            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalCount--;
                mTvCount.setText("数量：" + mTotalCount);
                mTotalPrice -= productItem.getPrice();
                mBtnPay.setText(mTotalPrice + "元 立即支付");
                mOrder.removeProduct(productItem);
            }
        });
    }


    private void loadMore() {
        mProductBiz.listByPage(++mCurrentPage, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                T.showToast(e.getMessage());
                stopLoadingProgress();
                mCurrentPage--;
                mRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                mRefreshLayout.setPullUpRefreshing(false);
                if (response.size() == 0) {
                    T.showToast("没有咯~~~");
                    return;
                }
                T.showToast("又找到" + response.size() + "道菜~~~");
                //mDatas.clear();
                for (Product p : response) {
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}

