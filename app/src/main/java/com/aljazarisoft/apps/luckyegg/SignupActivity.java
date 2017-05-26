package com.aljazarisoft.apps.luckyegg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aljazarisoft.apps.luckyegg.lib.HttpAsyncTask;
import com.aljazarisoft.apps.luckyegg.lib.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SignupActivity extends AppCompatActivity {
    EditText Name,Surname, Nick, Password, RePassword, Email,Phone;
    Button SignupBtn,LoginBtn;
    String Gourl=null;

    String Returned=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_type_list);
        Button openform=(Button)findViewById(R.id.signup_button_form);
        openform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_signup);
                Nick =(EditText)findViewById(R.id.formnick);
                Name = (EditText) findViewById(R.id.Sign_up_Name);
                Surname = (EditText) findViewById(R.id.Sign_up_Surname);
                Email = (EditText) findViewById(R.id.Sign_up_Email);
                Phone = (EditText) findViewById(R.id.Phone);
                Password = (EditText) findViewById(R.id.Sign_up_Password);
                RePassword = (EditText) findViewById(R.id.Sign_up_RePassword);
                SignupBtn = (Button) findViewById(R.id.btnSignup);
                LoginBtn = (Button) findViewById(R.id.LoginBTN);
                LoginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent menuIntent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(menuIntent);
                    }
                });
                SignupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = Name.getText().toString();
                        String surname = Surname.getText().toString();
                        String nick = Nick.getText().toString();
                        String email = Email.getText().toString();
                        String password = Password.getText().toString();
                        String repassword = RePassword.getText().toString();
                        String phone = Phone.getText().toString();
                        HashMap<String,String> signup=new HashMap<String, String>();
                        signup.put("name",name);
                        signup.put("surname",surname);
                        signup.put("user",nick);
                        signup.put("mail",email);
                        signup.put("password",password);
                        signup.put("rep",repassword);
                        signup.put("phone",phone);
                        Gourl=new Url().Url("Signup");
                        try {
                            Returned= new HttpAsyncTask(SignupActivity.this,signup).execute(Gourl).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }


                        if(Returned==null){}
                        else{
                            getTheResponse();
                        }


                    }
                });
            }
        });



    }
    protected  void getTheResponse(){

        if (Returned != null) {
            try {
                JSONArray jsonarray = new JSONArray(this.Returned);


                for(int i=0; i < jsonarray.length(); i++) {
                    JSONObject jsonObj = jsonarray.getJSONObject(i);
                    String query_result = jsonObj.getString("RESULT");
                    if (query_result.equals("SUCCESS")) {

                        Toast.makeText(SignupActivity.this, R.string.user_sign_up_form_success, Toast.LENGTH_SHORT).show();


                        Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(myIntent);

                    } else if (query_result.equals("FAILURE")) {
                        String reason = jsonObj.getString("REASON");
                        String msj=null;
                        if(reason.equals("empty"))
                            msj=SignupActivity.this.getString(R.string.error_user_sign_up_form_empty);
                        else if(reason.equals("userexist"))
                            msj=SignupActivity.this.getString(R.string.error_user_sign_up_form_token_nick);
                        else if(reason.equals("emailexist"))
                            msj=SignupActivity.this.getString(R.string.error_user_sign_up_form_token_email);
                        else if(reason.equals("false_nick"))
                            msj=SignupActivity.this.getString(R.string.error_user_sign_up_form_invalid_nick);
                        else if(reason.equals("false_email"))
                            msj=SignupActivity.this.getString(R.string.error_user_sign_up_form_invalid_email);
                        else if(reason.equals("matching"))
                            msj=SignupActivity.this.getString(R.string.error_user_sign_up_form_invalid_match_pw);
                        else if(reason.equals("closed"))
                            msj=SignupActivity.this.getString(R.string.error_user_sign_up_closed);
                        else if(reason.equals("unknow"))
                            msj=SignupActivity.this.getString(R.string.error_user_sign_up_unknow);
                        else if(reason.equals("false"))
                            msj=SignupActivity.this.getString(R.string.error_user_sign_up_unknow);




                        Toast.makeText(SignupActivity.this, msj, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(SignupActivity.this, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
                }





            } catch (JSONException e) {

                e.printStackTrace();

            }
        } else {
            Toast.makeText(SignupActivity.this, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }
}
