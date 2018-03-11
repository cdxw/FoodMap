package com.myfoodmapapp.myfoodmapapp.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Chris on 30/8/17.
 */

public class MapData implements Serializable{


    private String date;
    private Foods foods;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public Foods getFoods() {
        return foods;
    }

    public void setFoods(Foods foods) {
        this.foods = foods;
    }


}
