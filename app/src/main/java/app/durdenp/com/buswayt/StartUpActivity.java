package app.durdenp.com.buswayt;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class StartUpActivity extends ActionBarActivity {

    String tag = "StartUpActivity";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag, "Chiamo onCreate");



        setContentView(R.layout.activity_main);
        final Handler handler = new Handler();
        intent = new Intent(this, ChooseModeActivity.class);
        final Runnable doNextActivity = new Runnable() {
            @Override
            public void run() {
                // Intent to jump to the next activity

                startActivity(intent);
                finish(); // so the splash activity goes away
            }
        };

        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                handler.post(doNextActivity);
            }
        }.start();


    }






    @Override
    protected void onStart(){
        Log.i(tag, "Chiamo onStart");
        super.onStart();


    }

//    protected void onResume()
//
//    {
//        Log.i(tag, "Chiamo onResume");
//        super.onResume();
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Intent intent = new Intent(this, ChooseModeActivity.class);
//            startActivity(intent);
//
//    }


}
