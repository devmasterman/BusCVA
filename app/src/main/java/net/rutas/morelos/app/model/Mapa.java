package net.rutas.morelos.app.model;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by eroman on 18/10/16.
 */
@Table
public class Mapa extends SugarRecord {

    //private Long id;

    //private String route;
    //private String json;

    private String route;
    private String json;

    public Mapa(){

    }

    public Mapa(String route, String json){
        this.route=route;
        this.json=json;

    }

    /*public Long getId() {
        return id;
    }*/

    public String getRoute() {
        return route;
    }

    public String getJson() {
        return json;
    }
}