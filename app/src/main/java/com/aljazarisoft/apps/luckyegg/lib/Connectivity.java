package com.aljazarisoft.apps.luckyegg.lib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by atak on 30.04.2017.
 */

public class Connectivity {

    public static Boolean checknetwork(Context mContext) {

        NetworkInfo info = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected())
        {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable internet while roaming, just return false
            return true;
        }

        return true;

    }
}


