package com.aljazarisoft.apps.luckyegg.lib;

import android.content.Context;

/**
 * Created by atak on 10.04.2017.
 */

public class Url  {
    private String URL;
    Context context;



    public String Url(String... type){


         URL="http://luckyegg.apps.aljazarisoft.com/";
        if(type[0]=="Login")
            URL =URL+"user/login/" ;
        else if(type[0]=="Signup")
            URL= URL+"user/signup/" ;
        else if(type[0]=="Profile.Update")
            URL= URL+"user/profile/update/" ;
        else if(type[0]=="Profile")
            URL= URL+"user/profile";
        else if(type[0]=="Inbox")
            URL= URL+"message/inbox/" ;
        else if(type[0]=="Contact")
            URL= URL+"message/contact/" ;
        else if(type[0]=="Conversation")
            URL= URL+"message/Conversation/";
        else if(type[0]=="Item")
            URL= URL+"item/near/";
        else if(type[0]=="AddItem")
            URL= URL+"item/save/";
        else if(type[0]=="ItemFound")
            URL= URL+"item/found/";
        else if(type[0]=="Notification")
            URL= URL+"notification/";
        else if(type[0]=="MessageRead")
            URL= URL+"message/read/";


        return URL;

    }

}
