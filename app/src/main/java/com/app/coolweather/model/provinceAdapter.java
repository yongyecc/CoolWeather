package com.app.coolweather.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.coolweather.R;

import java.util.List;

public class provinceAdapter extends ArrayAdapter<province> {
    //接收代表列表视图的子视图ID
    private int resourceID;


    public provinceAdapter(@NonNull Context context, int resource, List<province> provinces) {
        super(context, resource, provinces);
        resourceID = resource;
    }

    /*
     * 根据构造方法传入的数据，获取对应的列表视图
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /*
         * 视图列表每一个子视图都有一个id，对应传进来的列表参数，getItem根据id获取传进来列表参数的对象
         */
        province pro = getItem(position);
        //写入视图模板布局文件
        View view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        /*
         * 获取布局文件对应的控件，修改它实现内容展示的功能
         */
        TextView textView = (TextView)view.findViewById(R.id.sonofprovince);
        textView.setText(pro.getProvince_name());
        return view;
    }
}
