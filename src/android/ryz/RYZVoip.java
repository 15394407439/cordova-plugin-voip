package com.ryz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.activity.SoftCallActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

public class RYZVoip extends CordovaPlugin {

	public static final int ERROR  = 0;
	public static final String TAG = "RYZVoip";


	@Override
	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

	     if("direct".equals(action)) {// 拨打电话
			 JSONObject json = args.getJSONObject(0);
			 String userId = json.getString("custuser36id");
			 Log.e(TAG, "execute: "+ userId );
			 Intent intent = new Intent(cordova.getActivity(), SoftCallActivity.class);
			 intent.putExtra("custuser36id",userId);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 cordova.getActivity().startActivity(intent);
		    //callbackContext.success();
		 } else{
			 callbackContext.error("拨打电话失败");
			}
		return true;
	  }


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
