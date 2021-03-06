package com.example.android_team_project_2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
    //일정추가 메뉴 클릭시 실행
    public void floating_Click(View view) {
        myDBHelper = new MyDBHelper(this);
        CursorPoint = new int[300];
        switch (view.getId()) {
            //월간달력에서 클릭시
            case R.id.floatingMonth:
                Intent intent_month = new Intent(this, ScheduleActivity.class);
                //월간달력에서 실행해 일정추가 메뉴가 종료됐을 때 일정추가 메뉴를 실행했던 시점의 화면으로 돌아가기 위해 정보를 미리 저장
                intent_month.putExtra("type", "month");
                intent_month.putExtra("Month_Position", MonthPoint);
                //데이터 베이스에 정보가 없을 시 이전 화면의 정보만 저장 후 액티비티 실행
                if (myDBHelper == null)
                    startActivity(intent_month);
                //그게 아니라면 커서에 데이터 베이스의 모든 값 가져오기
                Cursor cursor = myDBHelper.getAllUsersByMethod();
                //아이템의 개수를 파악하기 위한 커서키와 아이템의 위치를 저장하기 위한 데이트키 생성
                int cursor_key = 0, date_key = 0;
                while (cursor.moveToNext()) {
                    if (cursor.getString(2).equals(ClickPoint)) {
                        CursorPoint[cursor_key++] = date_key;
                        //미리 생성해둔 배열의 커서키 위치에 데이트키 저장
                    }
                    date_key++;
                }

                String[] CursorDate = new String[cursor_key];
                for (int i = 0; i < cursor_key; i++) {
                    cursor.moveToPosition(CursorPoint[i]);
                    CursorDate[i] = cursor.getString(1);
                    //아이템에서 사용할 제목만 배열으로 저장
                }

                //아이템의 개수가 2개 이상일 때
                if (cursor_key > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //다이얼로그 생성
                    builder.setTitle(ClickPoint);
                    //타이틀은 클릭한 날짜로 설정
                    builder.setItems(CursorDate, (dialog, pos) -> {
                        intent_month.putExtra("selected", CursorPoint[pos]);
                        //제목을 저장해둔 배열을 통해 다이얼로그에 아이템 생성 후 클릭한 아이템의 제목을 저장한 후 액티비티 실행 하도록 작성
                        startActivity(intent_month);
                    });
                    builder.setPositiveButton("NEW", (dialog, id) -> {
                        startActivity(intent_month);
                        //기존의 아이템이 있어도 새로운 일정을 추가할 수 있도록 작성
                    });
                    builder.setNegativeButton("Cancel", null);
                    //Cancel 클릭시 다이얼로그 종료
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    //다이얼로그 생성 후 화면에 출력
                }

                else if (cursor_key == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //다이얼로그 생성
                    builder.setTitle(ClickPoint);
                    //타이틀은 클릭한 날짜로 설정
                    builder.setItems(CursorDate, (dialog, pos) -> {
                        intent_month.putExtra("selected", CursorPoint[pos]);
                        //제목을 저장해둔 배열을 통해 다이얼로그에 아이템 생성 후 클릭한 아이템의 위치를 저장한 후 액티비티 실행 하도록 작성
                        startActivity(intent_month);
                    });
                    builder.setPositiveButton("NEW", (dialog, id) -> {
                        startActivity(intent_month);
                        //기존의 아이템이 있어도 새로운 일정을 추가할 수 있도록 작성
                    });
                    builder.setNegativeButton("Cancel", null);
                    //Cancel 클릭시 다이얼로그 종료
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    //다이얼로그 생성 후 화면에 출력
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //다이얼로그 생성
                    builder.setTitle(ClickPoint);
                    //타이틀은 클릭한 날짜로 설정
                    builder.setPositiveButton("NEW", (dialog, id) -> startActivity(intent_month));
                    builder.setNegativeButton("Cancel", null);
                    //Cancel 클릭시 다이얼로그 종료
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    //다이얼로그 생성 후 화면에 출력
                }
                break;

            case R.id.floatingWeek:
                Intent intent_week = new Intent(this, ScheduleActivity.class);

                intent_week.putExtra("type", "week");
                intent_week.putExtra("time", WeekFragment.Clicktime);
                intent_week.putExtra("Week_Position", WeekPoint);

                if(myDBHelper == null)
                    startActivity(intent_week);


                cursor = myDBHelper.getAllUsersByMethod();

                int time_key = 0, time_count = 0;
                int[] intent_time = new int[1];
                String[] CursorTime = new String[1];

                while (cursor.moveToNext()) {
                    if(cursor.getString(2).equals(ClickPoint) && cursor.getString(3).equals(WeekFragment.Clicktime+"")) {
                        CursorTime[0] = cursor.getString(1);
                        //배열에 제목 저장
                        time_key ++;
                        intent_time[0] = time_count;
                        break;
                    }
                    time_count ++;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(ClickPoint);
                if (time_key != 0) {
                    //다이얼로그 생성
                    //타이틀은 클릭한 날짜로 설정
                    builder.setItems(CursorTime, (dialog, pos) -> {
                        intent_week.putExtra("selected", intent_time[0]);
                        //제목을 저장해둔 배열을 통해 다이얼로그에 아이템 생성 후 클릭한 아이템의 위치를 저장한 후 액티비티 실행 하도록 작성
                        startActivity(intent_week);
                    });
                    builder.setPositiveButton("NEW", (dialog, id) -> {
                        startActivity(intent_week);
                        //기존의 아이템이 있어도 새로운 일정을 추가할 수 있도록 작성
                    });
                    builder.setNegativeButton("Cancel", null);
                    //Cancel 클릭시 다이얼로그 종료
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    //다이얼로그 생성 후 화면에 출력
                } else {
                    //다이얼로그 생성
                    //타이틀은 클릭한 날짜로 설정
                    builder.setPositiveButton("NEW", (dialog, id) -> startActivity(intent_week));
                    builder.setNegativeButton("Cancel", null);
                    //Cancel 클릭시 다이얼로그 종료
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    //다이얼로그 생성 후 화면에 출력
                }
                break;
        }
    }
}