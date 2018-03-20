package com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.CallPhoneBean;
import com.bean.ItemBean;
import com.dao.CallPhoneDao;
import com.google.gson.Gson;
import com.redyouzi.diancall.cust.R;
import com.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user002 on 2017/4/25.
 */
public class ContactsDialActivty extends Activity implements View.OnClickListener {

    public static final String TAG ="ContactsDialActivty";
    private TextView mTv_name;
    private TextView mTv_number;
    private TextView mTv_back;
    private TextView mTv_call_phone;
    private String mBase64Phone;
    private String mCustuser36id;
    private String mBase64;
    private String mResult;
    private String mName;
    private String mPhoneNumber;
    private CallPhoneDao mCallPhoneDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);
        initView();
        intLisenser();
        initData();
    }

    //初始化控件
    private void initView() {
        mTv_name = (TextView) findViewById(R.id.name);
        mTv_number = (TextView) findViewById(R.id.number);
        mTv_back = (TextView) findViewById(R.id.tv_back);
        mTv_call_phone = (TextView) findViewById(R.id.tv_call_phone);
    }

    //控件监听
    private void intLisenser() {
        mTv_back.setOnClickListener(this);
        mTv_call_phone.setOnClickListener(this);
    }

    //初始化数据
    private void initData() {
        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mPhoneNumber = intent.getStringExtra("number");
        mCustuser36id = intent.getStringExtra("custuser36id");
        Log.e(TAG, "mCustuser36id :----- "+ mCustuser36id);
        mTv_name.setText(mName);
        mTv_number.setText(mPhoneNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_call_phone:
                //把电话号码和姓名and时间添加到数据库
                mCallPhoneDao = new CallPhoneDao(this);
                mCallPhoneDao.insert(new ItemBean(mName,mPhoneNumber, System.currentTimeMillis()));
               // getDataAndShow();
                //创建判断有误网络的对象
                ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if(info != null && info.isAvailable()){
                    //WiFi网络 , 移动网络
                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Log.e(TAG, "当前有网络---------- ");
                        imageCallPhone();   //请求拨号
                    } else {
                        Log.e(TAG, "当前无网络---------- ");
                        Toast.makeText(ContactsDialActivty.this,"网络连接失败,请稍后重试...",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    Log.e(TAG, "当前无网络---------- ");
                    Toast.makeText(ContactsDialActivty.this,"网络连接失败,请稍后重试...",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }

    }


    //点击拨号图片拨打电话
    private void imageCallPhone() {
        //获取随机数
        Random r = new Random();
        String random = r.nextInt(900)+100 + "";
        //获取时间戳
        long timeMillis = System.currentTimeMillis();
        //使用base64进行加密
        String enToStr = Base64.encodeToString((mCustuser36id +timeMillis).getBytes(), Base64.DEFAULT);
        mBase64 = Base64.encodeToString(("ryz" + random + enToStr).getBytes(), Base64.DEFAULT);

        new Thread(){
            @Override
            public void run() {
                //super.run();
                //去网络获取数据
                OkHttpClient client = new OkHttpClient();
                String callPhoneUrl = Constants.CALLPHONEURL + "calledno="+mPhoneNumber + "&" +"accesstoken="+ mBase64;
                Log.e(TAG, "callPhoneUrl : "+ callPhoneUrl);
                Request request = new Request.Builder().get().url(callPhoneUrl).build();  //构建一个请求
                try {
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){  //访问网络成功
                        String json = response.body().string();
                        Log.e(TAG, "请求网络json : "+ json);
                        Gson gson = new Gson();
                        CallPhoneBean bean = gson.fromJson(json, CallPhoneBean.class);
                        if(bean == null || bean.retcode != 0 ){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ContactsDialActivty.this,"服务器连接失败,请稍后...",Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        mResult = bean.result;
                        Intent intent = new Intent(ContactsDialActivty.this,HuiboActivity.class);
                        intent.putExtra("phone_number", mPhoneNumber);
                        intent.putExtra("custuser36id",mCustuser36id);
                        intent.putExtra("result",mResult);
                        startActivity(intent);
                        Log.e(TAG, "retcode : -----"+ bean.retcode);
                        Log.e(TAG, "result : -----"+ mResult);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
