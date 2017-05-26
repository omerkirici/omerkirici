package com.aljazarisoft.apps.luckyegg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.aljazarisoft.apps.luckyegg.lib.HttpAsyncTask;
import com.aljazarisoft.apps.luckyegg.lib.Session;
import com.aljazarisoft.apps.luckyegg.lib.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
protected String Returned=null;
    Session session;
    EditText User,Password;
    Button Login,Signup,Remember,Forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


 session = new Session(LoginActivity.this);

        if(session.get("nick").equals("") || session.get("password").equals(""))
            getLoginForm();
        else
        doLogin();


    }

    protected void doLogin(){
        HashMap<String,String>  login= new HashMap<String,String>();
        login.put("nick",session.get("nick"));
        login.put("password",session.get("password"));
        String Gourl=new Url().Url("Login");

        HttpAsyncTask task = new HttpAsyncTask(LoginActivity.this,login);
        try {
            this.Returned=task.execute(Gourl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(this.Returned==null){
            finish();
        }
        else{
            doLoginResponse();
        }
    }
    protected  void doLoginResponse(){
        Session session = new Session(LoginActivity.this);

        try {
            if(this.Returned.equals(""))
            { }else{
                JSONArray jsonObj = new JSONArray(this.Returned);

                for(int i=0;i<jsonObj.length();i++){
                    JSONObject json_data = jsonObj.getJSONObject(i);

                    if(json_data.has("RESULT")){
                        String query_result = json_data.getString("RESULT");
                        if (query_result.equals("SUCCESS")) {
                            String name    = json_data.getString("name");
                            String surname = json_data.getString("surname");
                            String email   = json_data.getString("email");
                            String phone   = json_data.getString("phone");
                            String nickn    = json_data.getString("nick");


                            session.set("login","ok");
                            session.set("name",name);
                            session.set("surname",surname);
                            session.set("email",email);
                            session.set("phone",phone);
                            session.set("nick",nickn);


                            Intent myIntent = new Intent(LoginActivity.this, ProfileActivity.class);

                            startActivity(myIntent);


                        } else if (query_result.equals("FAILURE")) {



                            String reason = json_data.getString("REASON");
                            if(reason.equals("empty"))
                                session.exit();
                            else if(reason.equals("suspended"))
                                session.exit();
                            else if(reason.equals("false")){
                                session.exit();

                            }




                          //  Toast.makeText(getApplicationContext(), "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                        } else {
                         //   Toast.makeText(getApplicationContext(), "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                        }





                    } else {
                        //Toast.makeText(getApplicationContext(), "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
    protected  void getLoginForm(){

        setContentView(R.layout.activity_login);
        User =(EditText)findViewById(R.id.NICK);
        Password =(EditText)findViewById(R.id.PASSWORD);
        Login =(Button)findViewById(R.id.LOGINBNT);
       final  CheckBox remember = (CheckBox)findViewById(R.id.rememberme);
        if(remember.isChecked())

        Signup = (Button)findViewById(R.id.buttonsgnup);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
                }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> login =new HashMap<String, String>();
                login.put("GOTO","Login");
                login.put("nick",User.getText().toString());
                session=new Session(LoginActivity.this);

                login.put("password",Password.getText().toString());
                if(remember.isChecked())
                    session.set("password",Password.getText().toString());

                String Gourl=new Url().Url("Login");

                HttpAsyncTask task = new HttpAsyncTask(LoginActivity.this,login);
                try {
                    Returned=task.execute(Gourl).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(Returned==null){
                    finish();
                }
                else{
                    callResponseLoginForm();
                }

            }
        });

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
    protected  void callResponseLoginForm(){
        String msj=null;

        try {
            if(this.Returned.equals("")){

            }else{
                JSONArray jsonObj = new JSONArray(this.Returned);


                for(int i=0;i<jsonObj.length();i++){
                    JSONObject json_data = jsonObj.getJSONObject(i);

                    if(json_data.has("RESULT")){
                        String query_result = json_data.getString("RESULT");
                        if (query_result.equals("SUCCESS")) {
                            Toast.makeText(LoginActivity.this, json_data.getString("name") , Toast.LENGTH_SHORT).show();
                            String name    = json_data.getString("name");
                            String surname = json_data.getString("surname");
                            String email   = json_data.getString("email");
                            String phone   = json_data.getString("phone");
                            String nickn    = json_data.getString("nick");


                            session.set("login","ok");
                            session.set("name",name);
                            session.set("surname",surname);
                            session.set("email",email);
                            session.set("phone",phone);
                            session.set("nick",nickn);


                            Toast.makeText(getApplicationContext(), R.string.user_login_form_success, Toast.LENGTH_SHORT).show();

                            Intent myIntent = new Intent(LoginActivity.this, ProfileActivity.class);

                            startActivity(myIntent);

                            this.finish();
                        }else if (query_result.equals("FAILURE")) {
                            String reason = json_data.getString("REASON");
                            if(reason.equals("empty"))
                                msj =LoginActivity.this.getString(R.string.error_user_login_form_empty);
                            else if(reason.equals("suspended"))
                                msj =LoginActivity.this.getString(R.string.error_user_login_form_suspended);
                            else if(reason.equals("false")){

                                msj=LoginActivity.this.getString(R.string.error_user_login_form_false) ;
                                Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();

                            }





                        } else {
                            Toast.makeText(getApplicationContext(), "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }



        } catch (JSONException e) {
            e.printStackTrace();

        }

    }
}
