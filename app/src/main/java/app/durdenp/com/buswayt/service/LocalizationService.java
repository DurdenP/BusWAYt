package app.durdenp.com.buswayt.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class LocalizationService extends Service {

    @Override
    public void onCreate(){
        Toast.makeText(getApplicationContext(),
                "Il service e' stato creato", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int id){
        Toast.makeText(getApplicationContext(),
                "Il service e' partito", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy(){
        Toast.makeText(getApplicationContext(),
                "Il service e' stato terminato", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
