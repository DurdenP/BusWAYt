package app.durdenp.com.buswayt.client;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Marco on 25/05/2015.
 */
public class BusListItem {

    //Declaration of info to show in the view
    private String id;
    private String linea;
    private double speed;
    private int arrivoPrevisto;

    //Constructors


    /**
     *
     */
    public BusListItem() {
        this.id = null;
        this.linea = null;
        this.speed = 0;
        this.arrivoPrevisto = 0;
    }

    /**
     *
     * @param id
     */
    public BusListItem(String id) {
        this();
        this.id = id;
    }

    /**
     *
     * @param id
     * @param linea
     */
    public BusListItem(String id, String linea) {
        this(id);
        this.linea = linea;
    }

    /**
     *
     * @param id
     * @param linea
     * @param speed
     */
    public BusListItem(String id, String linea, double speed) {
        this(id, linea);
        this.speed = speed;
    }

    /**
     *
     * @param id
     * @param linea
     * @param speed
     * @param arrivoPrevisto
     */
    public BusListItem(String id, String linea, double speed, int arrivoPrevisto) {
        this(id, linea, speed);
        this.arrivoPrevisto = arrivoPrevisto;
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
    public int getArrivoPrevisto() {
        return arrivoPrevisto;
    }

    /**
     *
     * @param arrivoPrevisto
     */
    public void setArrivoPrevisto(int arrivoPrevisto) {
        this.arrivoPrevisto = arrivoPrevisto;
    }

    @Override
    public String toString(){
        String tmpToreturn = "";
        tmpToreturn = tmpToreturn + "Id: " + this.id + " ";
        tmpToreturn = tmpToreturn + "Linea: " + this.linea + " ";
        tmpToreturn = tmpToreturn + "Speed: " + this.speed + " ";
        tmpToreturn = tmpToreturn + "ArrPrev: " + this.arrivoPrevisto + " ";

        return tmpToreturn;
    }
}
