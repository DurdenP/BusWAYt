package app.durdenp.com.buswayt.mapUtility;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import app.durdenp.com.buswayt.mapUtility.FermataDescriptor;

/**
 * Created by Marco on 18/05/2015.
 */

public class LineaDescriptor {
    private String label;
    private String id;
    private LinkedList<FermataDescriptor> busStops;
    private LinkedList<LatLng> route;
    private boolean readyToBePrinted;

    /*Probabilemente non servono*/
    private HashMap<Integer, LinkedList<LatLng>> pCoord;
    private HashMap<Integer, LinkedList<LatLng>> fCoord;

    /**
     * Default constructor
     */
    public LineaDescriptor(){
        label = null;
        id = null;
        busStops = new LinkedList<>();
        route = new LinkedList();
        pCoord = new HashMap();
        fCoord = new HashMap();
        readyToBePrinted = false;
    }

    /**
     *
     * @param label
     * @param id
     */
    public LineaDescriptor(String label, String id) {
        this();
        this.label = label;
        this.id = id;
    }

    /**
     *
     * @param label
     * @param id
     * @param busStops
     */
    public LineaDescriptor(String label, String id, LinkedList<FermataDescriptor> busStops) {
        this(label, id);
        this.busStops = busStops;
    }

    /**
     *
     * @param label
     * @param id
     * @param busStops
     * @param route
     */
    public LineaDescriptor(String label, String id, LinkedList<FermataDescriptor> busStops, LinkedList<LatLng> route) {
        this(label, id, busStops);
        this.route = route;
    }

    //Getter and setter methos

    /**
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public LinkedList<FermataDescriptor> getBusStops() {
        return busStops;
    }

    /**
     *
     * @param busStops
     */
    public void setBusStops(LinkedList<FermataDescriptor> busStops) {
        this.busStops = busStops;
    }

    /**
     *
     * @return
     */
    public HashMap<Integer, LinkedList<LatLng>> getpCoord() {
        return pCoord;
    }

    /**
     *
     * @param pCoord
     */
    public void setpCoord(HashMap<Integer, LinkedList<LatLng>> pCoord) {
        this.pCoord = pCoord;
    }

    /**
     *
     * @return
     */
    public HashMap<Integer, LinkedList<LatLng>> getfCoord() {
        return fCoord;
    }

    /**
     *
     * @param fCoord
     */
    public void setfCoord(HashMap<Integer, LinkedList<LatLng>> fCoord) {
        this.fCoord = fCoord;
    }

    /**
     *
     * @param busStop
     * @param location
     */
    public void addBusStop(FermataDescriptor busStop, int location){
        busStops.add(location, busStop);
    }

    /**
     *
     * @return
     */
    public LinkedList<LatLng> getRoute() {
        return route;
    }

    /**
     *
     * @param route
     */
    public void setRoute(LinkedList<LatLng> route) {
        this.route = route;
    }

    /**
     *
     * @return
     */
    public boolean isReadyToBePrinted() {
        return readyToBePrinted;
    }

    /**
     *
     * @param readyToBePrinted
     */
    public void setReadyToBePrinted(boolean readyToBePrinted) {
        this.readyToBePrinted = readyToBePrinted;
    }

    /**
     *
     * @param id
     * @return
     */
    public int removeBusStop(String id){
        ListIterator<FermataDescriptor> it = this.busStops.listIterator();
        while(it.hasNext()){
            FermataDescriptor tmp = it.next();
            if(tmp.getId().compareTo(id) == 0){
                it.remove();
                return 0;
            }
        }

        return 1;
    }


}
