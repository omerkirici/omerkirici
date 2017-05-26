package com.aljazarisoft.apps.luckyegg.lib;

import android.content.Context;

public class Session {
 private Context context;

    private SecurePreferences preferences;

    public Session(Context cntx) {
        this.context=cntx;
        preferences  = new SecurePreferences(cntx, "SecurePreferences",
                "SometopSecretKey1235", true);


    }

    public void set(String name, String value) {

        preferences.put(name, value);


    }

    public String get(String name) {
   String returned = preferences.getString(name);
        if(returned==null)
        return "";
        else
            return returned;
    }

    public  boolean  exit(){
     preferences.clear();

return true;
    }
}