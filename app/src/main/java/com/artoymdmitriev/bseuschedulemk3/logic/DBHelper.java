package com.artoymdmitriev.bseuschedulemk3.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artoym on 12.05.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "scheduleInfo";

    // Contacts table name
    private static final String TABLE_INFO = "schedules";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TABLE_NAME = "table_name";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCHEDULE_INFO_TABLE = "CREATE TABLE " + TABLE_INFO + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_TABLE_NAME + " TEXT" + ")";
        System.out.println(CREATE_SCHEDULE_INFO_TABLE);
        db.execSQL(CREATE_SCHEDULE_INFO_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addScheduleInfo(CustomScheduleInfo customScheduleInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, customScheduleInfo.getGroupName()); // Contact Name
        values.put(KEY_TABLE_NAME, "schedule" + customScheduleInfo.getGroup()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_INFO, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public CustomScheduleInfo getScheduleInfo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INFO, new String[] { KEY_ID,
                        KEY_NAME, KEY_TABLE_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CustomScheduleInfo scheduleInfo = new CustomScheduleInfo();
        scheduleInfo.setGroupName(cursor.getString(1));
        scheduleInfo.setGroup(Integer.parseInt(cursor.getString(2)));
        // return contact
        return scheduleInfo;
    }

    // Getting All Contacts
    public List<CustomScheduleInfo> getAllScheduleInfo() {
        List<CustomScheduleInfo> scheduleInfoList = new ArrayList<CustomScheduleInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CustomScheduleInfo scheduleInfo = new CustomScheduleInfo();
                scheduleInfo.setGroupName(cursor.getString(1));
                String str = cursor.getString(2);
                scheduleInfo.setGroup(Integer.parseInt(str.substring(8)));
                // Adding contact to list
                scheduleInfoList.add(scheduleInfo);
            } while (cursor.moveToNext());
        }

        // return contact list
        return scheduleInfoList;
    }

    // Deleting single contact
    public void deleteScheduleInfo(CustomScheduleInfo scheduleInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INFO, KEY_TABLE_NAME + " = ?",
                new String[] { scheduleInfo.getGroupName() });
        db.close();
    }


    // Getting contacts Count
    public int getCustomScheduleInfoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int index = cursor.getCount();
        cursor.close();

        // return count
        return index;
    }
}
