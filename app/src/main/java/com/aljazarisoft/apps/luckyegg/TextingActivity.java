package com.aljazarisoft.apps.luckyegg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.aljazarisoft.apps.luckyegg.Adapter.TextingAdapter;
import com.aljazarisoft.apps.luckyegg.lib.HttpAsyncTask;
import com.aljazarisoft.apps.luckyegg.lib.Tools;
import com.aljazarisoft.apps.luckyegg.lib.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class TextingActivity extends AppCompatActivity {
String Data=null;
    private int top;
    int index=0;
 ListView TextingList;
    String Conversation;
    private String URL =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texting);
          Conversation = getIntent().getStringExtra("Contact.User.Id");

        getTexting();

    }

    protected void  getTexting(){
        HashMap<String,String> map = new HashMap<String,String>();

        HttpAsyncTask task = new HttpAsyncTask(TextingActivity.this,map);
        try {
            this.URL=new Url().Url("Contact")+"user/"+Conversation+"/";

            this.Data=task.execute(this.URL).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        getTheConversation();
    }
    protected void getTheConversation(){
        Button btnSend = (Button) findViewById(R.id.btn_chat_send);
        final EditText   editText = (EditText) findViewById(R.id.msg_type);
        try {

            // mCreateAndSaveFile("message.json",returned);

            if(this.Data==null)
            {

            }else {
                JSONArray andro =  new JSONArray(this.Data);


                TextingList =(ListView)findViewById(R.id.list_msg_text);
               index = TextingList.getFirstVisiblePosition();
                ArrayList<JSONObject> listItems=new Tools().getArrayListFromJSONArray(andro);

                TextingAdapter adapter=new TextingAdapter(this, R.layout.item_chat_left,listItems);

                final String  TextMessage =editText.getText().toString();
                TextingList.setAdapter(adapter);
                TextingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {



                        //   Toast.makeText(getBaseContext(),  "you clicked position ="+position, Toast.LENGTH_LONG).show();

                    }
                });

            }

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(15000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // save index and top position
                                    index = TextingList.getFirstVisiblePosition();
                                    View v = TextingList.getChildAt(0);
                                    top = (v == null) ? 0 : (v.getTop() - TextingList.getPaddingTop());
                                    getTexting();


                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };

            t.start();



        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HashMap<String, String> map =new HashMap<String, String>();
                map.put("Text",editText.getText().toString());
                map.put("Conversation_id",editText.getText().toString());
                map.put("UserId",Conversation);

                try {

                    URL=new Url().Url("Conversation")+Conversation;

                    Data= (String) new HttpAsyncTask(TextingActivity.this,map).execute(URL).get();
                    if(!Data.equals("")){
                        try {
                            JSONArray jsonarray = new JSONArray(Data);


                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonObj = jsonarray.getJSONObject(i);
                                String query_result = jsonObj.getString("RESULT");
                                if (query_result.equals("FAILURE")) {
                                    String msj=null;

                                    Toast.makeText(getBaseContext(),  "no message", Toast.LENGTH_SHORT).show();
                                    String reason = jsonObj.getString("REASON");
                                    String response = jsonObj.getString("RESPONSE");
                                    if (reason.equals("empty"))
                                        msj = "Error!";


                                    Toast.makeText(getApplicationContext(),  msj, Toast.LENGTH_SHORT).show();
                                }else{



                                }
                            }





                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }else{

                    }


                    getTexting();
                    editText.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }





            }
        });
    }
}
