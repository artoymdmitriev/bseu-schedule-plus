package com.artoymdmitriev.bseuschedulemk3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artoymdmitriev.bseuschedulemk3.R;
import com.artoymdmitriev.bseuschedulemk3.logic.AsyncParser;
import com.artoymdmitriev.bseuschedulemk3.logic.CustomScheduleInfo;
import com.artoymdmitriev.bseuschedulemk3.logic.DBHelper;
import com.artoymdmitriev.bseuschedulemk3.logic.DBHelperSchedule;
import com.artoymdmitriev.bseuschedulemk3.logic.ResultsListener;
import com.scheduleparser.parser.NormalItem;

import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity implements ResultsListener {
    TextView textView;
    ProgressBar progressBar;
    ImageView imageView;
    RelativeLayout relativeLayout;
    CustomScheduleInfo customScheduleInfo;
    ArrayList<NormalItem> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_download);

        Intent i = getIntent();
        customScheduleInfo = (CustomScheduleInfo) i.getSerializableExtra("CustomScheduleInfo");

        textView = (TextView) findViewById(R.id.textView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        parseNewItems();
    }

    @Override
    public void onResultSucceeded(Object obj) {
        relativeLayout.removeView(progressBar);

        textView.setText("Расписание загружено!");

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.success);

        arrayList = (ArrayList) obj;

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.addScheduleInfo(customScheduleInfo);

        DBHelperSchedule dbHelperSchedule = new DBHelperSchedule(this, "" + customScheduleInfo.getGroup());
        for (NormalItem normalItem : arrayList) {
            dbHelperSchedule.addNormalItem(normalItem);
        }
    }

    private void parseNewItems() {
        AsyncParser parser = new AsyncParser();
        parser.setResultsListener(this);
        parser.execute(customScheduleInfo);
    }

    public void showSchedule(View v) {
        Intent intent = new Intent(getBaseContext(), FullSchedule.class);
        intent.putExtra("Schedule", arrayList);
        System.out.println(customScheduleInfo.toString());
        startActivity(intent);
    }
}
