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

    /**
     * Default Constructor
     */
    public BusDescriptor(){
        this.id = null;
        this.linea = null;
        this.speed = 0;
        this.coordinates = new LatLng(0.0, 0.0);
    }

    /**
     *
     * @param id
     */
    public BusDescriptor(String id){
        this.id = id;
        this.linea = linea;
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
}
