package com.duke.random.randommachine;

import android.app.Application;

import com.lxj.xpopup.XPopup;
import com.tencent.mmkv.MMKV;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String rootDir = MMKV.initialize(this);
        System.out.println("mmkv root: " + rootDir);
        XPopup.setPrimaryColor(getResources().getColor(R.color.colorPrimary));
    }
}
