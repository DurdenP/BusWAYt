package app.durdenp.com.buswayt.client;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import app.durdenp.com.buswayt.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestLineaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestLineaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestLineaFragment extends Fragment implements Button.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "page";
    private static final String ARG_PARAM2 = "title";

    private int mPage;
    private String mTitle;

    private Button traceLineaButton;
    private Spinner citySpinner;
    private Spinner lineaSpinner;

    ArrayAdapter<CharSequence> citySpinnerAdapter;
    ArrayAdapter<CharSequence> lineaSpinnerAdapter;
    private String[] lineaBusArray;

    private String citySelected;
    private String lineaSelected;

    private OnFragmentInteractionListener mListener;

    /**
     *
     * @param page
     * @param title
     * @return
     */
    public static RequestLineaFragment newInstance(int page, String title) {
        RequestLineaFragment fragment = new RequestLineaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    public RequestLineaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PARAM1);
            mTitle = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_linea, container, false);

        citySpinner = (Spinner) view.findViewById(R.id.cityChooseSpinner);

        citySpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.city_array, android.R.layout.simple_spinner_item);
        citySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(citySpinnerAdapter);
        citySpinner.setOnItemSelectedListener(new CitySpinnerListener());

        lineaSpinner = (Spinner) view.findViewById(R.id.lineaChooseSpinner);
        lineaBusArray = getResources().getStringArray(R.array.linea_nessunaCitta_array);



        lineaSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, lineaBusArray);
        lineaSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineaSpinner.setAdapter(lineaSpinnerAdapter);
        lineaSpinner.setOnItemSelectedListener(new LineaSpinnerListener());

        traceLineaButton = (Button)view.findViewById(R.id.traceLineaBtn);
        traceLineaButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            Log.d("RequestLineaFragment", "OnAttachMethod");
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v){
        int buttonId = v.getId();

        switch(buttonId){
            case R.id.traceLineaBtn:
                String[] argumentsToPass = {citySelected, lineaSelected};
                sendGet();
                mListener.onFragmentInteraction("traceLinea", argumentsToPass);
                break;
        }
    }

    private void changeCitySpinnerAdapter(String city){
        /*Abbiamo una sola regione quindi non fa nulla*/
    }

    private void changeLineaSpinnerAdapter(){
        String msg = "citta' selezionata: " + citySelected;
        Log.d("changeLineaSpinner", msg);
        switch(citySelected){
            case "Catania":
                Log.d("changeLineaSpinner", "CASE: catania");
                lineaBusArray = getResources().getStringArray(R.array.linea_catania_array);
                break;
            case "Messina":
                Log.d("changeLineaSpinner", "CASE: messina");
                lineaBusArray = getResources().getStringArray(R.array.linea_messina_array);

                break;
            case "Palermo":
                Log.d("changeLineaSpinner", "CASE: palermo");
                lineaBusArray = getResources().getStringArray(R.array.linea_palermo_array);
                break;
        }

        lineaSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, lineaBusArray);
        lineaSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineaSpinner.setAdapter(lineaSpinnerAdapter);
    }

    /*MARCO*/
    private void sendGet() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://www.amt.ct.it/MappaLinee/leggifermate.php?linee=BRT1%3B";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getActivity(),
                                "Response is:" + response.substring(0,500), Toast.LENGTH_SHORT)
                                .show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "That didn't work!", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        queue.add(stringRequest);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String cmd, String[] arguments);

    }


    /*Classe che implementa il listener per lo spinner selezione citta'*/
    protected class CitySpinnerListener implements Spinner.OnItemSelectedListener{

        /*costruttore*/
        public CitySpinnerListener(){

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            citySelected = citySpinnerAdapter.getItem(position).toString();
            changeLineaSpinnerAdapter();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    /*Classe che implementa il listener per lo spinner selezione linea*/
    protected class LineaSpinnerListener implements Spinner.OnItemSelectedListener{

        /*costruttore*/
        public LineaSpinnerListener(){

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            lineaSelected = lineaSpinnerAdapter.getItem(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
