package com.aljazarisoft.apps.luckyegg.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aljazarisoft.apps.luckyegg.R;
import com.aljazarisoft.apps.luckyegg.lib.HttpAsyncTask;
import com.aljazarisoft.apps.luckyegg.lib.Session;
import com.aljazarisoft.apps.luckyegg.lib.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class TextingAdapter extends ArrayAdapter<JSONObject> {
    int vg;
private boolean it =false;
    private String user;
    private Activity activity;
    private ArrayList<JSONObject> messages;
    private Session session;
    HttpAsyncTask task;
    String GoUrl;
    public TextingAdapter(Activity context, int resource, ArrayList<JSONObject> objects){
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
        GoUrl= new Url().Url("MessageRead");
        this.session =new Session(this.activity);

    }



    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int layoutResource = 0; // determined by view type
        String ali=null;
        try {
            this.user=messages.get(position).getString("Conversation_User");

            if(this.user.equals(session.get("nick")))
            {
                layoutResource = R.layout.item_chat_left;
            }
            else{
                if(messages.get(position).getString("Conversation_Rid").equals("0")){
                    HashMap<String ,String> h = new HashMap<>();
                    h.put("rid",""+messages.get(position).getInt("Conversation_Message_Id"));
                 task = new HttpAsyncTask(activity,h);
                  ali = task.execute(GoUrl).get();


                }
                layoutResource = R.layout.item_chat_right;
            }








            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = inflater.inflate(layoutResource, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder.msg.setText(messages.get(position).getString("Conversation_Message"));
            holder.user.setText(messages.get(position).getString("Conversation_User"));


        } catch (JSONException e) {

            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return convertView;

    }
    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }



    private class ViewHolder {
        private TextView msg;
        private TextView user;

        public ViewHolder(View v) {

      user = (TextView) v.findViewById(R.id.Message_textview_user);
            msg = (TextView) v.findViewById(R.id.txt_msg);
        }
    }

    private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray){

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
}