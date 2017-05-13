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

public class SelectFacultyActivity extends AppCompatActivity implements ResultsListener {
    NumberPicker numberPicker;
    CustomScheduleInfo scheduleInfo = new CustomScheduleInfo();
    HashMap<Integer, String> hashMap;
    String[] faculties;
    Intent intent;

    @Override
    public void onBackPressed() {
        String action = "";
        try {
            action += intent.getAction();
        } catch (Exception e) {

        }
        if(action.equals("StartActivity")) {

        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_select_faculty);

        intent = getIntent();

        numberPicker = (NumberPicker) findViewById(R.id.faculties_numberpicker);

        String[] downloading = {"Загрузка..."};
        numberPicker.setDisplayedValues(downloading);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(downloading.length - 1);
        numberPicker.setValue(0);
        parseNewItems();
    }

    private void parseNewItems() {
        AsyncParser parser = new AsyncParser();
        parser.setResultsListener(this);
        parser.execute(scheduleInfo);
    }

    @Override
    public void onResultSucceeded(Object obj) {
        hashMap = (HashMap) obj;

        faculties = new String[hashMap.size()];
        int i = 0;
        for(Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            faculties[i] = entry.getValue();
            i++;
        }
        numberPicker.setDisplayedValues(faculties);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(faculties.length - 1);
        //numberPicker.setWrapSelectorWheel(false);

        numberPicker.setValue(0);
    }

    public void facultyChosen(View v) {
        Intent intent = new Intent(getBaseContext(), SelectFormActivity.class);

        int num = numberPicker.getValue();
        String chosenFacultyName = "";
        for(int i = 0; i < faculties.length; i++) {
            if(i == num) {
                chosenFacultyName = faculties[i];
            }
        }

        for(Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            if (entry.getValue().equals(chosenFacultyName)) {
                scheduleInfo.setFaculty(entry.getKey());
            }
        }

        intent.putExtra("CustomScheduleInfo", scheduleInfo);
        System.out.println(scheduleInfo.toString());
        startActivity(intent);
    }
}
