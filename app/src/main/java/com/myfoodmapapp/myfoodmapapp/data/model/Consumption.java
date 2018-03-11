package com.myfoodmapapp.myfoodmapapp.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chris on 24/8/17.
 */

public class Consumption {
    public static final String TAG = Consumption.class.getSimpleName();
    public static final String TABLE = "Consumption";

    public static final String Key_Date = "DATE";
    public static final String Key_Cholesterol= "CHOLESTEROL";
    public static final String Key_Calories = "CALORIES";
    public static final String Key_TotalFats = "TOTALFATS";
    public static final String Key_FoodName = "FOODNAME";

    private String date;
    private Double cholesterol;
    private Double totalFats;
    private Double calories;
    private String queryName;


    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String foodName) {
        this.queryName = foodName;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getTotalFats() {
        return totalFats;
    }

    public void setTotalFats(Double totalFats) {
        this.totalFats = totalFats;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories= calories;
    }

}
