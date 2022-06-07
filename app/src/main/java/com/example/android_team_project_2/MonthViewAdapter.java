package com.example.android_team_project_2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewAdapter extends BaseAdapter {

    private Context mContext;
    private int mResource;
    private ArrayList<My_date_month> my_date_months;
    private Activity mActivity;
    private int check = 0;
    MyDBHelper myDBHelper;

    public MonthViewAdapter(Context context, int Resource, ArrayList<My_date_month> dates, Activity activity) {
        mContext = context;
        mResource = Resource;
        my_date_months = dates;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return my_date_months.size();
    }

    @Override
    public Object getItem(int position) {
        return my_date_months.get(position);
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
        TextView tv_date = convertView.findViewById(R.id.month_textView);
        TextView tv_sc1 = convertView.findViewById(R.id.schedule_textView1);
        TextView tv_sc2 = convertView.findViewById(R.id.schedule_textView2);
        TextView tv_sc3 = convertView.findViewById(R.id.schedule_textView3);

        if (check == 0 && my_date_months.get(position).date == 1)
            check = 1;
        else if (check == 1 && my_date_months.get(position).date == 1)
            check = 0;

        if (position == 0 && my_date_months.get(position).date == 1)
            check = 1;

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        calendar.set(year, month - 1 - MonthFragment.page, 1);

        myDBHelper = new MyDBHelper(convertView.getContext());
        Cursor cursor;
        cursor = myDBHelper.searchMonth(year + "." + month);

        int cursorc = 0;

        while (cursor.moveToNext()) {
            if (check == 0)
                break;
            String Date = cursor.getString(2);
            if (Date.equals(year + "." + month + "." + my_date_months.get(position).date)) {
                if (cursorc == 0) {
                    tv_sc1.setText(cursor.getString(1));
                    tv_sc1.setVisibility(View.VISIBLE);
                    cursorc++;
                } else if (cursorc == 1) {
                    tv_sc2.setText(cursor.getString(1));
                    tv_sc2.setVisibility(View.VISIBLE);
                    cursorc++;
                } else if (cursorc == 2) {
                    tv_sc3.setText(cursor.getString(1));
                    tv_sc3.setVisibility(View.VISIBLE);
                    cursorc++;
                    break;
                }
            }
        }

        tv_date.setText(String.valueOf(my_date_months.get(position).date));

        if (check == 0) {
            tv_date.setBackgroundColor(Color.GRAY);
            tv_sc1.setBackgroundColor(Color.GRAY);
            tv_sc2.setBackgroundColor(Color.GRAY);
            tv_sc3.setBackgroundColor(Color.GRAY);
            tv_sc1.setVisibility(View.VISIBLE);
            tv_sc2.setVisibility(View.VISIBLE);
            tv_sc3.setVisibility(View.VISIBLE);
        }

        Display display = mContext.getDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        int Realheight = metrics.heightPixels;

        Rect rect = new Rect();
        Window window = mActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        int contentTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int statusBarHeight = rect.top;

        int bottomBarHeight = 0;
        int resourceIdBottom = mActivity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceIdBottom > 0)
            bottomBarHeight = mActivity.getResources().getDimensionPixelSize(resourceIdBottom);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        float density = displayMetrics.density;

        int Th = (int) (25 * density + 0.5);

        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            int pixels = Realheight - statusBarHeight - contentTop - Th - bottomBarHeight;
            switch (cursorc) {
                case 0:
                    tv_date.setHeight(pixels / 6);
                    tv_sc1.setHeight(0);
                    tv_sc2.setHeight(0);
                    tv_sc3.setHeight(0);
                    break;
                case 1:
                    tv_date.setHeight(pixels / 12);
                    tv_sc1.setHeight(pixels / 12);
                    tv_sc2.setHeight(0);
                    tv_sc3.setHeight(0);
                    break;
                case 2:
                    tv_date.setHeight(pixels / 18);
                    tv_sc1.setHeight(pixels / 18);
                    tv_sc2.setHeight(pixels / 18);
                    tv_sc3.setHeight(0);
                    break;
                case 3:
                    tv_date.setHeight(pixels / 24);
                    tv_sc1.setHeight(pixels / 24);
                    tv_sc2.setHeight(pixels / 24);
                    tv_sc3.setHeight(pixels / 24);
                    break;
            }
        } else if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int pixels = Realheight - statusBarHeight - contentTop - Th;
            switch (cursorc) {
                case 0:
                    tv_date.setHeight(pixels / 6);
                    tv_sc1.setHeight(0);
                    tv_sc2.setHeight(0);
                    tv_sc3.setHeight(0);
                    break;
                case 1:
                    tv_date.setHeight(pixels / 12);
                    tv_sc1.setHeight(pixels / 12);
                    tv_sc2.setHeight(0);
                    tv_sc3.setHeight(0);
                    break;
                case 2:
                    tv_date.setHeight(pixels / 18);
                    tv_sc1.setHeight(pixels / 18);
                    tv_sc2.setHeight(pixels / 18);
                    tv_sc3.setHeight(0);
                    break;
                case 3:
                    tv_date.setHeight(pixels / 24);
                    tv_sc1.setHeight(pixels / 24);
                    tv_sc2.setHeight(pixels / 24);
                    tv_sc3.setHeight(pixels / 24);
                    break;
            }
        }
        return convertView;
    }
}