package com.bean;

import java.io.Serializable;

/**
 * Created by user002 on 2017/3/2.
 */

public class ItemBean implements Serializable {

    private String callNumber;
    private long callTime;
    private String name;

    public ItemBean() {
    }

    public ItemBean(String name, String callNumber, long callTime) {
        this.name = name;
        this.callNumber = callNumber;
        this.callTime = callTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCallNumber(String callnumber) {
        this.callNumber = callnumber;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }

    public long getCallTime() {
        return callTime;
    }

}
