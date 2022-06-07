package com.example.android_team_project_2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class WeekFragment extends Fragment {
    static int page = 0;
    int year, month, dow, date, xdate, fmonth;
    int test = -1;
    int xPosition;
    static int monthPoint;
    static int Clicktime = 0;
    WeekViewAdapter weekViewAdapter;
    HourViewAdapter hourGridViewAdapter;
    HourViewAdapter hourListViewAdapter;
    ArrayList<My_date_week> My_week_grid;
    ArrayList<My_date_hour> My_hour_grid;
    ArrayList<My_date_hour> My_hour_list;
    View selectedHourView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week, container, false);

        Bundle bundle = getArguments();

        assert bundle != null;
        page = Integer.MAX_VALUE / 2 - bundle.getInt("position");

        GridView week_grid = rootView.findViewById(R.id.week_grid);

        My_week_grid = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        Activity activity = getActivity();

        assert activity != null;
        monthPoint = ((MainActivity) activity).getMonthPoint();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        date = calendar.get(Calendar.DATE);
        fmonth = month;

        if (Integer.MAX_VALUE / 2 - monthPoint == 0)
            calendar.add(Calendar.DATE, -(page * 7));
        else
            calendar.set(year, month - 1 - (Integer.MAX_VALUE / 2 - monthPoint), 1 - (page * 7));

        dow = calendar.get(Calendar.DAY_OF_WEEK);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        date = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month - 2, date);

        xdate = calendar.getActualMaximum(Calendar.DATE);

        calendar.set(year, month - 1, date);

        int c = 1;

        if (date - dow + 1 <= 0)
            getActivity().setTitle(year + "년 " + (month - 1) + "월 / " + month + "월");
        else if (date - dow + 7 > calendar.getActualMaximum(Calendar.DATE))
            getActivity().setTitle(year + "년 " + month + "월 / " + (month + 1) + "월");
        else
            getActivity().setTitle(year + "년 " + month + "월");

        for (int i = 1; i <= 7; i++) {
            if ((date - dow + i) <= 0) {
                My_week_grid.add(new My_date_week(xdate + date - dow + i));
            } else if ((date - dow + i) <= calendar.getActualMaximum(Calendar.DATE)) {
                My_week_grid.add(new My_date_week(date - dow + i));
            } else {
                My_week_grid.add(new My_date_week(c++));
            }
        }
        weekViewAdapter = new WeekViewAdapter(getActivity(), R.layout.week_item, My_week_grid, getActivity());

        week_grid.setAdapter(weekViewAdapter);

        week_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (test > 0) {
                    My_week_grid.remove(xPosition % 7);
                    if (test > 100)
                        My_week_grid.add(xPosition % 7, new My_date_week(test - 100));
                    else
                        My_week_grid.add(xPosition % 7, new My_date_week(test));

                    test = My_week_grid.get(i % 7).date;

                    My_week_grid.remove(i % 7);
                    if (test > 100)
                        My_week_grid.add(i % 7, new My_date_week(test));
                    else
                        My_week_grid.add(i % 7, new My_date_week(test + 100));

                    weekViewAdapter = new WeekViewAdapter(getActivity(), R.layout.week_item, My_week_grid, getActivity());

                    xPosition = i;

                    week_grid.setAdapter(weekViewAdapter);
                }
                test = My_week_grid.get(i % 7).date;
                My_week_grid.remove(i % 7);
                if (test > 100)
                    My_week_grid.add(i % 7, new My_date_week(test));
                else
                    My_week_grid.add(i % 7, new My_date_week(test + 100));

                xPosition = i;

                weekViewAdapter = new WeekViewAdapter(getActivity(), R.layout.week_item, My_week_grid, getActivity());

                week_grid.setAdapter(weekViewAdapter);
            }
        });

        ExpandableHeightGridView hour_grid = (ExpandableHeightGridView) rootView.findViewById(R.id.hour_grid);

        hour_grid.setExpanded(true);

        My_hour_grid = new ArrayList<>();

        for (int i = 0; i < 168; i++)
            My_hour_grid.add(new My_date_hour(-1));

        hourGridViewAdapter = new HourViewAdapter(getActivity(), R.layout.hour_item, My_hour_grid, getActivity());

        hour_grid.setAdapter(hourGridViewAdapter);

        hourGridViewAdapter.notifyDataSetChanged();

        ListView hour_list = rootView.findViewById(R.id.hour_list);

        My_hour_list = new ArrayList<>();

        for (int i = 0; i < 24; i++)
            My_hour_list.add(new My_date_hour(i));

        hourListViewAdapter = new HourViewAdapter(getActivity(), R.layout.hour_item, My_hour_list, getActivity());

        hour_list.setAdapter(hourListViewAdapter);

        hour_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Activity activity = getActivity();
                if (selectedHourView != null) {
                    selectedHourView.findViewById(R.id.hour_textView).setBackgroundColor(Color.WHITE);
                }

                view.findViewById(R.id.hour_textView).setBackgroundColor(Color.CYAN);

                if (My_week_grid.get(i % 7).date > 100) {
                    Toast.makeText(activity, (My_week_grid.get(i % 7).date - 100) + "일 " + i / 7 + "시", Toast.LENGTH_SHORT).show();
                    MainActivity.ClickPoint = year + "." + month + "." + (My_week_grid.get(i % 7).date - 100);
                } else {
                    Toast.makeText(activity, My_week_grid.get(i % 7).date + "일 " + i / 7 + "시", Toast.LENGTH_SHORT).show();
                    MainActivity.ClickPoint = year + "." + month + "." + (My_week_grid.get(i % 7).date);
                }

                Clicktime = i / 7;

                if (test > 0) {
                    My_week_grid.remove(xPosition % 7);
                    if (test > 100)
                        My_week_grid.add(xPosition % 7, new My_date_week(test - 100));
                    else
                        My_week_grid.add(xPosition % 7, new My_date_week(test));

                    weekViewAdapter = new WeekViewAdapter(getActivity(), R.layout.week_item, My_week_grid, getActivity());

                    week_grid.setAdapter(weekViewAdapter);
                }

                test = My_week_grid.get(i % 7).date;
                My_week_grid.remove(i % 7);
                My_week_grid.add(i % 7, new My_date_week(test + 100));

                weekViewAdapter = new WeekViewAdapter(getActivity(), R.layout.week_item, My_week_grid, getActivity());

                week_grid.setAdapter(weekViewAdapter);

                xPosition = i;

                selectedHourView = view;
            }
        });
        return rootView;
    }
}
