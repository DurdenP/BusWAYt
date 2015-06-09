package app.durdenp.com.buswayt.jsonWrapper;

/**
 * Created by Marco on 09/06/2015.
 */
public class BusPositionWrapper {
    private String id;
    private String linea;
    private double lat;
    private double longi;
    private double vel;

    /**
     * Defaul Constructor
     */
    public BusPositionWrapper() {
    }

    /**
     *
     * @param id
     * @param linea
     * @param lat
     * @param longi
     * @param vel
     */
    public BusPositionWrapper(String id, String linea, double lat, double longi, double vel) {
        this.id = id;
        this.linea = linea;
        this.lat = lat;
        this.longi = longi;
        this.vel = vel;
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
    public double getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     */
    public double getLongi() {
        return longi;
    }

    /**
     *
     * @param longi
     */
    public void setLongi(double longi) {
        this.longi = longi;
    }

    /**
     *
     * @return
     */
    public double getVel() {
        return vel;
    }

    /**
     *
     * @param vel
     */
    public void setVel(double vel) {
        this.vel = vel;
    }
}
