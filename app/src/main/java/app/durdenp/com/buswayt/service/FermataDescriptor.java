package app.durdenp.com.buswayt.service;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Marco on 18/05/2015.
 */
public class FermataDescriptor {

    private String id;
    private String nome;
    private LatLng coordinates;

    /*Constructor*/
    public FermataDescriptor(){
        nome = null;
        id = null;
        coordinates = new LatLng(0.0, 0.0);
    }

    /*Constructor*/
    public FermataDescriptor(String nome, String id, LatLng coordinates){
        this.nome = nome;
        this.id = id;
        this.coordinates = coordinates;
    }

    /**
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
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

}
