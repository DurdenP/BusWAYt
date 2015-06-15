package app.durdenp.com.buswayt.mapUtility;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Marco on 18/05/2015.
 */
public class FermataDescriptor {

    private String id;
    private String nome;
    private String lineeTransito;
    private LatLng coordinates;

    /*Constructor*/
    public FermataDescriptor(){
        nome = null;
        id = null;
        lineeTransito = null;
        coordinates = new LatLng(0.0, 0.0);
    }


    /*Constructor*/
    public FermataDescriptor(String nome, String id, String lineeTransito){
        this.nome = nome;
        this.id = id;
        this.lineeTransito = lineeTransito;
        this.coordinates = null;
    }

    /*Constructor*/
    public FermataDescriptor(String nome, String id, String lineeTransito, LatLng coordinates){
        this.nome = nome;
        this.id = id;
        this.lineeTransito = lineeTransito;
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

    /**
     *
     * @return
     */
    public String getLineeTransito() {
        return lineeTransito;
    }

    /**
     *
     * @param lineeTransito
     */
    public void setLineeTransito(String lineeTransito) {
        this.lineeTransito = lineeTransito;
    }

}
