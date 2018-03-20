package com.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.ItemBean;
import com.redyouzi.diancall.cust.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class CallLogAdapter extends BaseAdapter {
    private static final String TAG = "CallLogAdapter";
    private final Context context;
    private List<ItemBean> mDatas;

    public CallLogAdapter(Context context, List<ItemBean> datas){
        this.context = context;
        this.mDatas = datas;
    }
    //获取条目的个数
    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.size();
    }

    //控制具体的条目显示
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_view,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemBean itemBean = mDatas.get(position);
        if (TextUtils.isEmpty(itemBean.getName())){
            holder.mTv_name.setVisibility(View.GONE);
            holder.tv_phone_number.setTextColor(Color.BLACK);
            holder.tv_phone_number.setTextSize(18);
        }else{
            holder.mTv_name.setText(itemBean.getName());
            holder.mTv_name.setVisibility(View.VISIBLE);
            holder.tv_phone_number.setTextColor(context.getResources().getColor(R.color.input_hint));
            holder.tv_phone_number.setTextSize(16);
        }

        holder.tv_phone_number.setText(itemBean.getCallNumber());
        String currentTime = StringUtils.parseDurationToMM(System.currentTimeMillis());
        String callTime = StringUtils.parseDurationToMM(itemBean.getCallTime());
        if (currentTime.equals(callTime)){
            holder.tv_system_time.setText(StringUtils.parseDurationToHH(itemBean.getCallTime()));
        }else {
            holder.tv_system_time.setText(callTime);
        }
        return convertView;
    }

    @Override
    public ItemBean getItem(int position) {
        return mDatas==null?null:mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addData(int index, ItemBean inputData) {
        if (mDatas==null){
            return;
        }
        mDatas.add(index,inputData);
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tv_phone_number;
        TextView tv_system_time;
        RelativeLayout rl_call_phone;
        private final TextView mTv_name;

        public ViewHolder(View convertView) {
            tv_phone_number = (TextView) convertView.findViewById(R.id.tv_phone_number);
            tv_system_time = (TextView) convertView.findViewById(R.id.tv_system_time);
            rl_call_phone = (RelativeLayout) convertView.findViewById(R.id.rl_call_phone);
            mTv_name = (TextView) convertView.findViewById(R.id.tv_name);
        }
    }

    /**
     * 将list数据添加到数据集
     * @param datas
     */
    public void addDataList(List<ItemBean> datas){
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 将现有展示的数据全部替换成新的数据
     * @param datas
     */
    public void changeDatas(List<ItemBean> datas){
        Log.e(TAG,"datas:"+datas);
        mDatas = datas;
        notifyDataSetChanged();
    }

}
