package com.artoymdmitriev.bseuschedulemk3.logic;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Artoym on 12.05.2017.
 */

public class DateCalc {
    Date date1;
    Date date2;

    public DateCalc(Date date1, Date date2) {
        this.date1 = date1;
        this.date2 = date2;
    }

    public long getWeeksBetween() {
        return getDateDiff(date1, date2, TimeUnit.DAYS) / 7;
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
