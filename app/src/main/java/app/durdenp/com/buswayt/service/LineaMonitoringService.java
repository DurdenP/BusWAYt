package app.durdenp.com.buswayt.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Timer;
import java.util.TimerTask;

import app.durdenp.com.buswayt.mapUtility.LineaDescriptor;

public class LineaMonitoringService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final IBinder mBinder = new LocalBinder();


    //Linea and bus tracked
    private ResultReceiver busTraceReceiver;
    private String city;
    private String idLinea;
    //private String busID;

    private Timer timer;
    private TimerTask busTracerTask;
    final Handler handler = new Handler();

    //TODO delete this counter when the webservice is active
    private int count = 0;

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
                "Il service e' stato creato", Toast.LENGTH_LONG).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        busTraceReceiver = intent.getParcelableExtra("receiver");
        return mBinder;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(getApplicationContext(),
                "Il service e' stato terminato", Toast.LENGTH_LONG).show();
    }



    // TODO Actually city is not used because, we have only city in database
    public LineaDescriptor getLineaDescriptor(String lineaId, String city){

        LineaDescriptor tmp = new LineaDescriptor(lineaId, city);

        this.idLinea = lineaId;
        this.city = city;

        sendBusStopRequest();
        sendLineaInfoRequest();

        return tmp;
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    /**
     * Send fermate of selected linea to activity   CODE: 2
     */
    private void sendBusStopRequest() {
        String mLinea = "l"+idLinea;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getStringResourceByName(mLinea);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Receive the responce and send it to activity because through bundle
                        // we can only pass flat data
                        Bundle bundle = new Bundle();
                        bundle.putString("response", response);
                        busTraceReceiver.send(2, bundle);
                        //TODO insert trace request
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "That didn't work!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        queue.add(stringRequest);
    }

    /**
     * Send info abaut route of selected linea to activity CODE: 4
     */
    private void sendLineaInfoRequest() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://www.amt.ct.it/MappaLinee/leggilineerichiestej.php?linee='"+idLinea+"','xx'";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Receive the responce and send it to activity because through bundle
                        // we can only pass flat data
                        Bundle bundle = new Bundle();
                        bundle.putString("response", response);
                        busTraceReceiver.send(4, bundle);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "That didn't work!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        queue.add(stringRequest);
    }


    /**
     * Questa funzione chiede al webservice la posizione di tutti gli autobus
     * della linea e invia la risposta all'activity CODE: 1
     */
    private void tracingRequest(){

        if(timer != null){
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
        initializeTimerTask();
        timer.schedule(busTracerTask, 2500, 5000);

    }

    public void initializeTimerTask() {

        busTracerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp

                handler.post(new Runnable() {

                    public void run() {
                        /* TODO
                        if(busID != null) {
                            faccio richiesta solo per lo specifico bus
                        } else {
                            faccio richiesta per tutti i bus della linea
                        }
                         */

                        //get the current timeStamp
                        /*
                        Toast.makeText(getApplicationContext(),
                                "Sending messages", Toast.LENGTH_LONG).show();
                        */
                        count++;
                        Bundle bundle = new Bundle();
                        bundle.putDouble("latitude", 37.541386 + (count*0.00003));
                        bundle.putDouble("longitude", 15.078818+(count*0.00003));
                        bundle.putDouble("speed", 25.5);
                        busTraceReceiver.send(1, bundle);

                    }
                });
            }
        };
    }

}
