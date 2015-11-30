package com.nancy.daycounter.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by nan.zhang on 11/30/15.
 */
public class DayCounter {

    public DayCounter() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

//    public void setId(UUID id) {
//        mId = id;
//    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getWhat() {
        return mWhat;
    }

    public void setWhat(String what) {
        mWhat = what;
    }


    private UUID mId;

    private Date mDate;

    private String mWhat;
}
