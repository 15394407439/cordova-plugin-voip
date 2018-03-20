package com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.CallLongBean;
import com.bean.CallPhoneBean;
import com.bean.ItemBean;
import com.dao.CallPhoneDao;
import com.google.gson.Gson;
import com.redyouzi.diancall.cust.R;
import com.utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @Title:Android客户端
 * @Description: 电话拨打界面
 */
public class SoftCallActivity extends Activity implements View.OnClickListener, TextWatcher {
	private static final int REQUEST_READ_CONTACTS =101 ;
	private final String TAG = "SoftCallActivity";
	private Context mContext;
	private LinearLayout mLl_bohao;
	private LinearLayout mLl_tongxunlu;
	private TextView mTv_hujiao;
	private PopupWindow mPopupWindow;
	private ImageView mIv_barBack;
	private RelativeLayout mRl_call_bottom;
	private View mPopupView;
	private ImageView mIv_oneButton;
	private ImageView mIv_twoButton;
	private ImageView mIv_threeButton;
	private ImageView mIv_fourButton;
	private ImageView mIv_fiveButton;
	private ImageView mIv_sixButton;
	private ImageView mIv_sevenButton;
	private ImageView mIv_eightButton;
	private ImageView mIv_NineButton;
	private ImageView mIv_pasteButton;
	private ImageView mIv_zeroButton;
	private ImageView mIv_deleteButton;
	private TextView mTv_call_phone;
	private TextView mTv_call_long_time;
	private ListView mListview;
	private CallLogAdapter mAdapter;
	private TextView mTv_phone_number;
	private List<ItemBean> mList;
	private String mCurrentTime;
	private ItemBean mBean;
	private CallPhoneDao mCallPhoneDao;
	private String mCalllongtime;
	private String mCustuser36id;
	private String mBase64Time;
	private String mBase64Call;
	private String mBase64Item;
	private int mLongtime;
	private String mReplacePhone;
	private TextView mTv_top_up;
	private ItemBean mItem_bean;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate: 执行了 ");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext=this;
		setContentView(R.layout.vs_callphone_layout);
		showKeyboard();
		initView();
		initListener();
		initDate();
	}


	private void showKeyboard() {

		mPopupView = LayoutInflater.from(SoftCallActivity.this).inflate(R.layout.keyboard_layout, null);
		mPopupWindow = new PopupWindow(mPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setContentView(mPopupView);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);

		mPopupWindow.setFocusable(false); //该值为false时，点击弹窗框外面window不会消失，即使设置了背景也无效，只能由dismiss()关闭

		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
		mPopupWindow.getContentView().setFocusableInTouchMode(true);
		mPopupWindow.getContentView().setFocusable(true);
	}

	//初始化控件
	private void initView() {
		mIv_barBack = (ImageView) findViewById(R.id.bar_back);
		mLl_bohao = (LinearLayout) findViewById(R.id.ll_bohao);
		mLl_tongxunlu = (LinearLayout) findViewById(R.id.ll_tongxunlu);
		mTv_hujiao = (TextView) findViewById(R.id.tv_hujiao);
		mRl_call_bottom = (RelativeLayout) findViewById(R.id.rl_call_bottom);
		mTv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
		mTv_call_long_time = (TextView) findViewById(R.id.tv_call_long_time);
		mTv_top_up = (TextView) findViewById(R.id.tv_top_up);
		mListview = (ListView) findViewById(R.id.listview);

		//获取键盘控件ID
		mIv_oneButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitOneButton);
		mIv_twoButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitTwoButton);
		mIv_threeButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitThreeButton);

		mIv_fourButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitFourButton);
		mIv_fiveButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitFiveButton);
		mIv_sixButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitSixButton);

		mIv_sevenButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitSevenButton);
		mIv_eightButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitEightButton);
		mIv_NineButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitNineButton);

		mIv_pasteButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitPasteButton);
		mIv_zeroButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitZeroButton);
		mIv_deleteButton = (ImageView) mPopupView.findViewById(R.id.keyboard_DigitDeleteButton);

		//输入号码框的id
		mTv_call_phone = (TextView) mPopupView.findViewById(R.id.tv_call_phone);

	}

	//初始化监听
	private void initListener() {
		mTv_top_up.setOnClickListener(this);
		mLl_tongxunlu.setOnClickListener(this);
		mLl_bohao.setOnClickListener(this);
		mIv_barBack.setOnClickListener(this);
		mTv_hujiao.setOnClickListener(this);
		//键盘的数字监听
		mIv_oneButton.setOnClickListener(this);
		mIv_twoButton.setOnClickListener(this);
		mIv_threeButton.setOnClickListener(this);

		mIv_fourButton.setOnClickListener(this);
		mIv_fiveButton.setOnClickListener(this);
		mIv_sixButton.setOnClickListener(this);

		mIv_sevenButton.setOnClickListener(this);
		mIv_eightButton.setOnClickListener(this);
		mIv_NineButton.setOnClickListener(this);

		mIv_pasteButton.setOnClickListener(this);
		mIv_zeroButton.setOnClickListener(this);
		mIv_deleteButton.setOnClickListener(this);
		mTv_call_phone.addTextChangedListener(this);

		//给通话记录的Item条目做监听
		mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mItem_bean = (ItemBean) mAdapter.getItem(position);
				//Pattern p = Pattern.compile("[0-9]*");    //判断是否是纯数字
				//Matcher m = p.matcher(mPhone_number);
					ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo info = connectivity.getActiveNetworkInfo();
					if(info != null && info.isAvailable()){
						//WiFi网络 , 移动网络
						if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
							Log.e(TAG, "当前有网络---------- ");
							itemCallPhone();  //点击条目请求拨号
						} else {
							Log.e(TAG, "当前无网络---------- ");
							Toast.makeText(SoftCallActivity.this,"网络连接失败,请稍后重试...",Toast.LENGTH_SHORT).show();
							return;
						}
					}else{
						Log.e(TAG, "当前无网络---------- ");
						Toast.makeText(SoftCallActivity.this,"网络连接失败,请稍后重试...",Toast.LENGTH_SHORT).show();
						return;
					}

				if(mItem_bean.getName() == null || mItem_bean.getName().equals("")){
					mCallPhoneDao.insert(new ItemBean("", mItem_bean.getCallNumber(),System.currentTimeMillis()));
					getDataAndShow();
				}else{
					mCallPhoneDao.insert(new ItemBean(mItem_bean.getName(), mItem_bean.getCallNumber(),System.currentTimeMillis()));
					getDataAndShow();
				}

			}

			//点击条目拨打电话
			private void itemCallPhone() {
				mBase64Item = base64Encrypt();
				new Thread(){
					@Override
					public void run() {
						//super.run();
						//去网络获取数据
						OkHttpClient client = new OkHttpClient();
						String callItemUrl = Constants.CALLPHONEURL + "calledno="+mItem_bean.getCallNumber() + "&" +"accesstoken="+ mBase64Item;
						Log.e(TAG, "callPhoneUrl : "+ callItemUrl);
						Request request = new Request.Builder().get().url(callItemUrl).build();  //构建一个请求
						try {
							Response response = client.newCall(request).execute();
							if(response.isSuccessful()){  //访问网络成功
								String json = response.body().string();
								Log.e(TAG, "请求网络json : "+ json);
								Gson gson = new Gson();
								CallPhoneBean bean = gson.fromJson(json, CallPhoneBean.class);
								Log.e(TAG, "retcode : -----"+ bean.retcode);
								if(bean == null || bean.retcode != 0){
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(SoftCallActivity.this,"服务器连接失败,请稍后...",Toast.LENGTH_SHORT).show();
										}
									});
									return;
								}
								Intent intent = new Intent(SoftCallActivity.this,HuiboActivity.class);
								intent.putExtra("phone_number", mItem_bean.getCallNumber());
								intent.putExtra("custuser36id", mCustuser36id);
								intent.putExtra("result", bean.result);
								startActivity(intent);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		});

	}

	//点击响应的方法
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.bar_back:
				finish();
			    break;
			case R.id.tv_top_up:
				Intent call_intent = new Intent(this,CallTopUpActivity.class);
				startActivity(call_intent);
				break;
			case R.id.ll_bohao:
				Log.e(TAG, "onClick:拨号被点击了");
				if (mPopupWindow != null && !mPopupWindow.isShowing()) {
					mPopupWindow.showAtLocation(findViewById(R.id.ll_call_log), Gravity.BOTTOM, 0, mRl_call_bottom.getHeight()+20);
				}else if(mPopupWindow != null && mPopupWindow.isShowing()){
					mPopupWindow.dismiss();
				}
				break;
			case R.id.ll_tongxunlu:
				Intent intent = new Intent(this,ContactsActivity.class);
				intent.putExtra("custuser36id",mCustuser36id);

				startActivity(intent);
				break;
			case R.id.keyboard_DigitOneButton:  //键盘数字1
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim()+"1");
				break;
			case R.id.keyboard_DigitTwoButton:  //键盘数字2
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim()+"2");
				break;
			case R.id.keyboard_DigitThreeButton: //键盘数字3
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim() +"3");
				break;
			case R.id.keyboard_DigitFourButton: //键盘数字4
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim()+"4");
				break;
			case R.id.keyboard_DigitFiveButton: //键盘数字5
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim()+"5");
				break;
			case R.id.keyboard_DigitSixButton: //键盘数字6
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim()+"6");
				break;
			case R.id.keyboard_DigitSevenButton:  //键盘数字7
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim()+"7");
				break;
			case R.id.keyboard_DigitEightButton: //键盘数字8
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim()+"8");
				break;
			case R.id.keyboard_DigitNineButton: //键盘数字9
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim()+"9");
				break;
			case R.id.keyboard_DigitPasteButton: //键盘粘贴
				break;
			case R.id.keyboard_DigitZeroButton: //键盘数字0
				mTv_call_phone.setText(mTv_call_phone.getText().toString().trim()+"0");
				break;
			case R.id.keyboard_DigitDeleteButton: //键盘删除键
				String et_lenght = mTv_call_phone.getText().toString();
				if(et_lenght.length() != 0){
					mTv_call_phone.setText(et_lenght.substring(0,et_lenght.length()-1));
				}
				break;

			case R.id.tv_hujiao:
				//Toast.makeText(this,"正在为您接通，请稍候!",Toast.LENGTH_SHORT).show()
				String inputData= mTv_call_phone.getText().toString();
				mReplacePhone = inputData.replaceAll(" ", "");
				Log.e(TAG, "mReplacePhone:替换之后的号码为:"+mReplacePhone);
				if (TextUtils.isEmpty(mReplacePhone)){
					Toast.makeText(this,"号码不能为空哦！！",Toast.LENGTH_SHORT).show();
					return;
				}else {
					String regex = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(mReplacePhone);
					if(matcher.find()){
						ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
						NetworkInfo info = connectivity.getActiveNetworkInfo();
						if(info != null && info.isAvailable()){
							//WiFi网络 , 移动网络
							if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
								Log.e(TAG, "当前有网络---------- ");
								requestNetworkPhoneNumber();  //请求网络拨打电话
							} else {
								Log.e(TAG, "当前无网络---------- ");
								Toast.makeText(SoftCallActivity.this,"网络连接失败,请稍后重试...",Toast.LENGTH_SHORT).show();
								return;
							}
						}else{
							Log.e(TAG, "当前无网络---------- ");
							Toast.makeText(SoftCallActivity.this,"网络连接失败,请稍后重试...",Toast.LENGTH_SHORT).show();
							return;
						}

					}else{
						Toast.makeText(this,"你输入的号码有误，请重新输入",Toast.LENGTH_SHORT).show();
						return;
					}
				}

			mBean = new ItemBean("",mReplacePhone,System.currentTimeMillis());
			mCallPhoneDao.insert(mBean);
			//mAdapter.addData(0, mBean);
			getDataAndShow();
			mTv_call_phone.setText("");
			break;
		}
	}

	//获取数据库的数据并显示出来
	public void getDataAndShow() {
		new Thread(){
			@Override
			public void run() {
				final List<ItemBean> list = mCallPhoneDao.queryAll();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mAdapter.changeDatas(list);
					}
				});
			}
		}.start();
	}


	//请求网络拨打电话
	private void requestNetworkPhoneNumber() {
		mBase64Call = base64Encrypt();
		new Thread(){
			@Override
			public void run() {
				//super.run();
				//去网络获取数据
				OkHttpClient client = new OkHttpClient();
				String callPhoneUrl = Constants.CALLPHONEURL + "calledno="+mReplacePhone + "&" +"accesstoken="+ mBase64Call;
				Log.e(TAG, "callPhoneUrl : "+ callPhoneUrl);
				Request request = new Request.Builder().get().url(callPhoneUrl).build();  //构建一个请求
				try {
					Response response = client.newCall(request).execute();
					if(response.isSuccessful()){  //访问网络成功
						String json = response.body().string();
						Log.e(TAG, "请求网络json : "+ json);
						Gson gson = new Gson();
						CallPhoneBean bean = gson.fromJson(json, CallPhoneBean.class);
						Log.e(TAG, "retcode : -----"+ bean.retcode);
						if(bean == null || bean.retcode != 0){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(SoftCallActivity.this,"服务器连接失败,请稍后...",Toast.LENGTH_SHORT).show();
								}
							});
							return;
						}
						Intent in = new Intent(SoftCallActivity.this,HuiboActivity.class);
						in.putExtra("phone_number", mReplacePhone);
						in.putExtra("custuser36id", mCustuser36id);
						in.putExtra("result", bean.result);
						startActivity(in);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	//初始化数据
	private void initDate() {

		mCallPhoneDao = new CallPhoneDao(this);
		mList = mCallPhoneDao.queryAll();
		mAdapter = new CallLogAdapter(SoftCallActivity.this, mList);
		mListview.setAdapter(mAdapter);
		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if(info != null && info.isAvailable()){
			//WiFi网络 , 移动网络
			if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
				Log.e(TAG, "当前有网络---------- ");
				requestCallLongTime();   //请求当前拨号时间
			} else {
				Log.e(TAG, "当前无网络---------- ");
				Toast.makeText(SoftCallActivity.this,"网络连接失败,无法获得通话时长...",Toast.LENGTH_LONG).show();
				return;
			}
		}else{
			Log.e(TAG, "当前无网络---------- ");
			Toast.makeText(SoftCallActivity.this,"网络连接失败,无法获得通话时长...",Toast.LENGTH_LONG).show();
			return;
		}

	}

	//请求网络通话时长
	private void requestCallLongTime() {
		mBase64Time = base64Encrypt();
		Log.e(TAG, "mBase64Time1---------- "+mBase64Time);
		new Thread(){
			@Override
			public void run() {
				//super.run();
				Log.e(TAG, "initDate===========2: ");
				//去网络获取数据
				OkHttpClient client = new OkHttpClient();
				String calllongurl = Constants.CALLLONGURL + mBase64Time;
				Log.e(TAG, "calllongurl : "+ calllongurl);
				Request request = new Request.Builder().get().url(calllongurl).build();  //构建一个请求
				try {
					Response response = client.newCall(request).execute();
					if(response.isSuccessful()){  //访问网络成功
						String json = response.body().string();
						Gson gson = new Gson();
						CallLongBean bean = gson.fromJson(json, CallLongBean.class);
						if(bean != null){
							mCalllongtime = bean.result;
							mLongtime = Integer.parseInt(mCalllongtime)/60;
							Log.e(TAG, "获取通话时长 : "+ mLongtime);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mTv_call_long_time.setText(mLongtime+" 分钟");
								}
							});
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	//用base64进行加密
	public String base64Encrypt() {
		Intent intent = getIntent();
		mCustuser36id = intent.getStringExtra("custuser36id");
		//获取随机数
		Random r = new Random();
		String random = r.nextInt(900)+100 + "";
		//获取时间戳
		long timeMillis = System.currentTimeMillis();
		//使用base64进行加密
		String enToStr = Base64.encodeToString((mCustuser36id +timeMillis).getBytes(), Base64.DEFAULT);
		String base64 = Base64.encodeToString(("ryz" + random + enToStr).getBytes(), Base64.DEFAULT);
		return base64;
	}

	@Override
	protected void onStart() {
		super.onStart();
		//mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getDataAndShow();
		//mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "SoftCallActivity : 退出了");
		mPopupWindow.dismiss();
		mPopupView = null;
		mPopupWindow = null;
	}


	//对text文本进行监听
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s == null || s.length() == 0)
			return;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (i != 3 && i != 8 && s.charAt(i) == ' ') {
				continue;
			} else {
				sb.append(s.charAt(i));
				if ((sb.length() == 4 || sb.length() == 9)
						&& sb.charAt(sb.length() - 1) != ' ') {
					sb.insert(sb.length() - 1, ' ');
				}
			}
		}
		if (!sb.toString().equals(s.toString())) {
			int index = start + 1;
			if (sb.charAt(start) == ' ') {
				if (before == 0) {
					index++;
				} else {
					index--;
				}
			} else {
				if (before == 1) {
					index--;
				}
			}
			mTv_call_phone.setText(sb.toString());
			//mTv_call_phone.setSelection(index);
			if (mTv_call_phone.getText().toString().length()>=13) {
				mTv_call_phone.setText(mTv_call_phone.getText().toString().substring(0,13));
			}
		}
	}

	@Override
	public void afterTextChanged(Editable editable) {

	}
}
