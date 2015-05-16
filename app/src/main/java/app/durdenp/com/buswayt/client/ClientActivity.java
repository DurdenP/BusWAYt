package app.durdenp.com.buswayt.client;

import android.support.v7.app.ActionBarActivity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
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

import app.durdenp.com.buswayt.R;


public class ClientActivity extends ActionBarActivity implements RequestLineaFragment.OnFragmentInteractionListener {

    private GoogleMap googleMap;

    private ArrayList<LatLng> posMarkArray = new ArrayList<>();
    private ArrayList<Marker> markerArray = new ArrayList<>();


    private LocationManager locationManager;
    private LocationListener listenerFine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        RequestLineaFragment lineaFragment = new RequestLineaFragment();
        lineaFragment.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction().add(R.id.linearlayout02, lineaFragment).commit();

        posMarkArray.add(new LatLng(37.524940, 15.073690));
        posMarkArray.add(new LatLng(37.526340, 15.075090));

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (googleMap != null) {

            //Focalizziamo la mappa su un punto prefissato

            if(!posMarkArray.isEmpty()) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posMarkArray.get(0), 10));
            }else{
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.524940, 15.073690), 15));
            }


            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.524940, 15.073690), 15));
            //Aggiungiamo i marker

            for(int i = 0; i < posMarkArray.size() ; i++){
                markerArray.add(googleMap.addMarker(new MarkerOptions()
                        .position(posMarkArray.get(i)).title("Marker_"+i)));
            }


        } else {
            GooglePlayServicesUtil.getErrorDialog(
                    GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
                    , this, 0).show();
        }
    }

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

    //Registrazione del location listener
    private void registerLocationListener() {

        //ottenimento location manager da sistema
        locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);

        //definizione criteri di scelta del provider
        Criteria fine = new Criteria();
        fine.setAccuracy(Criteria.ACCURACY_FINE);

        if (listenerFine == null)
            createLocationListener();

        //registrazione del listener al location manager
        locationManager.requestLocationUpdates(
                locationManager.getBestProvider(fine, true),
                1000, 1, listenerFine);

        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,listenerFine);
    }

    private void createLocationListener() {

        //Creazione del location listener
        listenerFine = new LocationListener() {
            public void onStatusChanged(String provider,
                                        int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            //azione da eseguire ad ogni variazione della posizione
            public void onLocationChanged(Location location) {

                for(int i = 0; i<posMarkArray.size(); i++){
                    double tmp = i*0.00003;
                    posMarkArray.set(i, new LatLng(location.getLatitude()+tmp, location.getLongitude()+tmp));
                }

                if (googleMap != null) {

                    //Focalizziamo la mappa su un punto prefissato
                    if(!posMarkArray.isEmpty()) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posMarkArray.get(0), 15));
                    }else{
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.524940, 15.073690), 15));
                    }

                    //Rimuoviamo vecchio marker
                    for(Marker tmp : markerArray){
                        tmp.remove();
                    }

                    //Aggiungiamo il marker per la nuova posizione
                    for(int i = 0; i < posMarkArray.size() ; i++){
                        markerArray.add(googleMap.addMarker(new MarkerOptions()
                                .position(posMarkArray.get(i)).title("Marker_"+i)));
                    }

                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerLocationListener();
        initilizeMap();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //Se l'applicazione lascia il foreground
        //cancello la sottoscrizione al location listener
        locationManager.removeUpdates(listenerFine);

    }

    @Override
    public void onFragmentInteraction(String cmd, String[] arguments) {
        Log.d("onFragmentInteraction", cmd);
        switch(cmd){
            case "traceLinea":
                String message = "città: " + arguments[0] + " linea: " + arguments[1];
                Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                toast.show();
                break;

        }
    }
}
