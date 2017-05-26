package com.aljazarisoft.apps.luckyegg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aljazarisoft.apps.luckyegg.Adapter.ConversationsAdapter;
import com.aljazarisoft.apps.luckyegg.lib.HttpAsyncTask;
import com.aljazarisoft.apps.luckyegg.lib.Session;
import com.aljazarisoft.apps.luckyegg.lib.Tools;
import com.aljazarisoft.apps.luckyegg.lib.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {

    private TextView mTextMessage;
    EditText name,surname,email,phone,password;
    Button ExitBTN,UpdateprofileBTN,BackToProfile;
    HashMap<String,String> HashMap = new HashMap<String, String>();
    Session session;
    String Gourl=null;
    String Returned=null;
    boolean getupdate=false;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    getUpdateProfile();
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_eggs:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_map:
                    Intent map = new Intent(ProfileActivity.this,MapsActivity.class);
                    startActivity(map);
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_exit:
                    Session session=new Session(ProfileActivity.this);
                    session.exit();
                    Intent f = new Intent(ProfileActivity.this,LoginActivity.class);
                    startActivity(f);

                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session= new Session(ProfileActivity.this);


        if(session.get("login").equals("ok"))
            getConversations();
        else{
            Intent it = new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(it);
        }
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void getConversations()
    {
        HashMap<String, String> ConversationMap=new HashMap<String, String>();
        ConversationMap.put("","");
        HttpAsyncTask task = new HttpAsyncTask(ProfileActivity.this,ConversationMap);
        try {
            Gourl=new Url().Url("Inbox");
            this.Returned= task.execute(Gourl).get();
            if (this.Returned==null)
            {

            }else{

                response( );
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    protected void response( ){
        String msj=null;
        JSONArray andro= null;
        try {
            andro = new JSONArray(this.Returned);
            ListView list =(ListView)findViewById(R.id.List_Inbox);
            final ArrayList<JSONObject> listMessage=new Tools().getArrayListFromJSONArray(andro);


            ConversationsAdapter adapter=new ConversationsAdapter(this,R.layout.activity_user_inbox,R.id.Inbox_pic_profile,listMessage);

            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    String product = "";
                    try {
                        product = listMessage.get(position).getString("Conversation_User_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent menuIntent = new Intent(ProfileActivity.this, TextingActivity.class);
                    menuIntent.putExtra("Contact.User.Id", product);
                    startActivity(menuIntent);


                    //   Toast.makeText(getBaseContext(),  "you clicked position ="+position, Toast.LENGTH_LONG).show();

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }



        try {
            if(this.Returned.equals(""))
            {}else{
                JSONArray jsonObj = new JSONArray(this.Returned);
                for(int i=0;i<jsonObj.length();i++){
                    JSONObject json_data = jsonObj.getJSONObject(i);

                    if(json_data.has("RESULT")){
                        String query_result = json_data.getString("RESULT");
                        if (query_result.equals("FAILURE")) {
                            String reason = json_data.getString("REASON");
                            if(reason.equals("session")){
                                msj =ProfileActivity.this.getString(R.string.error_user_login_form_error);
                                new Tools().makeAlert(getApplicationContext(),"Fail","Cannot connect to the url","Okay i got it","","LoginActivity");

                            }
                            Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();

                        }





                    } else {
                        //    Toast.makeText(getApplicationContext(), "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }



    }
    protected void getUpdateProfile(){
        setContentView(R.layout.activity_profile_update);
        BackToProfile=(Button)findViewById(R.id.BackToProfile);
        BackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(menuIntent);
            }
        });
        Gourl = new Url().Url("Profile.Update");

        try {
            HttpAsyncTask task = new HttpAsyncTask(ProfileActivity.this,HashMap);
            Returned = task.execute(Gourl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(Returned==null){}
        else{
            try {
                getProfile();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        session = new Session(ProfileActivity.this);
        name = (EditText)findViewById(R.id.name);
        surname = (EditText)findViewById(R.id.surname);
        email = (EditText)findViewById(R.id.emailAd);
        phone = (EditText)findViewById(R.id.phone);

        password = (EditText) findViewById(R.id.Password_update);
        String Name =session.get("name");
        String Surname =session.get("surname");
        String Email =session.get("email");
        String Phone =session.get("phone");
        String Nick =session.get("nick");

        name.setText(Name);
        surname.setText(Surname);
        email.setText(Email);
        phone.setText(Phone);


        Button Update= (Button)findViewById(R.id.BTNUpdate);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString();
                String Surname = surname.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String Phone = phone.getText().toString();
                Toast.makeText(getApplicationContext(), "Trying to update...", Toast.LENGTH_SHORT).show();

            }
        });


    }
    protected  void getProfile() throws JSONException {

        try{
            String msj=null;
            JSONArray jsonObj = new JSONArray(this.Returned);
            for(int i=0;i<jsonObj.length();i++){
                JSONObject json_data = jsonObj.getJSONObject(i);

                if(json_data.has("RESULT")){
                    String query_result = json_data.getString("RESULT");
                    if (query_result.equals("SUCCESS")) {
                        String name    = json_data.getString("Profile.Name");
                        String surname = json_data.getString("Profile.Surname");
                        String email   = json_data.getString("Profile.Email");
                        String phone   = json_data.getString("Profile.Phone");
                        String nickn    = json_data.getString("Profile.Username");

                        this.name.setText(name);
                        this.surname.setText(surname);
                        this.email.setText(email);
                        this.phone.setText(phone);

                    }else if (query_result.equals("FAILURE")) {
                        String reason = json_data.getString("REASON");
                        if(reason.equals("session")){
                            msj =ProfileActivity.this.getString(R.string.error_user_login_form_error);
                            new Tools().makeAlert(getApplicationContext(),"Fail","Cannot connect to the url","Okay i got it","","LoginActivity");

                        }
                        Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();

                    }





                } else {
                    // Toast.makeText(getApplicationContext(), "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){

        }



    } }
