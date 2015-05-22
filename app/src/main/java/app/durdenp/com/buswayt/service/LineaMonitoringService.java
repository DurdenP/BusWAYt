package app.durdenp.com.buswayt.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class LineaMonitoringService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final IBinder mBinder = new LocalBinder();


    //Linea and bus tracked
    private ResultReceiver busTraceReceiver;
    private String city;
    private String linea;
    private String busID;

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


    public LineaDescriptor getLineaDescriptor(String label, String city){
        LineaDescriptor linea = new LineaDescriptor();

        /*TODO rimuovere il codice segunete e inserire il codice per fare la richiesta al webservice
        * la linea potrebbe anche essere salvata in locale, ad esempio nella citta' dove si risiede
        * e si usa spesso il servizio in modo da non dover fare sempre richieste che consumano banda
        * e batteria*/
        LinkedList<FermataDescriptor> busStops = new LinkedList();

        FermataDescriptor stop01 = new FermataDescriptor("Parcheggio 2 Obelischi", "1832", new LatLng(37.541386, 15.078818));
        FermataDescriptor stop02 = new FermataDescriptor("Largo Barriera", "1833", new LatLng(37.542831, 15.076597));
        FermataDescriptor stop03 = new FermataDescriptor("Passo gravina", "171", new LatLng(37.538871, 15.071018));
        FermataDescriptor stop04 = new FermataDescriptor("Passo gravina 261", "1835", new LatLng(37.533429, 15.076938));
        FermataDescriptor stop05 = new FermataDescriptor("Via Etnea f/co", "177", new LatLng(37.524651, 15.081988));
        FermataDescriptor stop06 = new FermataDescriptor("Largo Barriera", "1844", new LatLng(37.542775, 15.076511));

        busStops.add(stop01);
        busStops.add(stop02);
        busStops.add(stop03);
        busStops.add(stop04);
        busStops.add(stop05);
        busStops.add(stop06);

        linea.setId("ctBrt");
        linea.setLabel(label);
        linea.setBusStops(busStops);
        tracingRequestor();

        return linea;
    }

    /**
     * Questa funzione chiede al webservice la posizione di uno o di tutti gli autobus
     * della linea
     */
    private void tracingRequestor(){

        /*
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        */

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
