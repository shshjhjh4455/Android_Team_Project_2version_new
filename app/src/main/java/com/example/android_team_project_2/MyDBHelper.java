package com.example.android_team_project_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
    final static String TAG = "SQLiteDBTest";

    public MyDBHelper(Context context) {
        super(context, UserContract.DB_NAME, null, UserContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, getClass().getName() + ".onCreate()");
        db.execSQL(UserContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG, getClass().getName() + ".onUpgrade()");
        db.execSQL(UserContract.Users.DELETE_TABLE);
        onCreate(db);
    }

    public void insertUserBySQL(String title, String date, String s_time, String e_time, String place_x, String place_y, String memo) {
        try {
            String sql = String.format(
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s')",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    UserContract.Users.KEY_TITLE,
                    UserContract.Users.KEY_DATE,
                    UserContract.Users.KEY_START_TIME,
                    UserContract.Users.KEY_END_TIME,
                    UserContract.Users.KEY_PLACE_X,
                    UserContract.Users.KEY_PLACE_Y,
                    UserContract.Users.KEY_MEMO,
                    title,
                    date,
                    s_time,
                    e_time,
                    place_x,
                    place_y,
                    memo);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG, "Error in inserting recodes");
        }
    }

    public Cursor getAllUsersBySQL() {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql, null);
    }

    // 시간정보 없이 데이터베이스를 호출할 때 사용하는 함수
    public Cursor getDayUsersBySQL(String scheduleYear, String scheduleMonth, String scheduleDay) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME + " WHERE year= '" + scheduleYear + "' AND Month= '" + scheduleMonth + "' AND Day= '" + scheduleDay + "'";
        return getReadableDatabase().rawQuery(sql, null);
    }

    public void deleteUserBySQL(String _id) {
        try {
            String sql = String.format(
                    "DELETE FROM %s WHERE %s = %s",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    _id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG, "Error in deleting recodes");
        }
    }

    public void updateUserBySQL(String _id, String title, String date, String s_time, String e_time, String place_x, String place_y, String memo) {
        try {
            String sql = String.format(
                    "UPDATE  %s SET %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s' WHERE %s = %s",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users.KEY_TITLE, title,
                    UserContract.Users.KEY_DATE, date,
                    UserContract.Users.KEY_START_TIME, s_time,
                    UserContract.Users.KEY_END_TIME, e_time,
                    UserContract.Users.KEY_PLACE_X, place_x,
                    UserContract.Users.KEY_PLACE_Y, place_y,
                    UserContract.Users.KEY_MEMO, memo,
                    UserContract.Users._ID, _id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG, "Error in updating recodes");
        }
    }

    public long insertUserByMethod(String title, String date, String s_time, String e_time, String place_x, String place_y, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.Users.KEY_TITLE, title);
        values.put(UserContract.Users.KEY_DATE, date);
        values.put(UserContract.Users.KEY_START_TIME, s_time);
        values.put(UserContract.Users.KEY_END_TIME, e_time);
        values.put(UserContract.Users.KEY_PLACE_X, place_x);
        values.put(UserContract.Users.KEY_PLACE_Y, place_y);
        values.put(UserContract.Users.KEY_MEMO, memo);

        return db.insert(UserContract.Users.TABLE_NAME, null, values);
    }

    public Cursor getAllUsersByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(UserContract.Users.TABLE_NAME, null, null, null, null, null, null);
    }

    public long deleteUserByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = UserContract.Users._ID + " = ?";
        String[] whereArgs = {_id};
        return db.delete(UserContract.Users.TABLE_NAME, whereClause, whereArgs);
    }

    public void delete(String date, String s_time, String e_time) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + UserContract.Users.TABLE_NAME +
                " WHERE " + UserContract.Users.KEY_DATE + " = '" + date +
                "' AND " + UserContract.Users.KEY_START_TIME + " = '" + s_time +
                "' AND " + UserContract.Users.KEY_END_TIME + " = '" + e_time + "';");
    }

    public Cursor searchMonth(String searchDate) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM Users WHERE Date LIKE ?");

        SQLiteDatabase db = getReadableDatabase();
        String[] params = {searchDate + "%"};
        Cursor cursor = db.rawQuery(sb.toString(), params);

        return cursor;
    }

    public long updateUserByMethod(String _id, String title, String date, String s_time, String e_time, String place_x, String place_y, String memo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.Users.KEY_TITLE, title);
        values.put(UserContract.Users.KEY_DATE, date);
        values.put(UserContract.Users.KEY_START_TIME, s_time);
        values.put(UserContract.Users.KEY_END_TIME, e_time);
        values.put(UserContract.Users.KEY_PLACE_X, place_x);
        values.put(UserContract.Users.KEY_PLACE_Y, place_y);
        values.put(UserContract.Users.KEY_MEMO, memo);

        String whereClause = UserContract.Users._ID + " = ?";
        String[] whereArgs = {_id};

        return db.update(UserContract.Users.TABLE_NAME, values, whereClause, whereArgs);
    }


    // 시간정보를 포함하여 데이터베이스를 호출할 때 사용하는 함수
    public Cursor getHourUsersBySQL(String scheduleYear, String scheduleMonth, String scheduleDay, String scheduleHour) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME + " WHERE year= '"
                + scheduleYear + "' AND Month= '" + scheduleMonth + "' AND Day= '" + scheduleDay + "' AND StartTime= '" + scheduleHour + "'";
        return getReadableDatabase().rawQuery(sql, null);
    }
}

final class UserContract {
    public static final String DB_NAME = "user.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {
    }

    /* Inner class that defines the table contents */
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String KEY_TITLE = "Title";
        public static final String KEY_DATE = "Date";
        public static final String KEY_START_TIME = "Start_time";
        public static final String KEY_END_TIME = "End_time";
        public static final String KEY_PLACE_X = "Place_x";
        public static final String KEY_PLACE_Y = "Place_y";
        public static final String KEY_MEMO = "Memo";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_TITLE + TEXT_TYPE + COMMA_SEP +
                KEY_DATE + TEXT_TYPE + COMMA_SEP +
                KEY_START_TIME + TEXT_TYPE + COMMA_SEP +
                KEY_END_TIME + TEXT_TYPE + COMMA_SEP +
                KEY_PLACE_X + TEXT_TYPE + COMMA_SEP +
                KEY_PLACE_Y + TEXT_TYPE + COMMA_SEP +
                KEY_MEMO + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}