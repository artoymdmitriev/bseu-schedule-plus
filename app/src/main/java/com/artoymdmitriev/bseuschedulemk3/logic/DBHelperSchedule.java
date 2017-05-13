package com.artoymdmitriev.bseuschedulemk3.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.scheduleparser.parser.NormalItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artoym on 12.05.2017.
 */

public class DBHelperSchedule extends SQLiteOpenHelper {
    // All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "schedule";

    // Contacts table name
    private static String TABLE_SCHEDULE;

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DAY_OF_WEEK = "day_of_week";
    private static final String KEY_NUMBER_OF_DAY_OF_WEEK = "number_of_day_of_week";
    private static final String KEY_TIME = "time";
    private static final String KEY_NUMBER_OF_WEEK = "number_of_week";
    private static final String KEY_DISCIPLINE = "discipline";
    private static final String KEY_TEACHER = "teacher";
    private static final String KEY_PLACE = "place";
    private static final String KEY_SUBGROUP = "subgroup";
    private static final String KEY_LESSON_TYPE = "lesson_type";

    public DBHelperSchedule(Context context, String tableName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_SCHEDULE = "schedule" + tableName;

        //if we are adding new group, we need to create a table
        //for it. This try-catch block "checks" if the table exists
        //and creates new in case it doesn't.
        try {
            getAllNormalItems();
        } catch (Exception e) {
            onCreate(this.getWritableDatabase());
        }
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_SCHEDULE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DAY_OF_WEEK + " TEXT,"
                + KEY_NUMBER_OF_DAY_OF_WEEK + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_NUMBER_OF_WEEK + " TEXT,"
                + KEY_DISCIPLINE + " TEXT,"
                + KEY_TEACHER + " TEXT,"
                + KEY_PLACE + " TEXT,"
                + KEY_SUBGROUP + " TEXT,"
                + KEY_LESSON_TYPE + " TEXT" + ")";
        System.out.println(CREATE_SCHEDULE_TABLE);
        db.execSQL(CREATE_SCHEDULE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addNormalItem(NormalItem normalItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DAY_OF_WEEK, normalItem.getDayOfWeek());
        values.put(KEY_NUMBER_OF_DAY_OF_WEEK, normalItem.getNumberOfDayOfWeek());
        values.put(KEY_TIME, normalItem.getTime());
        values.put(KEY_NUMBER_OF_WEEK, normalItem.getNumberOfWeek());
        values.put(KEY_DISCIPLINE, normalItem.getDiscipline());
        values.put(KEY_TEACHER, normalItem.getTeacher());
        values.put(KEY_PLACE, normalItem.getPlace());
        values.put(KEY_SUBGROUP, normalItem.getSubgroup());
        values.put(KEY_LESSON_TYPE, normalItem.getLessonType());

        // Inserting Row
        db.insert(TABLE_SCHEDULE, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<NormalItem> getAllNormalItems() {
        List<NormalItem> normalItemsList = new ArrayList<NormalItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SCHEDULE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NormalItem normalItem = new NormalItem();
                normalItem.setDayOfWeek(cursor.getString(1));
                normalItem.setNumberOfDayOfWeek(Integer.parseInt(cursor.getString(2)));
                normalItem.setTime(cursor.getString(3));
                normalItem.setNumberOfWeek(Integer.parseInt(cursor.getString(4)));
                normalItem.setDiscipline(cursor.getString(5));
                normalItem.setTeacher(cursor.getString(6));
                normalItem.setPlace(cursor.getString(7));
                normalItem.setSubgroup(cursor.getString(8));
                normalItem.setLessonType(cursor.getString(9));
                // Adding contact to list
                normalItemsList.add(normalItem);
            } while (cursor.moveToNext());
        }

        // return contact list
        return normalItemsList;
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SCHEDULE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void dropTable(String tableName) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_SCHEDULE;

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DROP_TABLE);
    }

}
