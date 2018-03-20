package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.redyouzi.diancall.cust.R;

/**
 * Created by user002 on 2017/10/31.
 */

public class CallTopUpActivity extends Activity implements View.OnClickListener {

    private ImageView mIv_cz_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("CallTopUpActivity","onCreate == 执行了");
        setContentView(R.layout.activity_call_top_up);
        initView();
        initListener();
        initData();
    }
    //初始化控件
    private void initView(){
        mIv_cz_back = (ImageView) findViewById(R.id.iv_cz_back);
    }

    //初始化监听
    private void initListener(){
        mIv_cz_back.setOnClickListener(this);
    }

    //初始化数据
    private void initData(){
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_cz_back:
                finish();
                break;
        }
    }

}
