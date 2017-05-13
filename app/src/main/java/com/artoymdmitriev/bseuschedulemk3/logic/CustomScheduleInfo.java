package com.artoymdmitriev.bseuschedulemk3.logic;

import com.scheduleparser.parser.ScheduleInfo;

import java.io.Serializable;

/**
 * Created by Artoym on 09.05.2017.
 */

public class CustomScheduleInfo extends ScheduleInfo implements Serializable {
    String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return getFaculty() + " " + getForm() + " " + getCourse() + " " + getGroup();
    }
}
