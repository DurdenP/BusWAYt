package app.durdenp.com.buswayt.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class LineaMonitoringService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class LocalBinder extends Binder {
        public LineaMonitoringService getService() {
            return LineaMonitoringService.this;
        }
    }

    public LineaMonitoringService() {
    }


    @Override
    public void onCreate(){
        Toast.makeText(getApplicationContext(),
                "Il service e'? stato creato", Toast.LENGTH_LONG).show();

    }


    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(getApplicationContext(),
                "Il service e'? stato terminato", Toast.LENGTH_LONG).show();
    }
}
