package com.aljazarisoft.apps.luckyegg.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aljazarisoft.apps.luckyegg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConversationsAdapter extends ArrayAdapter<JSONObject>{

    int vg;

    ArrayList<JSONObject> list;

    Context context;

    public ConversationsAdapter(Context context, int vg, int id, ArrayList<JSONObject> list){

        super(context,vg, id,list);

        this.context=context;

        this.vg=vg;

        this.list=list;


    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(vg, parent, false);

        TextView Inbox_text_user=(TextView)itemView.findViewById(R.id.Inbox_text_user);

        TextView Inbox_text_time=(TextView)itemView.findViewById(R.id.Inbox_text_time);

        TextView Inbox_text_message=(TextView)itemView.findViewById(R.id.Inbox_text_message);

        try {

            Inbox_text_user.setText(list.get(position).getString("Conversation_User"));

            Inbox_text_time.setText(list.get(position).getString("Conversation_Time"));

            Inbox_text_message.setText(list.get(position).getString("Conversation_Message"));



        } catch (JSONException e) {

            e.printStackTrace();

        }



        return itemView;

    }

}