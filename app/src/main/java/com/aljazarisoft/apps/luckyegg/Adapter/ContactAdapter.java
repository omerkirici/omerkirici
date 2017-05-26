package com.aljazarisoft.apps.luckyegg.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aljazarisoft.apps.luckyegg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<JSONObject> {

    int vg;

    ArrayList<JSONObject> list;

    Context context;

    public ContactAdapter(Context context, int vg, int id, ArrayList<JSONObject> list){

        super(context,vg, id,list);

        this.context=context;

        this.vg=vg;

        this.list=list;

    }



    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(vg, parent, false);


 

        try {
            TextView contact=(TextView)itemView.findViewById(R.id.Contact_Textview_user);
            ImageView img =(ImageView)itemView.findViewById(R.id.Contact_user_picture);





            contact.setText(list.get(position).getString("Contact.User"));
            String product =list.get(position).getString("Contact.Id");


        } catch (JSONException e) {

            e.printStackTrace();

        }



        return itemView;

    }

}