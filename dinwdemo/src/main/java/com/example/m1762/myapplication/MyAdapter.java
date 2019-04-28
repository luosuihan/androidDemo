package com.example.m1762.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    List mData;
    public MyAdapter(Context context,List data){
        this.mContext=context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //写一个静态的class,把layout_list_item的控件转移过来使用
    static class ViewHolder{
        //声明引用
        public TextView tvTitle,tvTime,tvContext;
    }
    //重要的方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //实例化ViewHolder
        ViewHolder holder = null;
        //如果视图为空
        if (convertView == null){
            //此处需要导入包，填写ListView的图标和标题等控件的来源，来自于layout_list_item这个布局文件
            convertView = mLayoutInflater.inflate(R.layout.layout_list_item,null);
            //生成一个ViewHolder的对象
            holder = new ViewHolder();
            //把layout_list_item对象转移过来，以便修改和赋值
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv1);
            //把这个holder传递进去
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //给控件赋值
        holder.tvTime.setText(mData.get(position).toString());
        return convertView;
    }
}
