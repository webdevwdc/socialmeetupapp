package com.nationality.utils;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.nationality.model.MeetupDetailsRespond;

import java.util.List;

/**
 * Created by webskitters on 4/21/2017.
 */




public class TrackAppliation extends MultiDexApplication {

    List<MeetupDetailsRespond> meet_resp;

    public List<MeetupDetailsRespond> getMeet_resp() {
        return meet_resp;
    }

    public void setMeet_resp(List<MeetupDetailsRespond> meet_resp) {
        this.meet_resp = meet_resp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }



}
