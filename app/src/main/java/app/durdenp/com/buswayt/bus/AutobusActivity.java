package app.durdenp.com.buswayt.bus;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import app.durdenp.com.buswayt.R;
import app.durdenp.com.buswayt.service.LocalizationService;


public class AutobusActivity extends ActionBarActivity {
    LocalizationService mService;
    private static final String TAG = AutobusActivity.class.getSimpleName();

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private Button btnShowLocation, btnStartLocationUpdates;
    Button imageButton;
    String lineaid;
    String busid;


    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autobus);

        Bundle extras = getIntent().getExtras();
        lineaid= extras.getString("lineaid");
        busid=extras.getString("busid");

    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
//        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        btnStartLocationUpdates = (Button) findViewById(R.id.btnLocationUpdates);
        Intent intent = new Intent(this, LocalizationService.class);
        imageButton =(Button)findViewById(R.id.button4);
        intent.putExtra("lineaid",lineaid);
        intent.putExtra("busid",busid);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }



    public void startSimulation(View view)
    {

        mService.sendLineaInfoRequest();


    }







    public void startLocationUpdates(View view)
    {
        if(mBound)
        {

            if (!mRequestingLocationUpdates) {
                // Changing the button text
                btnStartLocationUpdates
                        .setText("STOP LOCATION UPDATES");

                mRequestingLocationUpdates = true;

                imageButton.setVisibility(View.VISIBLE);

                // Starting the location updates
                mService.startLocationUpdates();



            } else {
                // Changing the button text
                btnStartLocationUpdates
                        .setText("START LOCATION UPDATES");

                mRequestingLocationUpdates = false;
                imageButton.setVisibility(View.INVISIBLE);

                // Stopping the location updates
                mService.stopLocationUpdates();
                System.out.println("stopLocationUpdates!");


            }




        }

    }








    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalizationService.LocalBinder binder = (LocalizationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_autobus, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
