package app.durdenp.com.buswayt.client;

import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import app.durdenp.com.buswayt.R;
import app.durdenp.com.buswayt.bus.CustomRequest;
import app.durdenp.com.buswayt.jsonWrapper.LineaRequestedWrapper;
import app.durdenp.com.buswayt.mapUtility.BusDescriptor;
import app.durdenp.com.buswayt.mapUtility.FermataDescriptor;
import app.durdenp.com.buswayt.jsonWrapper.FermateWrapper;
import app.durdenp.com.buswayt.mapUtility.LineaDescriptor;
import app.durdenp.com.buswayt.mapUtility.LineaSetup;
import app.durdenp.com.buswayt.service.LineaMonitoringService;


public class ClientActivity extends ActionBarActivity implements RequestLineaFragment.OnFragmentInteractionListener {

    private GoogleMap googleMap;
    private LineaMonitoringService lineaServiceConnection;
    private boolean mBound = false;

    private LineaSetup lineaSetup;

    //Description Bus Stop variable
    private LineaDescriptor linea;
    private ArrayList<Polyline> routeLineArray;
    private ArrayList<Marker> busStopArray;
    private String citySelected;

    //Tracing bus description
    BusPositionReceiver tracingReceiver;
    ArrayList<Marker> busMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        lineaSetup = new LineaSetup();

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


        Log.w("onFragmentInteraction", cmd);

        switch(cmd){
            case "traceLinea":

                String message = "città: " + arguments[0] + " linea: " + arguments[1];

                Log.w("onFragmentInton message", message);

                citySelected = arguments[0];
                centerMapToCitySelected();
                linea = lineaServiceConnection.getLineaDescriptor(arguments[1], arguments[0]);
                break;
        }
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
        Log.w("printLinea", "printLinea ciao");
        printBusStop();
        printRoute();
    }

    /**
     *
     */
    private void printBusStop(){
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
            if(stopCount == 1){/*Capolinea*/
                busStopArray.add(googleMap.addMarker(new MarkerOptions().title(tmp.getNome()).position(tmp.getCoordinates()).snippet("Linee in Transito: " + tmp.getLineeTransito()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))));
            }else {
                busStopArray.add(googleMap.addMarker(new MarkerOptions().title(tmp.getNome()).position(tmp.getCoordinates()).snippet("Linee in Transito: " + tmp.getLineeTransito()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))));
            }
        }

        LatLng cameraposition = new LatLng(latitude/stopCount, longitude/stopCount);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraposition, 14));
    }

    /**
     *
     */
    private void printRoute(){
        LinkedList<LatLng> route = linea.getRoute();

        Log.w("printRoute:size - ", Integer.toString(route.size()));
        if(routeLineArray == null){
            routeLineArray = new ArrayList();
        }

        if(!routeLineArray.isEmpty()){
            for(Polyline tmp : routeLineArray){
                tmp.remove();
            }
        }

        for(int z = 0; z<route.size()-1; z++){
            LatLng src= route.get(z);
            LatLng dest= route.get(z + 1);
            routeLineArray.add(googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                    .width(3)
                    .color(Color.BLUE).geodesic(true)));
        }

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
            busMarker.add(googleMap.addMarker(new MarkerOptions().title(tmpDesc.getId()).position(tmpDesc.getCoordinates()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))));
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

            switch(resultCode){
                case 1: /*Follow bus movement with camera*/

                    manageBusPositionInfoResponce(resultData.getString("response"));

                    /*
                    BusDescriptor bus = new BusDescriptor("CT130", "BRT");
                    LatLng coord = new LatLng(resultData.getDouble("latitude"), resultData.getDouble("longitude"));
                    bus.setCoordinates(coord);
                    bus.setSpeed(resultData.getDouble("speed"));
                    ArrayList<BusDescriptor> tmpArray = new ArrayList();
                    tmpArray.add(bus);
                    */
                    //sendDataToBusListFragment();

                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 16));

                    //Calling print function
                    //printBusMarker(tmpArray);
                    break;
                case 2: /*Processing a list of bus stops of selected line*/
                    manageBusStopInfoResponce(resultData.getString("response"));
                    break;
                case 3:/*Do not follow bus movement with camera*/
                    break;
                case 4:/*Receiving info of the linea route*/
                    manageLineaInfoResponce(resultData.getString("response"));
                    break;
            }
        }

        private void manageBusPositionInfoResponce(String busPositionJSON){
            if(busPositionJSON.compareTo("none") == 0){
                Log.w("ClientActivity", "No Position received");
            }else {
                ArrayList<BusDescriptor> busList = lineaSetup.parseBusPositionJSON(busPositionJSON);

                if (busList != null && !busList.isEmpty()) {
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busList.get(0).getCoordinates(), 13));
                }

                printBusMarker(busList);

            /*Manda delle cose fake*/
                //sendDataToBusListFragment();
            }
        }

        private void manageBusStopInfoResponce(String busStopJSON){

            Log.w("busStopJSON", busStopJSON);

            if(lineaSetup.setUpBusStop(linea, parseFermate(busStopJSON))){

                printLinea();
            }
        }


        /**
         *
         * @param inputJSON
         * @return
         */
        private LinkedList<FermataDescriptor> parseFermate(String inputJSON){

            Type listType = new TypeToken<LinkedList<FermateWrapper>>() {}.getType();
            Gson reader = new Gson();

            LinkedList<FermateWrapper> fermate = reader.fromJson(inputJSON, listType);

            LinkedList<FermataDescriptor> tmpFermate = new LinkedList();

            ListIterator<FermateWrapper> it = fermate.listIterator();
            while(it.hasNext()){
                FermateWrapper tmpVect = it.next();
                FermataDescriptor tmp = new FermataDescriptor(tmpVect.getDescrizione(), tmpVect.getCodice(), tmpVect.getLineeTransito(), new LatLng(tmpVect.getLatitude(), tmpVect.getLongitude()));
                tmpFermate.add(tmp);
            }

            return tmpFermate;

        }

        /**
         *
         * @param lineaInfoJSON
         */
        private void manageLineaInfoResponce(String lineaInfoJSON){
            Log.w("lineaInfoJSON", lineaInfoJSON);
            Gson reader = new Gson();

            LineaRequestedWrapper fermateInfo = reader.fromJson(lineaInfoJSON, LineaRequestedWrapper.class);
            lineaSetup.parseLineaRequestedWrapper(linea, fermateInfo);
            lineaSetup.setUpRoute(linea);

            if(linea.isReadyToBePrinted()){
                printLinea();
            }
        }
    }
}
