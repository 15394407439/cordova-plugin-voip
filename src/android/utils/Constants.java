package com.utils;

/**
 * Created by user002 on 2017/4/26.
 */

public interface Constants {

    //线上地址
     String URL = "http://c.diancall.com";
    //测试地址
    //String URL = "http://192.168.1.128:6001";

  /**
   * 获取通话时长：
   *http://192.168.1.128:6001/pipes/custvoipcall/myvoicetimeAPP/cnl6MjM1TTJJeWIzcHNNVFE1TXpFNU5EYzFPVFkyT1E9PQo=*/

  /**
   * 回拨拨打
   * http://192.168.1.128:6001/pipes/custvoipcall/zexuntocallAPP?calledno=15394407439&accesstoken=M2Iyb3psMTQ5MzA4NDU2OTAwMA==*/


    //获取通话时长
    String CALLLONGURL = URL + "/pipes/custvoipcall/myvoicetimeAPP/";

    //回拨拨打电话
    String CALLPHONEURL = URL + "/pipes/custvoipcall/callbackAPP?";

    //取消拨打电话
    String CANCELCALLPHONE = URL + "/pipes/custvoipcall/cancelcallbackAPP/?";

}
