package app.durdenp.com.buswayt.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.ListIterator;

import app.durdenp.com.buswayt.R;
import app.durdenp.com.buswayt.service.BusDescriptor;
import app.durdenp.com.buswayt.service.FermataDescriptor;
import app.durdenp.com.buswayt.service.LineaDescriptor;
import app.durdenp.com.buswayt.service.LineaMonitoringService;


public class ClientActivity extends ActionBarActivity implements RequestLineaFragment.OnFragmentInteractionListener, BusListFragmentFragment.OnFragmentInteractionListener {

    private GoogleMap googleMap;
    private LineaMonitoringService lineaServiceConnection;
    private boolean mBound = false;


    //Description Bus Stop variable
    private LineaDescriptor linea;
    private ArrayList<Marker> busStopArray;
    private String citySelected;

    //Tracing bus description
    BusPositionReceiver tracingReceiver;
    ArrayList<Marker> busMarker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        /*
        //Adding Second Fragment
        RequestLineaFragment lineaFragment = new RequestLineaFragment();
        lineaFragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().add(R.id.linearlayout02, lineaFragment).commit();
        //End Adding second frame
        */

        //Adding Third Fragment
        BusListFragmentFragment busListFragment = new BusListFragmentFragment();
        busListFragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().add(R.id.linearlayout02, busListFragment).commit();
        //End Adding Third Fragment


        //Tracing bus configuration
        tracingReceiver = new BusPositionReceiver(null);


        try {
            // Loading map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (googleMap != null) {

            //Focalizziamo la mappa su un punto prefissato
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.524940, 15.073690), 15));


        } else {
            GooglePlayServicesUtil.getErrorDialog(
                    GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
                    , this, 0).show();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Intent intent = new Intent(this, LineaMonitoringService.class);
        intent.putExtra("receiver", tracingReceiver);
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

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LineaMonitoringService.LocalBinder binder = (LineaMonitoringService.LocalBinder) service;
            lineaServiceConnection = binder.getService();
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
        getMenuInflater().inflate(R.menu.menu_client, menu);
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

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //Se l'applicazione lascia il foreground
        //cancello la sottoscrizione al location listener
        //locationManager.removeUpdates(listenerFine);

    }

    /**
     * Refer to RequestLineaFragment
     * @param cmd
     * @param arguments
     */
    @Override
    public void onFragmentInteraction(String cmd, String[] arguments) {
        Log.d("onFragmentInteraction", cmd);
        switch(cmd){
            case "traceLinea":
                String message = "città: " + arguments[0] + " linea: " + arguments[1];
                Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                toast.show();
                citySelected = arguments[0];
                centerMapToCitySelected();
                linea = lineaServiceConnection.getLineaDescriptor(arguments[1], arguments[0]);
                printLinea();
                break;
        }
    }

    /**
     * Refer to BusListFragment
     * @param id
     */
    @Override
    public void onFragmentInteraction(String id) {
        /*TODO busListFragment Interaction Listener*/
        Toast.makeText(this, id + " Clicked!"
                , Toast.LENGTH_SHORT).show();


    }

    private void centerMapToCitySelected(){
        LatLng position = new LatLng(37.524940, 15.073690);

        switch(citySelected){
            case "Catania":
                position = new LatLng(37.524940, 15.073690);
                break;
            case "Palermo":
                position = new LatLng(38.120113, 13.356774);
                break;
            case "Messina":
                position = new LatLng(38.185347, 15.546908);
                break;
        }

        if(googleMap != null){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
        }
    }

    /**
     * Disegna il percorso della linea selezionata sulla mappa
     */
    private void printLinea(){
        ListIterator<FermataDescriptor> it = linea.getBusStops().listIterator();

        double latitude = 0;
        double longitude = 0;
        double stopCount = 0;


        /*Removing old bus stop*/
        if(busStopArray == null){
            busStopArray = new ArrayList();
        }

        if(!busStopArray.isEmpty()) {
            for (Marker tmp : busStopArray) {
                tmp.remove();
            }
        }

        /*adding current busStop array*/
        while(it.hasNext()){
            stopCount++;
            FermataDescriptor tmp = it.next();
            latitude = latitude + tmp.getCoordinates().latitude;
            longitude = longitude + tmp.getCoordinates().longitude;

            busStopArray.add(googleMap.addMarker(new MarkerOptions().title(tmp.getNome()).position(tmp.getCoordinates())));
        }

        LatLng cameraposition = new LatLng(latitude/stopCount, longitude/stopCount);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraposition, 14));

    }

    /**
     * TODO
     * @param bus
     */
    private void printBusMarker(ArrayList<BusDescriptor> bus){
        if(busMarker == null){
            busMarker = new ArrayList();
        }

        if(!busMarker.isEmpty()){
            for(Marker tmp : busMarker){
                tmp.remove();
            }
        }

        for(BusDescriptor tmpDesc : bus){
            busMarker.add(googleMap.addMarker(new MarkerOptions().title(tmpDesc.getId()).position(tmpDesc.getCoordinates())));
        }
    }


    class BusPositionReceiver extends ResultReceiver{

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public BusPositionReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if(resultCode == 1){
                BusDescriptor bus = new BusDescriptor("CT130", "BRT");
                LatLng coord = new LatLng(resultData.getDouble("latitude"), resultData.getDouble("longitude"));
                bus.setCoordinates(coord);
                bus.setSpeed(resultData.getDouble("speed"));
                ArrayList<BusDescriptor> tmpArray = new ArrayList();
                tmpArray.add(bus);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 16));

                //Calling print function
                printBusMarker(tmpArray);
            }

        }
    }
}
