package com.example.android_team_project_2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthFragment extends Fragment {
    static int page = 0;
    int year, month, dow, lastdate, xdate;
    MonthViewAdapter adapter;
    ArrayList<My_date_month> My_cal;
    View selectedView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_month, container, false);

        Bundle bundle = getArguments();

        assert bundle != null;
        page = Integer.MAX_VALUE / 2 - bundle.getInt("position");

        GridView gridView = rootView.findViewById(R.id.month_grid);

        My_cal = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;

        calendar.set(year, month - 1 - page, 1);

        dow = calendar.get(Calendar.DAY_OF_WEEK);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        lastdate = calendar.getActualMaximum(Calendar.DATE);

        requireActivity().setTitle(year + "년 " + month + "월");

        calendar.set(year, month - 2, 1);

        xdate = calendar.getActualMaximum(Calendar.DATE);

        for (int l = dow - 2; l >= 0; l--)
            My_cal.add(new My_date_month(xdate - l));

        for (int i = 1; i <= lastdate; i++)
            My_cal.add(new My_date_month(i));

        calendar.set(year, month - 1, lastdate);

        if (My_cal.size() <= 35) {
            for (int k = 1; k <= 14 - calendar.get(Calendar.DAY_OF_WEEK); k++) {
                My_cal.add(new My_date_month(k));
            }
        } else {
            for (int k = 1; k <= 7 - calendar.get(Calendar.DAY_OF_WEEK); k++) {
                My_cal.add(new My_date_month(k));
            }
        }

        adapter = new MonthViewAdapter(getActivity(), R.layout.month_item, My_cal, getActivity());

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            Activity activity = getActivity();
            if (selectedView != null) {
                selectedView.findViewById(R.id.month_textView).setBackgroundColor(Color.WHITE);
            }
            if (position >= dow - 1 && position < lastdate + dow - 1) {
                Toast.makeText(activity, year + "." + month + "." + (position - dow + 2), Toast.LENGTH_SHORT).show();
                view.findViewById(R.id.month_textView).setBackgroundColor(Color.CYAN);
                MainActivity.ClickPoint = year + "." + month + "." + (position - dow + 2);
            } else {
                MainActivity.ClickPoint = "";
                return;
            }
            selectedView = view;
        });
        return rootView;
    }
}

