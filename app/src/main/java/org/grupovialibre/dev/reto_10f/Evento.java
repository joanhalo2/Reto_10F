package org.grupovialibre.dev.reto_10f;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.HashMap;
import java.util.Map;

import static android.R.attr.description;

/**
 * Created by joan on 29/11/16.
 */

public class Evento implements Parcelable {

    private String location;
    private String evento;
    private String tipoEvento;
    private String municipio;
    private String sitio;

    public Evento(String location, String evento, String tipoEvento, String municipio, String sitio) {
        this.setLocation(location);
        this.setEvento(evento);
        this.setTipoEvento(tipoEvento);
        this.setMunicipio(municipio);
        this.setSitio(sitio);
    }

    public Evento(){

    }

    public Evento(Parcel input){
        this.location = input.readString();
        this.evento = input.readString();
        this.tipoEvento = input.readString();
        this.municipio = input.readString();
        this.sitio = input.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(evento);
        dest.writeString(tipoEvento);
        dest.writeString(municipio);
        dest.writeString(sitio);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("location", location);
        result.put("evento", evento);
        result.put("tipoevento", tipoEvento);
        result.put("municipio", municipio);
        result.put("sitio", sitio);

        return result;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
    }

    @Override
    public int describeContents() {
        return 0;
    }





    public static final Parcelable.Creator<Evento> CREATOR
            = new Parcelable.Creator<Evento>() {
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };
}
