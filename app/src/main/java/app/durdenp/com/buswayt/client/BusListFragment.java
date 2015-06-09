package app.durdenp.com.buswayt.client;

import android.app.Activity;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import app.durdenp.com.buswayt.R;
import app.durdenp.com.buswayt.mapUtility.BusDescriptor;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class BusListFragment extends ListFragment implements AbsListView.OnItemClickListener {

    private ArrayList<BusListItem> busListItem;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "page";
    private static final String ARG_PARAM2 = "title";

    private int mPage;
    private String mTitle;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private BusListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static BusListFragment newInstance(int page, String title) {
        BusListFragment fragment = new BusListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BusListFragment() {
    }
    ArrayList<BusDescriptor> busDescriptorList;

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
        View view = inflater.inflate(R.layout.fragment_buslistfragment_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);

        ClientActivity activity = (ClientActivity)getActivity();

        busDescriptorList = activity.getBusList();
        busListItem = new ArrayList();

        if(!busDescriptorList.isEmpty()){
            for(BusDescriptor bus : busDescriptorList){
                BusListItem tmp = new BusListItem(bus.getId(), bus.getLinea(), bus.getSpeed(), bus.getPrevArrivalTime(), bus.getNextBusStop().getNome());
                busListItem.add(tmp);
            }
        }

        mAdapter = new BusListAdapter(getActivity(), busListItem);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("BUSLISTFRAGMENT", "onItemClick");
        if (null != mListener) {
            Log.d("BUSLISTFRAGMENT", "mListener != null");
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //TODO fare come nell'altro fragment
            BusListItem item = this.busListItem.get(position);
            mListener.onFragmentInteraction(item.toString());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    public void refreshBusItemList(ArrayList<BusDescriptor> busDescriptorListRefr){

        mAdapter.clear();

        busListItem = new ArrayList();

        if(!busDescriptorList.isEmpty()){
            for(BusDescriptor bus : busDescriptorList){
                BusListItem tmp = new BusListItem(bus.getId(), bus.getLinea(), bus.getSpeed(), bus.getPrevArrivalTime(), bus.getNextBusStop().getNome());
                busListItem.add(tmp);
            }
        }

        mAdapter.notifyDataSetChanged();
        /*
         if(getActivity() != null) {
            //mAdapter = new BusListAdapter(getActivity(), busListItem);
            mAdapter = new BusListAdapter(getActivity(), busListItem);
            if(mListView != null) {
                mListView.setAdapter(mAdapter);
            }
            Log.w("refreshBusItemList", "activity is != NULL");
        }else{
            Log.w("refreshBusItemList", "activity is NULL");
        }
        */
    }
}
