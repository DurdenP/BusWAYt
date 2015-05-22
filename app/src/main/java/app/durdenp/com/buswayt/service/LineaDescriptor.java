package app.durdenp.com.buswayt.service;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Marco on 18/05/2015.
 */
public class LineaDescriptor {
    private String label;
    private String id;
    private LinkedList<FermataDescriptor> busStops;


    /**
     * Default constructor
     */
    public LineaDescriptor(){
        label = null;
        id = null;
        busStops = new LinkedList<>();
    }

    /**
     * Constructor
     *
     * @param label
     * @param id
     */
    public LineaDescriptor(String label, String id){
        this.label = label;
        this.id = id;
        this.busStops = new LinkedList();
    }

    /**
     * Constructor
     *
     * @param label
     * @param id
     * @param busStops
     */
    public LineaDescriptor(String label, String id, LinkedList<FermataDescriptor> busStops){
        this.label = label;
        this.id = id;
        this.busStops = busStops;
    }

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
     * @param busStop
     * @param location
     */
    public void addBusStop(FermataDescriptor busStop, int location){
        busStops.add(location, busStop);
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
