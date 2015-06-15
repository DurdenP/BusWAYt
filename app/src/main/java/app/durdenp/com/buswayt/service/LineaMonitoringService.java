package app.durdenp.com.buswayt.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;
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

import app.durdenp.com.buswayt.R;
import app.durdenp.com.buswayt.mapUtility.LineaDescriptor;

public class LineaMonitoringService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    String url;
    private final IBinder mBinder = new LocalBinder();

    //Linea and bus tracked
    private ResultReceiver busTraceReceiver;
    private String city;
    private String idLinea;

    private Timer timer;
    private TimerTask busTracerTask;
    final Handler handler = new Handler();
    RequestQueue queue;


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

        url=getResources().getString(R.string.webserver);
        queue= Volley.newRequestQueue(getApplicationContext());

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

        String mLinea = "busstationinfo?linea="+idLinea;
        String localurl =url+mLinea;


//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, localurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Receive the responce and send it to activity because through bundle
                        // we can only pass flat data
                        Bundle bundle = new Bundle();
                        bundle.putString("response", response);
                        busTraceReceiver.send(2, bundle);
                        //TODO adjust trace request
                        tracingRequest();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "That didn't work!", Toast.LENGTH_SHORT)
                        .show();
                tracingRequest();
            }
        });
        queue.add(stringRequest);
    }

    /**
     * Send info abaut route of selected linea to activity CODE: 4
     */
    private void sendLineaInfoRequest() {

//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String path="routeinfo?linea="+idLinea;
        String localurl = url+path;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, localurl,
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
        timer.schedule(busTracerTask, 1000, 1000);

    }

    public void initializeTimerTask() {

        busTracerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp

                handler.post(new Runnable() {

                    public void run() {



                        String path="lineinfo?linea=";
                        String localurl =url+path;

                        if(idLinea != null) {
                            // faccio richiesta solo per lo specifico bus
                            localurl = localurl + idLinea;
                            //url = "http://151.97.63.136:8080/lineinfo?linea="+ busID;
                        }

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, localurl,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        // Receive the responce and send it to activity because through bundle
                                        // we can only pass flat data
                                        Bundle bundle = new Bundle();
                                        bundle.putString("response", response);

                                        Log.w("RESPONSE: ", response.toString());

                                        busTraceReceiver.send(1, bundle);

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Bundle bundle = new Bundle();
                                bundle.putString("response", "none");

                                busTraceReceiver.send(1, bundle);
                                Toast.makeText(getApplicationContext(),
                                        "That didn't work! BusInfo", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                        queue.add(stringRequest);
                    }
                });
            }
        };
    }

}
