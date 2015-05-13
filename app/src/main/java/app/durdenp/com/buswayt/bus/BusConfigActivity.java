package app.durdenp.com.buswayt.bus;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import app.durdenp.com.buswayt.R;

public class BusConfigActivity extends ActionBarActivity {

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

        savePreferences("CHECKBOX", checkBox.isChecked());

        if (checkBox.isChecked()){
            EditText bus = (EditText) findViewById(R.id.busIdEditText);
            EditText linea= (EditText)findViewById(R.id.LineaIdEditText2);


            savePreferences("BUSID", bus.getText().toString());
            savePreferences("LINEA", linea.getText().toString());
            Toast.makeText(this, "Preferenze salvate", Toast.LENGTH_LONG).show();
        }

        else{
            Toast.makeText(this, "Preferenze NON salvate", Toast.LENGTH_LONG).show();
        }


        /*bind to sLocalization Service*/


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
