package com.aljazarisoft.apps.luckyegg.lib;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.Toast;

import com.aljazarisoft.apps.luckyegg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * Created by atak on 15.04.2017.
 */

public class Tools {

    public ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray){

        ArrayList<JSONObject> aList=new ArrayList<JSONObject>();

        try {


            if (jsonArray != null) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    aList.add(jsonArray.getJSONObject(i));

                }


            }

        }catch (JSONException je){je.printStackTrace();}

        return  aList;

    }
    public void makeAlert(final Context ctn, String... dialog){
        final String letsgo = dialog[4];
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(new ContextThemeWrapper(ctn, R.style.myDialog));
 alertDialog2.setCancelable(false);
// Setting Dialog Title
        alertDialog2.setTitle(dialog[0]);

// Setting Dialog Message
        alertDialog2.setMessage(dialog[1]);

// Setting Icon to Dialog
    //    alertDialog2.setIcon(R.drawable.delete);

// Setting Positive "Yes" Btn
     if(dialog[2]==null){

     }else{
         alertDialog2.setPositiveButton(dialog[2],
                 new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                         // Write your code here to execute after dialog
                         if(letsgo.equals("ACTION_LOCATION_SOURCE_SETTINGS")){
                             Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                             ctn.startActivity(intent);
                         }
                         System.exit(0);

                     }
                 });
     }
        if( dialog[3]==null){

        }else{
            // Setting Negative "NO" Btn
            alertDialog2.setNegativeButton(dialog[3],
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            Toast.makeText(ctn,
                                    "You clicked on NO", Toast.LENGTH_SHORT)
                                    .show();
                            dialog.cancel();
                        }
                    });
        }

// Showing Alert Dialog
        alertDialog2.show();
    }
    public boolean isJSONObject(String JSonString){
        Object json = null;
        boolean result=false;

        try {
            json = new JSONTokener(JSonString).nextValue();
            if (json instanceof JSONObject)
              result=true;
            else if (json instanceof JSONArray);
            result=false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
      return result;
    }

    public void push(Context ctn){

}
}
