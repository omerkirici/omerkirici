package com.aljazarisoft.apps.luckyegg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.aljazarisoft.apps.luckyegg.lib.Session;
import com.google.firebase.iid.FirebaseInstanceId;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        CookieHandler.setDefault(new CookieManager());
        Session session = new Session(MainActivity.this);
        session.set("Token", FirebaseInstanceId.getInstance().getToken());
        TimerTask task2 = new TimerTask() {

            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finishscreen();
            }
        };
        Timer t = new Timer();
        t.schedule(task2, 3000);

    }

    private void finishscreen() {
        Session session = new Session(MainActivity.this);

        this.finish();
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
}
