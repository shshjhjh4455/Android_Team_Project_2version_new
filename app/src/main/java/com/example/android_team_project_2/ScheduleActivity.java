package com.example.android_team_project_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mGoogleMap = null;
    private MyDBHelper mDbHelper;
    Intent intent_load;
    Intent intent_save;
    int key, sHour = 0, eHour = 1;
    int sHour_check = -1, eHour_check = -1;
    int Position;
    String type;
    private final LatLng hansung = new LatLng(37.5822608, 127.0094254);
    private final MarkerOptions marker_hansung = new MarkerOptions().position(hansung);
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); // 구글맵 프래그먼트 생성
        assert mapFragment != null;
        mapFragment.getMapAsync(ScheduleActivity.this);

        intent_load = getIntent(); // 이전 프래그먼트로부터 데이터를 받아온다
        mDbHelper = new MyDBHelper(this); // 데이터베이스 생성

        TimePicker timeStart = findViewById(R.id.timeStart);
        TimePicker timeEnd = findViewById(R.id.timeEnd);

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editPlace = (EditText) findViewById(R.id.editPlace);
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        editTitle.setHint(MainActivity.ClickPoint);
        String[] title = String.valueOf(MainActivity.ClickPoint).split("[.]");
        setTitle(title[0] + "년 " + title[1] + "월 " + title[2] + "일");
        //액티비티 제목을 클릭한 날짜로 표기

        intent_save = new Intent(this, MainActivity.class);

        type = intent_load.getStringExtra("type");
        intent_save.putExtra("type", type);

        if(type.equals("month")) {
            Position = intent_load.getIntExtra("Month_Position", Integer.MAX_VALUE / 2);
            intent_save.putExtra("Month_Position", Position);
        }

        else if(type.equals("week")) {
            Position = intent_load.getIntExtra("Week_Position", Integer.MAX_VALUE / 2);
            intent_save.putExtra("Week_Position", Position);
        }
        //액티비티 실행할 때 받은 정보를 다시 메인액티비티로 전해줘야 하기 때문에 저장

        key = intent_load.getIntExtra("selected", -1);
        //선택된 일정이 있는지 확인

        if (key != -1) {
            Cursor cursor = mDbHelper.getAllUsersByMethod();

            cursor.moveToPosition(key);

            editTitle.setText(cursor.getString(1));
            editPlace.setText(cursor.getString(5));
            editMemo.setText(cursor.getString(6));

            timeStart.setHour(Integer.parseInt(cursor.getString(3)));
            timeStart.setMinute(0);
            sHour = Integer.parseInt(cursor.getString(3));
            sHour_check = sHour;
            timeEnd.setHour(Integer.parseInt(cursor.getString(4)));
            timeEnd.setMinute(0);
            eHour = Integer.parseInt(cursor.getString(4));
            eHour_check = eHour;
            //일정의 제목과 위치 메모 시간을 불러와 화면에 표기 시간을 이용해 일정을 삭제하기 때문에 시간을 따로 저장
        }

        else {
            int time = intent_load.getIntExtra("time", 0);
            sHour = time;
            eHour = time + 1;
            timeStart.setHour(sHour);
            timeStart.setMinute(0);
            timeEnd.setHour(eHour);
            timeEnd.setMinute(0);
            //일정을 선택하지 않은 월간달력의 경우 0시-1시 일정으로 시간을 표기
            //일정을 선택하지 않았지만 주간달력의 경우 시간을 선택해 일정추가 액티비티를 실행하기 때문에 선택한 시간을 불러와 표기
        }

        timeStart.setOnTimeChangedListener((timePicker, h, m) -> {
            sHour = h;
            if (sHour <= 22)
                timeEnd.setHour(h + 1);
            else if (sHour == 23)
                timeEnd.setHour(0);
            timeEnd.setMinute(m);
            //시작시간이 종료시간보다 늦어질 수 없게 작성
        });

        timeEnd.setOnTimeChangedListener((timePicker, h, m) -> {
            eHour = h;
            if (eHour >= 1)
                timeStart.setHour(h - 1);
            else if (eHour == 0)
                timeStart.setHour(23);
            timeEnd.setMinute(m);
            //위와 같음
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> finish()); // 취소 버튼 클릭 시 액티비티를 종료
    }

    @SuppressLint("Range")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) { // 이전 프래그먼트에서 받아온 데이터에 해당하는 데이터베이스에 저장된 주소로 구글맵을 이동
        mGoogleMap = googleMap;
        Cursor cursor = mDbHelper.getAllUsersByMethod();
        if (key == -1) {
            defaultMapReady();
            //선택한 일정이 없는 경우 한성대학교를 지도에 표기
            return;
        }
        cursor.moveToPosition(key);
        String address = cursor.getString(5);

        searchAddress(address);
    }

    public void defaultMapReady() {
        if (marker != null)
            marker.remove();
            //기존에 있던 마커를 삭제

        marker = mGoogleMap.addMarker(marker_hansung);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung, 15));
    }

    //찾기 버튼 클릭시 실행
    public void getAddress(View view) {
        EditText editText = (EditText)findViewById(R.id.editPlace);
        String address = editText.getText().toString();
        if(address.length() > 0)
            searchAddress(address);
        else
            defaultMapReady();
            //입력한 값이 없다면 초기값 한성대로 이동
    }

    //입력한 지명의 위치를 지도에 그리는 함수
    public void searchAddress(String address) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(address,1);
            if (addresses.size() > 0) {
                if (marker != null)
                    marker.remove();
                //입력한 지명의 위치로 이동할 때 기존에 있던 마커를 삭제

                Address Location = (Address) addresses.get(0);
                LatLng location = new LatLng(Location.getLatitude(), Location.getLongitude());
                marker = mGoogleMap.addMarker(new MarkerOptions().position(location).title(address));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Wrong Address!");
                //다이얼로그 제목은 Delete로 표기
                builder.setPositiveButton("OK", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } catch (IOException e) {
            Log.e(getClass().toString(),"Failed in using Geocoder.", e);
            return;
        }
    }

    //저장버튼 클릭시 실행
    public void insertRecord(View view) {
        mDbHelper = new MyDBHelper(this);
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editPlace = (EditText) findViewById(R.id.editPlace);
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        String Title = editTitle.getText().toString();
        String Memo = editMemo.getText().toString();
        String Place = editPlace.getText().toString();

        if (Place.length() == 0)
            Place = "hansung";
            //위치의 변경이 없으면 초기값인 hansung으로 저장

        if(sHour_check != -1 && eHour_check != -1) {
            mDbHelper.delete(MainActivity.ClickPoint,sHour_check + "", eHour_check + "");
            //기존의 일정을 클릭해 일정추가 액티비티를 실행했을 경우 기존의 일정을 삭제하고 저장하여 일정을 수정하는 방식으로 작성
        }

        mDbHelper.insertUserByMethod(Title, MainActivity.ClickPoint, sHour + "", eHour + "", Place, Memo);
        finish();
        startActivity(intent_save);
        //제목, 날짜, 시간, 위치, 메모 저장후 종료 인텐트에 저장한 값을 전달해주며 메인액티비티 실행
    }

    //삭제버튼 클릭시 실행
    public void deleteRecord(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete");
        //다이얼로그 제목은 Delete로 표기
        builder.setPositiveButton("OK", (dialog, id) -> {
            mDbHelper.delete(MainActivity.ClickPoint,sHour + "", eHour + "");
            finish();
            startActivity(intent_save);
            //OK버튼 클릭시 삭제 후 메인액티비티 실행
        });
        builder.setNegativeButton("Cancel", null);
        //Cancel 클릭시 다이얼로그만 종료
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}