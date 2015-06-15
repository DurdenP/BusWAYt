package app.durdenp.com.buswayt.mapUtility;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Marco on 18/05/2015.
 */
public class BusDescriptor {
    private String id;
    private String linea;
    private LatLng coordinates;
    private double speed;
    private FermataDescriptor lastBusStop;
    private FermataDescriptor nextBusStop;

    /**
     * Default Constructor
     */
    public BusDescriptor(){
        this.id = null;
        this.linea = null;
        this.speed = 0;
        this.coordinates = new LatLng(0.0, 0.0);
        this.lastBusStop = null;
        this.nextBusStop = null;
    }

    /**
     *
     * @param id
     */
    public BusDescriptor(String id){
        this.id = id;
        this.coordinates = new LatLng(0.0, 0.0);
        this.speed = 0;
    }

    /**
     *
     * @param id
     * @param linea
     */
    public BusDescriptor(String id, String linea){
        this(id);
        this.linea = linea;
        this.coordinates = new LatLng(0.0, 0.0);
        this.speed = 0;
        this.lastBusStop = null;
        this.nextBusStop = null;
    }

    /**
     *
     * @param id
     * @param linea
     * @param coordinates
     */
    public BusDescriptor(String id, String linea, LatLng coordinates){
        this(id, linea);
        this.coordinates = coordinates;
        this.speed = 0;
        this.lastBusStop = null;
        this.nextBusStop = null;
    }

    /**
     *
     * @param id
     * @param linea
     * @param coordinates
     * @param speed
     */
    public BusDescriptor(String id, String linea, LatLng coordinates, double speed){
        this(id, linea, coordinates);
        this.speed = speed;
        this.lastBusStop = null;
        this.nextBusStop = null;
    }

    /**
     *
     * @param id
     * @param linea
     * @param coordinates
     * @param speed
     * @param lastBusStop
     */
    public BusDescriptor(String id, String linea, LatLng coordinates, double speed, FermataDescriptor lastBusStop){
        this(id, linea, coordinates, speed);
        this.lastBusStop = lastBusStop;
        this.nextBusStop = null;
    }

    /**
     *
     * @param id
     * @param linea
     * @param coordinates
     * @param speed
     * @param lastBusStop
     * @param nextBusStop
     */
    public BusDescriptor(String id, String linea, LatLng coordinates, double speed, FermataDescriptor lastBusStop, FermataDescriptor nextBusStop){
        this(id, linea, coordinates, speed, lastBusStop);
        this.nextBusStop = nextBusStop;
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
    public String getLinea() {
        return linea;
    }

    /**
     *
     * @param linea
     */
    public void setLinea(String linea) {
        this.linea = linea;
    }

    /**
     *
     * @return
     */
    public LatLng getCoordinates() {
        return coordinates;
    }

    /**
     *
     * @param coordinates
     */
    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    /**
     *
     * @return
     */
    public double getSpeed() {
        return speed;
    }

    /**
     *
     * @param speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     *
     * @return
     */
    public FermataDescriptor getLastBusStop() {
        return lastBusStop;
    }

    /**
     *
     * @param lastBusStop
     */
    public void setLastBusStop(FermataDescriptor lastBusStop) {
        this.lastBusStop = lastBusStop;
    }

    /**
     *
     * @return
     */
    public FermataDescriptor getNextBusStop() {
        return nextBusStop;
    }

    /**
     *
     * @param nextBusStop
     */
    public void setNextBusStop(FermataDescriptor nextBusStop) {
        this.nextBusStop = nextBusStop;
    }

    /**
     *
     * @return expected arrival time to next busStop
     */
    public int getPrevArrivalTime(){
        //TODO implement this function
        return 0;
    }

    private double getDistanceFromNextBusStop(){
        //TODO implment this method
        return 0.0;
    }

    @Override
    public String toString() {
        return "BusDescriptor{" +
                "id='" + id + '\'' +
                ", linea='" + linea + '\'' +
                ", coordinates=" + coordinates +
                ", speed=" + speed +
                ", lastBusStop=" + lastBusStop +
                ", nextBusStop=" + nextBusStop +
                '}';
    }
}
