package com.example.android_team_project_2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WeekViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final int mResource;
    private final ArrayList<My_date_week> my_date_weeks;
    private final Activity mActivity;

    public WeekViewAdapter(Context context, int Resource, ArrayList<My_date_week> dates, Activity activity) {
        mContext = context;
        mResource = Resource;
        my_date_weeks = dates;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return my_date_weeks.size();
    }

    @Override
    public Object getItem(int position) {
        return my_date_weeks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        TextView tv_date = convertView.findViewById(R.id.week_textView);

        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if ((my_date_weeks.get(position).date) > 100) {
            tv_date.setText(String.valueOf(my_date_weeks.get(position).date - 100));
            tv_date.setBackgroundColor(Color.CYAN);
        } else
            tv_date.setText(String.valueOf(my_date_weeks.get(position).date));

        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tv_date.setHeight(width / 25 * 24 / 7);
        } else if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tv_date.setHeight(height / 25 * 24 / 7);
        }

        return convertView;
    }
}

