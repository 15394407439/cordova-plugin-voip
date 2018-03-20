package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.CallPhoneBean;
import com.google.gson.Gson;
import com.redyouzi.diancall.cust.R;
import com.utils.Constants;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by user002 on 2017/4/25.
 */

public class HuiboActivity extends Activity implements View.OnClickListener {

    public static final String TAG ="HuiboActivity";
    private TextView mTv_call_phone;
    private TextView mTv_huibo_fanhui;
    private String mCustuser36id;
    private String mBase64;
    private String mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huibo_activity);

        initView();
        initLisenter();
        initDate();
    }

    //初始化控件
    private void initView() {
        mTv_call_phone = (TextView) findViewById(R.id.tv_call_phone);
        mTv_huibo_fanhui = (TextView) findViewById(R.id.tv_huibo_fanhui);
    }

    //控件监听
    private void initLisenter() {
        mTv_huibo_fanhui.setOnClickListener(this);

    }

    private void initDate() {
        Intent intent = getIntent();
        String number = intent.getStringExtra("phone_number");
        mCustuser36id = intent.getStringExtra("custuser36id");
        mResult = intent.getStringExtra("result");
        Log.e(TAG, "custuser36id ===== "+ mCustuser36id);
        Log.e(TAG, "callId ===== "+ mResult);
        mTv_call_phone.setText(number);
    }

    //点击取消拨打电话
    @Override
    public void onClick(View v) {
        //获取随机数
        Random r = new Random();
        String random = r.nextInt(900)+100 + "";
        //获取时间戳
        long timeMillis = System.currentTimeMillis();
        //使用base64进行加密
        String enToStr = Base64.encodeToString((mCustuser36id +timeMillis).getBytes(), Base64.DEFAULT);
        mBase64 = Base64.encodeToString(("ryz" + random + enToStr).getBytes(), Base64.DEFAULT);
        //contactDialInfo();

        new Thread(){
            @Override
            public void run() {
                //super.run();
                //去网络获取数据
                OkHttpClient client = new OkHttpClient();
                String CancelCallPhoneUrl = Constants.CANCELCALLPHONE + "callId="+mResult + "&" +"accesstoken="+ mBase64;

                Log.e(TAG, "CancelCallPhoneUrl : "+ CancelCallPhoneUrl);
                Request request = new Request.Builder().get().url(CancelCallPhoneUrl).build();  //构建一个请求
                try {
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){  //访问网络成功
                        String json = response.body().string();
                        Log.e(TAG, "请求网络json : "+ json);
                        Gson gson = new Gson();
                        CallPhoneBean bean = gson.fromJson(json, CallPhoneBean.class);
                        Log.e(TAG, "retcode : -----"+ bean.retcode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Toast.makeText(HuiboActivity.this,"您已取消拨号.....",Toast.LENGTH_SHORT).show();
        finish();
    }

    //看得见
    @Override
    protected void onStart() {
        super.onStart();
    }

    //获得焦点
    @Override
    protected void onResume() {
        super.onResume();
    }

    //失去焦点
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"HuiboActivity 界面失去焦点");
        finish();
    }

    //看不见
    @Override
    protected void onStop() {
        super.onStop();
    }

    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
