package com.example.android_team_project_2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class HourViewAdapter extends BaseAdapter {

    private Context mContext;
    private int mResource;
    private ArrayList<My_date_hour> my_date_hours;
    private Activity mActivity;
    MyDBHelper myDBHelper;
    int year, month, date, xdate, dow, fmonth;

    public HourViewAdapter(Context context, int Resource, ArrayList<My_date_hour> hours, Activity activity) {
        mContext = context;
        mResource = Resource;
        my_date_hours = hours;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return my_date_hours.size();
    }

    @Override
    public Object getItem(int position) {
        return my_date_hours.get(position);
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
        TextView tv_date = convertView.findViewById(R.id.hour_textView);

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;

        calendar.set(year, month - 1 - MonthFragment.page, 1);

        myDBHelper = new MyDBHelper(convertView.getContext());
        Cursor cursor;
        cursor = myDBHelper.searchMonth(year+"."+month);

        ArrayList<My_date_week> My_week_grid = new ArrayList<>();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        date = calendar.get(Calendar.DATE);
        fmonth = month;

        if (Integer.MAX_VALUE / 2 - WeekFragment.monthPoint == 0)
            calendar.add(Calendar.DATE, -((WeekFragment.page - 1) * 7));
        else
            calendar.set(year, month - 1 - (Integer.MAX_VALUE / 2 - WeekFragment.monthPoint), 1 - ((WeekFragment.page - 1) * 7));

        dow = calendar.get(Calendar.DAY_OF_WEEK);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        date = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month - 2, date);

        xdate = calendar.getActualMaximum(Calendar.DATE);

        calendar.set(year, month - 1, date);

        int c = 1;

        for (int i = 1; i <= 7; i++) {
            if ((date - dow + i) <= 0) {
                My_week_grid.add(new My_date_week(xdate + date - dow + i));
            } else if ((date - dow + i) <= calendar.getActualMaximum(Calendar.DATE)) {
                My_week_grid.add(new My_date_week(date - dow + i));
            } else {
                My_week_grid.add(new My_date_week(c++));
            }
        }

        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if ((my_date_hours.get(position).hour) > -1)
            tv_date.setText(my_date_hours.get(position).hour + "");
        else
            tv_date.setText(" ");

        while (cursor.moveToNext()) {
            if ((my_date_hours.get(position).hour) > -1)
                break;
            String Date = cursor.getString(2);
            String Time = cursor.getString(3);
            if(Date.equals(year + "." + month + "." + My_week_grid.get(position%7).date) && Time.equals(position/7+""))
                tv_date.setText(cursor.getString(1));
        }

        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tv_date.setHeight(width / 25 * 24 / 7);
        } else if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tv_date.setHeight(height / 25 * 24 / 7);
        }

        return convertView;
    }
}