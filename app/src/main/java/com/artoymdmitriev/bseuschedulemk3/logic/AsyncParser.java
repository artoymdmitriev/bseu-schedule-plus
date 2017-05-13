package com.artoymdmitriev.bseuschedulemk3.logic;

import android.os.AsyncTask;

import com.scheduleparser.parser.NormalItem;
import com.scheduleparser.parser.ParseNormalizer;
import com.scheduleparser.parser.ParsedItem;
import com.scheduleparser.parser.Parser;
import com.scheduleparser.parser.ScheduleInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Artoym on 09.05.2017.
 */

public class AsyncParser extends AsyncTask<ScheduleInfo, Void, Object> {
    ResultsListener resultsListener;
    Parser parser = new Parser();

    @Override
    protected Object doInBackground(ScheduleInfo... scheduleInfos) {
        HashMap<Integer, String> hashMap = null;
        ArrayList<ParsedItem> parsedItems = null;
        ScheduleInfo scheduleInfo = scheduleInfos[0];
        if (scheduleInfo.getFaculty() == 0) {
            hashMap = parser.getFaculties();
            return hashMap;
        } else if (scheduleInfo.getForm() == 0) {
            hashMap = parser.getForms(scheduleInfo);
            return hashMap;
        } else if (scheduleInfo.getCourse() == 0) {
            hashMap = parser.getCourses(scheduleInfo);
            return hashMap;
        } else if (scheduleInfo.getGroup() == 0) {
            hashMap = parser.getGroups(scheduleInfo);
            return hashMap;
        } else {
            try {
                parsedItems = parser.getSchedule(scheduleInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ParseNormalizer parseNormalizer = new ParseNormalizer(parsedItems);
            ArrayList<NormalItem> normalItems = parseNormalizer.normalize();
            return normalItems;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        resultsListener.onResultSucceeded(o);
    }

    public void setResultsListener(ResultsListener resultsListener) {
        this.resultsListener = resultsListener;
    }
}
