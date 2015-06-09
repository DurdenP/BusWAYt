package app.durdenp.com.buswayt.bus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.durdenp.com.buswayt.R;

public class BusConfigActivity extends ActionBarActivity {
    String address="http://151.97.157.200:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_config);
        loadSavedPreferences();
    }

    //Metodo per il caricamento delle shared preferences
    private void loadSavedPreferences() {
        SharedPreferences sp =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX", false);
        String busid = sp.getString("BUSID", "");
        String linea=sp.getString("LINEA", "");

        CheckBox checkBox = (CheckBox) findViewById(R.id.saveCheckBox);




        if(cbValue == true){
            checkBox.setChecked(true);
        }



        else
            checkBox.setChecked(false);

        EditText busText = (EditText) findViewById(R.id.busIdEditText);
        EditText lineaText = (EditText) findViewById(R.id.LineaIdEditText2);

        busText.setText(busid);
        lineaText.setText(linea);


    }


    public void startLocalizationService(View v)
    {
        startSavedPreferences(v);



    }


    public void startSavedPreferences(View v)
    {
        CheckBox checkBox = (CheckBox) findViewById(R.id.saveCheckBox);
        EditText bus=null;
        EditText linea=null;

        savePreferences("CHECKBOX", checkBox.isChecked());

        if (checkBox.isChecked()){
             bus = (EditText) findViewById(R.id.busIdEditText);
             linea= (EditText)findViewById(R.id.LineaIdEditText2);


            savePreferences("BUSID", bus.getText().toString());
            savePreferences("LINEA", linea.getText().toString());
            Toast.makeText(this, "Preferenze salvate", Toast.LENGTH_LONG).show();
        }

        else{
            Toast.makeText(this, "Preferenze NON salvate", Toast.LENGTH_LONG).show();
        }



        String busid=bus.getText().toString();
        String lineaid=linea.getText().toString();
        sendHttpRequest("linebusmapping", busid, lineaid);


        Intent autobus= new Intent(this, AutobusActivity.class);
        autobus.putExtra("lineaid",lineaid);
        autobus.putExtra("busid",busid);
        startActivity(autobus);

        /*bind to sLocalization Service*/


    }



    public void sendHttpRequest(String path, String busid, String lineaid)  {
        String url =address+path;

        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("busid", busid);
        params.put("lineaid", lineaid);

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

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







    private void savePreferences(String key, boolean value) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //Metodo per il salvataggio di una stringa in shared preferences
    private void savePreferences(String key, String value) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bus_config, menu);
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
