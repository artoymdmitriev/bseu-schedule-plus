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

public class SelectGroupActivity extends AppCompatActivity implements ResultsListener {
    NumberPicker numberPicker;
    CustomScheduleInfo customScheduleInfo;
    HashMap<Integer, String> hashMap;
    String[] groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_select_group);

        Intent i = getIntent();
        customScheduleInfo = (CustomScheduleInfo) i.getSerializableExtra("CustomScheduleInfo");
        numberPicker = (NumberPicker) findViewById(R.id.groups_numberpicker);
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


        //trims group names and lefts only it's code name e.g. "15ДКИ"
        HashMap<Integer, String> hashMap2 = new HashMap<>();
        for(Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            hashMap2.put(entry.getKey(), entry.getValue().substring(0, 8));
        }
        hashMap = hashMap2;
        //stop

        groups = new String[hashMap.size()];
        int i = 0;
        for(Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            groups[i] = entry.getValue();
            i++;
        }

        numberPicker.setDisplayedValues(groups);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(groups.length - 1);
        //numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(0);

    }

    private void parseNewItems() {
        AsyncParser parser = new AsyncParser();
        parser.setResultsListener(this);
        parser.execute(customScheduleInfo);
    }

    public void groupChosen(View v) {
        Intent intent = new Intent(getBaseContext(), DownloadActivity.class);

        int num = numberPicker.getValue();
        String chosenGroupName = "";
        for(int i = 0; i < groups.length; i++) {
            if(i == num) {
                chosenGroupName = groups[i];
                customScheduleInfo.setGroupName(chosenGroupName);
            }
        }

        for(Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            if (entry.getValue().equals(chosenGroupName)) {
                customScheduleInfo.setGroup(entry.getKey());
                System.out.println(entry.getValue());
            }
        }

        intent.putExtra("CustomScheduleInfo", customScheduleInfo);
        System.out.println(customScheduleInfo.toString());
        startActivity(intent);
    }
}
