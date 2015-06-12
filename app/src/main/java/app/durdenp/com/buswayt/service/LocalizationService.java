package app.durdenp.com.buswayt.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.durdenp.com.buswayt.bus.CustomRequest;

public class LocalizationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // LogCat tag
    private static final String TAG = LocalizationService.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private boolean mRequestingLocationUpdates = false;

    String address="http://151.97.157.157:8080/";


    private LocationRequest mLocationRequest;

    String lineaid;
    String busid;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public LocalizationService getService() {
            return LocalizationService.this;
        }
    }


    @Override
    public void onCreate(){

        // First we need to check availability of play services
        if (checkPlayServices()) {

            System.out.println("Dentro checkPlayServices");
            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        Toast.makeText(getApplicationContext(),
                "Il Localization service e' partito", Toast.LENGTH_LONG).show();

    }




    @Override
    public void onStart(Intent intent, int id){



    }



    @Override
    public void onDestroy(){
        Toast.makeText(getApplicationContext(),

                "Il service e' stato terminato", Toast.LENGTH_LONG).show();


    }

    @Override
    public IBinder onBind(Intent intent) {

        Bundle extras = intent.getExtras();
        lineaid= extras.getString("lineaid");
        busid=extras.getString("busid");

        return mBinder;
    }




    @Override
    public void onConnected(Bundle bundle) {
        // Once connected with google api, get the location

        //displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();

    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            float speed = mLastLocation.getSpeed();

            Toast.makeText(getApplicationContext(),
                    "Lineaid: "+ lineaid +", "+" Busid: "+ busid +", "+" Latitude: "+ latitude +", "+ " Longitude: "+ longitude +", "+" Speed: " + speed, Toast.LENGTH_LONG)
                    .show();

            //sendHttpRquest(String busid, String lineaid, double latitude, double longitude, float speed)

            sendHttpRequest(busid,lineaid, latitude, longitude,speed);

        } else {


            Toast.makeText(getApplicationContext(),
                    "(Couldn't get the location. Make sure location is enabled on the device)", Toast.LENGTH_LONG)
                    .show();
        }
    }


    public void sendHttpRequest(String busid, String lineaid, double latitude, double longitude, float speed)
    {
        String url =address+"businfo";

        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<>();
        params.put("speed", String.valueOf(speed));
        params.put("longitude", String.valueOf(longitude));
        params.put("latitude",String.valueOf(latitude));
        params.put("busid", busid);
        params.put("lineaid", lineaid);

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.PUT, url, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());

                Toast.makeText(getApplicationContext(),
                        "Response: "+ response.toString(), Toast.LENGTH_LONG)
                        .show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Error Response: ", response.toString());
            }
        });

        queue.add(jsObjRequest);



    }




    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());

    }






    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();

                stopSelf(); /* kill service*/
            }
            return false;
        }
        return true;
    }





    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        System.out.println("buildGoogleApiClient");

    }


    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        System.out.println("createLocationRequest");
    }











    /**
     * Starting the location updates
     * */
    public void startLocationUpdates() {
        Log.d(TAG, "Periodic location updates started!");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        mRequestingLocationUpdates = true;

    }

    /**
     * Stopping location updates
     */
    public void stopLocationUpdates() {
        mRequestingLocationUpdates = false;
        Log.d(TAG, "Periodic location updates stopped!");

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    }











