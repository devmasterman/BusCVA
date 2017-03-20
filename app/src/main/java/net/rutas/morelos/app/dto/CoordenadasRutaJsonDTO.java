package net.rutas.morelos.app.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eroman on 8/06/16.
 */
public class CoordenadasRutaJsonDTO {
    private String transport;
    private List<CoordenadasDTO> coordinates;

    public CoordenadasRutaJsonDTO() {
        this.coordinates =  new ArrayList<CoordenadasDTO>();
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public List<CoordenadasDTO> getCoordinates() {
        return coordinates;
    }

    public void addCoordinates(CoordenadasDTO coordinate) {
        if(this.coordinates==null)
            this.coordinates = new ArrayList<CoordenadasDTO>();
        this.coordinates.add(coordinate);
    }
}
