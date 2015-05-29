package app.durdenp.com.buswayt.jsonWrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Marco on 26/05/2015.
 */
public class FermateWrapper {
    @SerializedName("Codice") String codice;
    @SerializedName("Descrizione") String descrizione;
    @SerializedName("Latitudine") double latitude;
    @SerializedName("Longitudine") double longitude;
    @SerializedName("LineeTransito") String lineeTransito;
    int ps;

    /**
     *
     * @param codice
     * @param descrizione
     * @param latitude
     * @param longitude
     * @param lineeTransito
     * @param ps
     */
    public FermateWrapper(String codice, String descrizione, double latitude, double longitude, String lineeTransito, int ps) {
        this.codice = codice;
        this.descrizione = descrizione;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lineeTransito = lineeTransito;
        this.ps = ps;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLineeTransito() {
        return lineeTransito;
    }

    public void setLineeTransito(String lineeTransito) {
        this.lineeTransito = lineeTransito;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    @Override
    public String toString() {
        return "FermateWrapper{" +
                "codice='" + codice + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", lineeTransito='" + lineeTransito + '\'' +
                ", ps=" + ps +
                '}';
    }
}
