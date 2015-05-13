package app.durdenp.com.buswayt.client;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.durdenp.com.buswayt.R;


public class ClientMapOption extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Associamo al Fragment il layout R.layout.primo
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_client_map_option, null);
        return root;
    }

}
