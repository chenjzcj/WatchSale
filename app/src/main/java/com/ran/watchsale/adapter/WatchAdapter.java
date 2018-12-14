package com.ran.watchsale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ran.watchsale.R;
import com.ran.watchsale.bean.Watch;

import java.util.List;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2016/6/21 0021
 */
public class WatchAdapter extends BaseAdapter {

    private List<Watch> watches;
    private Context context;

    public WatchAdapter(Context context, List<Watch> watches) {
        this.watches = watches;
        this.context = context;
    }

    @Override
    public int getCount() {
        return watches != null ? watches.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_watch, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Watch watch = watches.get(position);
        holder.id.setText(String.valueOf(watch.getId()));
        holder.number.setText(watch.getNumber());
        holder.date.setText(watch.getTime());
        holder.state.setText(String.valueOf(watch.isState()));
        return convertView;
    }

    public void setWatches(List<Watch> watches) {
        this.watches = watches;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView id;
        TextView number;
        TextView date;
        TextView state;

        public ViewHolder(View root) {
            id = (TextView) root.findViewById(R.id.tv_id);
            number = (TextView) root.findViewById(R.id.tv_number);
            date = (TextView) root.findViewById(R.id.tv_date);
            state = (TextView) root.findViewById(R.id.tv_state);
        }
    }
}
