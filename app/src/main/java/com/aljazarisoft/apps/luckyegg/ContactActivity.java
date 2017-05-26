package com.aljazarisoft.apps.luckyegg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aljazarisoft.apps.luckyegg.Adapter.ContactAdapter;
import com.aljazarisoft.apps.luckyegg.lib.HttpAsyncTask;
import com.aljazarisoft.apps.luckyegg.lib.Tools;
import com.aljazarisoft.apps.luckyegg.lib.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ContactActivity extends AppCompatActivity {
 String Data=null;
    private String URL =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getContect();
    }

    protected void getContect(){

        HashMap<String,String> map = new HashMap<String,String>();
        try {
            this.URL=new Url().Url("Contact");
            this.Data= (String) new HttpAsyncTask(ContactActivity.this,map).execute(this.URL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(Data==null)
        {

        }else {




            try {


                final JSONArray andro =  new JSONArray(this.Data);
                ListView Clist =(ListView)findViewById(R.id.ContactList);
                final ArrayList<JSONObject> listContact=new Tools().getArrayListFromJSONArray(andro);

                ContactAdapter adapter=new ContactAdapter(this, R.layout.activity_contact_contact, R.id.Contact_user_picture,listContact);

                Clist.setAdapter(adapter);
                Clist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        String product = "";
                        try {
                            product = listContact.get(position).getString("Contact.User.Id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent menuIntent = new Intent(ContactActivity.this, TextingActivity.class);
                        menuIntent.putExtra("Contact.User.Id", product);
                        startActivity(menuIntent);


                        //   Toast.makeText(getBaseContext(),  "you clicked position ="+position, Toast.LENGTH_LONG).show();

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        }

}
