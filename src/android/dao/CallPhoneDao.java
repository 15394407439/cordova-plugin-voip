package com.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class CallPhoneDao {

    private CallPhoneHelper mHelper;

    public CallPhoneDao(Context context) {
        mHelper = new CallPhoneHelper(context);
    }

    /**
     * 添加数据
     * @param bean
     * @return
     */
    public boolean insert(ItemBean bean){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name",bean.getName());
        values.put("phoneNum",bean.getCallNumber());
        values.put("time",bean.getCallTime());
        long result = db.insert("call_phone", null, values);
        db.close();
        return result!=-1;
    }

    /**
     * 查寻所有
     * @return
     */
    public List<ItemBean> queryAll(){
        List<ItemBean> list = new ArrayList<ItemBean>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] columns = {
                "name",
                "phoneNum",
               "time"
        };

        Cursor cursor = db.query("call_phone", columns, null, null, null, null, "time desc");

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String number = cursor.getString(cursor.getColumnIndex("phoneNum"));
            long time = cursor.getLong(cursor.getColumnIndex("time"));

            list.add(new ItemBean(name,number, time));

        }
        cursor.close();
        db.close();

        return list;

    }
}
