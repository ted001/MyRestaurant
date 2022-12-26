package com.example.restaurant.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant.R;
import com.example.restaurant.UserInfoHolder;
import com.example.restaurant.bean.Order;
import com.example.restaurant.bean.User;
import com.example.restaurant.biz.OrderBiz;
import com.example.restaurant.net.CommonCallback;
import com.example.restaurant.ui.adapter.OrderAdapter;
import com.example.restaurant.ui.view.CircleTransform;
import com.example.restaurant.ui.view.refresh.SwipeRefresh;
import com.example.restaurant.ui.view.refresh.SwipeRefreshLayout;
import com.example.restaurant.util.T;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity {
    private Button mBtnTakeOrder;
    private ImageView mIvIcon;
    private TextView mTvName;

    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private List<Order> mDatas = new ArrayList<>();

    private SwipeRefreshLayout mRefreshLayout;

    private OrderBiz mOrderBiz = new OrderBiz();
    private int mCurrentPage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initViews();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLoadingProgress();
        loadData();
    }

    private void initViews() {

        mBtnTakeOrder = (Button) findViewById(R.id.id_btn_take_order);
        mIvIcon = (ImageView) findViewById(R.id.id_iv_icon);
        mTvName = (TextView) findViewById(R.id.id_tv_name);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_refresh_layout);

        User user = UserInfoHolder.getInstance().getUser();
        if (user != null) {
            mTvName.setText(user.getUsername());
        } else {
            toLoginActivity();
            return;
        }

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

        mAdapter = new OrderAdapter(this, mDatas);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        Picasso.with(this).load(R.drawable.icon).placeholder(R.drawable.pictures_no).transform(new CircleTransform()).into(mIvIcon);

    }

    private void initEvents() {
        mBtnTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProductListActivity();
            }
        });
    }

    private void loadData() {
        mOrderBiz.listByPage(0, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                if (e.getMessage().contains("用户未登录")) {
                    toLoginActivity();
                }

            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                T.showToast("更新订单成功~~~");
                mDatas.clear();
                mDatas.addAll(response);
                mAdapter.notifyDataSetChanged();
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void loadMore() {
        mOrderBiz.listByPage(++mCurrentPage, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mRefreshLayout.setPullUpRefreshing(false);
                mCurrentPage--;

                String message = e.getMessage();
                if (message.contains("用户未登录")) {
                    toLoginActivity();
                }

            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                if (response.size() == 0) {
                    T.showToast("木有历史订单啦~~~");
                    mRefreshLayout.setPullUpRefreshing(false);
                    return;
                }
                T.showToast("更新订单成功~~~");
                mDatas.addAll(response);
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setPullUpRefreshing(false);
            }
        });
    }

    private void toProductListActivity() {
//        Intent intent = new Intent(this, ProductListActivity.class);
//        startActivity(intent);
    }
}
