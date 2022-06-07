package com.example.android_team_project_2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    int MonthPoint = Integer.MAX_VALUE / 2;
    int WeekPoint = Integer.MAX_VALUE / 2;
    int year, month, date, dow;
    int  WeekCheck = 0, fmonth = 0, monthPage = 0, weekPage = 0;
    static String ClickPoint = "";
    static int[] CursorPoint;
    MyDBHelper myDBHelper;
    String name = "month";
    Intent intent;

    public int getMonthPoint() {
        return MonthPoint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();

        if(intent != null) {
            MonthPoint = intent.getIntExtra("Month_Position", Integer.MAX_VALUE / 2);
            WeekPoint = intent.getIntExtra("Week_Position", Integer.MAX_VALUE / 2);
            name = intent.getStringExtra("type");
        }

        if(name == null || name.equals("month")) {
            setContentView(R.layout.activity_month);

            ViewPager2 vpPager = findViewById(R.id.vpPager1);
            FragmentStateAdapter adapter = new MonthPagerAdapter(this);
            vpPager.setAdapter(adapter);

            vpPager.setCurrentItem(MonthPoint, false);

            vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    Calendar calendar = Calendar.getInstance();

                    MonthPoint = position;
                    monthPage = Integer.MAX_VALUE / 2 - position;

                    calendar.add(Calendar.MONTH, -monthPage);

                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH) + 1;

                    setTitle(year + "년 " + month + "월");
                }
            });
        }

        else if(name.equals("week")) {
            setContentView(R.layout.activity_week);

            ViewPager2 vpPager2 = findViewById(R.id.vpPager2);
            FragmentStateAdapter adapter2 = new WeekPagerAdapter(this);
            vpPager2.setAdapter(adapter2);

            vpPager2.setCurrentItem(WeekPoint, false);

            vpPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    Calendar calendar = Calendar.getInstance();

                    weekPage = Integer.MAX_VALUE / 2 - position;

                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH) + 1;
                    date = calendar.get(Calendar.DATE);
                    fmonth = month;

                    if (Integer.MAX_VALUE / 2 - MonthPoint == 0)
                        calendar.add(Calendar.DATE, -(weekPage * 7));
                    else {
                        calendar.add(Calendar.MONTH, -(Integer.MAX_VALUE / 2 - MonthPoint));
                        fmonth = calendar.get(Calendar.MONTH) + 1;
                        calendar.set(year, month - 1 - (Integer.MAX_VALUE / 2 - MonthPoint), 1 - (weekPage * 7));
                    }

                    dow = calendar.get(Calendar.DAY_OF_WEEK);
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH) + 1;
                    date = calendar.get(Calendar.DAY_OF_MONTH);

                    if (date - dow + 1 <= 0) {
                        setTitle(year + "년 " + (month - 1) + "월 / " + month + "월");
                        WeekCheck = fmonth - month;
                    } else if (date - dow + 7 > calendar.getActualMaximum(Calendar.DATE)) {
                        setTitle(year + "년 " + month + "월 / " + (month + 1) + "월");
                        WeekCheck = fmonth - month + 1;
                    } else {
                        setTitle(year + "년 " + month + "월");
                        WeekCheck = fmonth - month;
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_month:
                setContentView(R.layout.activity_month);

                ViewPager2 vpPager1 = findViewById(R.id.vpPager1);
                FragmentStateAdapter adapter1 = new MonthPagerAdapter(this);
                vpPager1.setAdapter(adapter1);

                vpPager1.setCurrentItem(MonthPoint - WeekCheck, false);

                vpPager1.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        Calendar calendar = Calendar.getInstance();

                        MonthPoint = position;

                        monthPage = Integer.MAX_VALUE / 2 - position;

                        calendar.add(Calendar.MONTH, -monthPage);

                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH) + 1;

                        setTitle(year + "년 " + month + "월");
                    }
                });
                return true;

            case R.id.action_week:
                setContentView(R.layout.activity_week);

                ViewPager2 vpPager2 = findViewById(R.id.vpPager2);
                FragmentStateAdapter adapter2 = new WeekPagerAdapter(this);
                vpPager2.setAdapter(adapter2);

                vpPager2.setCurrentItem(WeekPoint, false);

                vpPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        Calendar calendar = Calendar.getInstance();

                        weekPage = Integer.MAX_VALUE / 2 - position;

                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH) + 1;
                        date = calendar.get(Calendar.DATE);
                        fmonth = month;

                        if (Integer.MAX_VALUE / 2 - MonthPoint == 0)
                            calendar.add(Calendar.DATE, -(weekPage * 7));
                        else {
                            calendar.add(Calendar.MONTH, -(Integer.MAX_VALUE / 2 - MonthPoint));
                            fmonth = calendar.get(Calendar.MONTH) + 1;
                            calendar.set(year, month - 1 - (Integer.MAX_VALUE / 2 - MonthPoint), 1 - (weekPage * 7));
                        }

                        dow = calendar.get(Calendar.DAY_OF_WEEK);
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH) + 1;
                        date = calendar.get(Calendar.DAY_OF_MONTH);

                        if (date - dow + 1 <= 0) {
                            setTitle(year + "년 " + (month - 1) + "월 / " + month + "월");
                            WeekCheck = fmonth - month;
                        } else if (date - dow + 7 > calendar.getActualMaximum(Calendar.DATE)) {
                            setTitle(year + "년 " + month + "월 / " + (month + 1) + "월");
                            WeekCheck = fmonth - month + 1;
                        } else {
                            setTitle(year + "년 " + month + "월");
                            WeekCheck = fmonth - month;
                        }
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void fClick(View view) {
        myDBHelper = new MyDBHelper(this);
        CursorPoint = new int[300];
        switch (view.getId()) {
            case R.id.floatingMonth:
                Intent intent_month = new Intent(this, ScheduleActivity.class);

                intent_month.putExtra("type", "month");
                intent_month.putExtra("Month_Position", MonthPoint);

                if (myDBHelper == null)
                    startActivity(intent_month);

                Cursor cursor = myDBHelper.getAllUsersBySQL();

                StringBuffer buffer = new StringBuffer();
                int cursor_key = 0, date_key = 0;
                while (cursor.moveToNext()) {
                    if (cursor.getString(2).equals(ClickPoint)) {
                        buffer.append(cursor.getString(2));
                        CursorPoint[cursor_key++] = date_key;
                    }
                    date_key++;
                }
                if (cursor_key > 1) {
                    String[] CursorDate = new String[cursor_key];
                    for (int i = 0; i < cursor_key; i++) {
                        cursor.moveToPosition(CursorPoint[i]);
                        CursorDate[i] = cursor.getString(1);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle(ClickPoint);

                    builder.setItems(CursorDate, (dialog, pos) -> {
                        intent_month.putExtra("selected", CursorPoint[pos]);

                        startActivity(intent_month);
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if (cursor_key == 1) {
                    intent_month.putExtra("selected", CursorPoint[0]);
                    startActivity(intent_month);
                } else
                    startActivity(intent_month);
                break;

            case R.id.floatingWeek:
                Intent intent_week = new Intent(this, ScheduleActivity.class);

                intent_week.putExtra("type", "week");
                intent_week.putExtra("time", WeekFragment.Clicktime);
                intent_week.putExtra("Week_Position", WeekPoint);

                if(myDBHelper == null)
                    startActivity(intent_week);


                cursor = myDBHelper.getAllUsersBySQL();

                int time_key = 0, time_count = 0;

                while (cursor.moveToNext()) {
                    if(cursor.getString(2).equals(ClickPoint) && cursor.getString(3).equals(WeekFragment.Clicktime+"")) {
                        intent_week.putExtra("selected", time_count);
                        startActivity(intent_week);
                        time_key ++;
                        break;
                    }
                    time_count ++;
                }

                if(time_key == 0)
                    startActivity(intent_week);
                break;
        }
    }
}