package com.artoymdmitriev.bseuschedulemk3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;

import com.artoymdmitriev.bseuschedulemk3.R;
import com.artoymdmitriev.bseuschedulemk3.logic.AsyncParser;
import com.artoymdmitriev.bseuschedulemk3.logic.CustomScheduleInfo;
import com.artoymdmitriev.bseuschedulemk3.logic.ResultsListener;

import java.util.HashMap;
import java.util.Map;

public class SelectCourseActivity extends AppCompatActivity implements ResultsListener{
    NumberPicker numberPicker;
    CustomScheduleInfo customScheduleInfo;
    HashMap<Integer, String> hashMap;
    String[] courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_select_course);

        Intent i = getIntent();
        customScheduleInfo = (CustomScheduleInfo) i.getSerializableExtra("CustomScheduleInfo");
        numberPicker = (NumberPicker) findViewById(R.id.courses_numberpicker);
        String[] downloading = {"Загрузка..."};
        numberPicker.setDisplayedValues(downloading);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(downloading.length - 1);
        numberPicker.setValue(0);

        parseNewItems();

    }

    @Override
    public void onResultSucceeded(Object obj) {
        hashMap = (HashMap) obj;

        courses = new String[hashMap.size()];
        int i = 0;
        for(Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            courses[i] = entry.getValue();
            i++;
        }

        numberPicker.setDisplayedValues(courses);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(courses.length - 1);
        //numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(0);
    }

    private void parseNewItems() {
        AsyncParser parser = new AsyncParser();
        parser.setResultsListener(this);
        parser.execute(customScheduleInfo);
    }

    public void courseChosen(View v) {
        Intent intent = new Intent(getBaseContext(), SelectGroupActivity.class);

        int num = numberPicker.getValue();
        String chosenCourseName = "";
        for(int i = 0; i < courses.length; i++) {
            if(i == num) {
                chosenCourseName = courses[i];
            }
        }

        for(Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            if (entry.getValue().equals(chosenCourseName)) {
                customScheduleInfo.setCourse(entry.getKey());
            }
        }

        intent.putExtra("CustomScheduleInfo", customScheduleInfo);
        System.out.println(customScheduleInfo.toString());
        startActivity(intent);
    }
}
