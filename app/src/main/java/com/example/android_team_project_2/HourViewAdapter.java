package com.example.android_team_project_2;

import android.annotation.SuppressLint;
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

    private final Context mContext;
    private final int mResource;
    private final ArrayList<My_date_hour> my_date_hours;
    private final Activity mActivity;
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

    @SuppressLint("SetTextI18n")
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
        //해당 연 월에 맞는 데이터를 검색해 커서에 저장

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

        if ((my_date_hours.get(position).hour) > -1)
            tv_date.setText(my_date_hours.get(position).hour + "");
        else
            tv_date.setText(" ");

        while (cursor.moveToNext()) {
            if ((my_date_hours.get(position).hour) > -1)
                break;
            //현재 포지션에 저장된 시간이 0이상인 경우 시간을 표기하는 리스트뷰 이기 때문에 break
            String Date = cursor.getString(2);
            String Time = cursor.getString(3);
            if(Date.equals(year + "." + month + "." + My_week_grid.get(position%7).date) && Time.equals(position/7+""))
                tv_date.setText(cursor.getString(1));
            //날짜와 시간이 현재 뷰에 해당하는 날짜와 시간과 같으면 일정의 제목을 텍스트로 설정
        }

        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tv_date.setHeight(width / 25 * 24 / 7);
        } else if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tv_date.setHeight(height / 25 * 24 / 7);
        }
        //주간 달력의 경우 일정이 두개이상 나올 수 없기 때문에 이전 버전에서 높이의 변화는 없다
        return convertView;
    }
}