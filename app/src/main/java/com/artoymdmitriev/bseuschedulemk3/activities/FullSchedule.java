package com.artoymdmitriev.bseuschedulemk3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.artoymdmitriev.bseuschedulemk3.R;
import com.artoymdmitriev.bseuschedulemk3.adapters.ScheduleItemAdapter;
import com.artoymdmitriev.bseuschedulemk3.logic.CustomScheduleInfo;
import com.artoymdmitriev.bseuschedulemk3.logic.DBHelper;
import com.artoymdmitriev.bseuschedulemk3.logic.DBHelperSchedule;
import com.artoymdmitriev.bseuschedulemk3.logic.DateCalc;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.scheduleparser.parser.NormalItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FullSchedule extends AppCompatActivity {
    private ListView listView;
    long week;
    int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_schedule);

        /*Intent intent = new Intent(this, WeekScheduleActivity.class);
        startActivity(intent);*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));


        //setting up action drawer
        DrawerBuilder drawerBuilder = new DrawerBuilder();
        Drawer result = drawerBuilder
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withTranslucentNavigationBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .build();

        DBHelper dbHelper = new DBHelper(this);
        ArrayList<CustomScheduleInfo> arrayList = (ArrayList<CustomScheduleInfo>) dbHelper.getAllScheduleInfo();
        for (CustomScheduleInfo customScheduleInfo : arrayList) {
            result.addItem(new PrimaryDrawerItem().withName(customScheduleInfo.getGroupName())
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            showSchedule("" + customScheduleInfo.getGroup());
                            toolbar.setTitle(customScheduleInfo.getGroupName());
                            result.closeDrawer();
                            return true;
                        }
                    }));
        }
        result.addItem(new PrimaryDrawerItem().withName("Загрузить расписание").withIcon(android.R.drawable.stat_sys_download).
                withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        startDownloadActivity();
                        return true;
                    }
                }));
        //end of setting up action drawer

        //loading the first schedule so that the activity won't be empty on the first start
        String groupName = arrayList.get(0).getGroupName();
        int groupID = arrayList.get(0).getGroup();

        getSupportActionBar().setTitle(groupName);
        showSchedule("" + groupID);
        //end of loading the schedule
    }

    private void startDownloadActivity() {
        Intent intent = new Intent(this, SelectFacultyActivity.class);
        startActivity(intent);
    }

    private void showSchedule(String groupNumber) {
        getWeeksBetween();
        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        DBHelperSchedule dbHelperSchedule = new DBHelperSchedule(this, groupNumber);
        ArrayList<NormalItem> normalItems = (ArrayList) dbHelperSchedule.getAllNormalItems();
        ArrayList<NormalItem> scheduleForTheDay = new ArrayList<>();

        for (NormalItem normalItem : normalItems) {
            if (week == normalItem.getNumberOfWeek() && day == normalItem.getNumberOfDayOfWeek()) {
                scheduleForTheDay.add(normalItem);
            }
        }

        listView = (ListView) findViewById(R.id.listview);
        ScheduleItemAdapter itemsAdapter =
                new ScheduleItemAdapter(this, R.layout.list_item, scheduleForTheDay);
        listView.setAdapter(itemsAdapter);
    }

    private void getWeeksBetween() {

        Date endDate = new Date();
        Date startDate;

        if (endDate.getMonth() > 8) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            startDate = new Date(year - 1900, 8, 1);
        } else {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            startDate = new Date(year - 1900, 1, 6);
        }

        DateCalc dateCalc = new DateCalc(startDate, endDate);

        //ATTENTION!! Probably, the index of the first week is 0 (not shure),
        //so we are adding +1
        week = dateCalc.getWeeksBetween() + 1;
    }
}
