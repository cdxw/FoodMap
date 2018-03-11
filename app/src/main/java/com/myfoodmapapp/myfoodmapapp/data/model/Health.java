package com.myfoodmapapp.myfoodmapapp.data.model;

import java.util.Date;

/**
 * Created by chris on 24/8/17.
 */

public class Health {
    public static final String TAG = Health.class.getSimpleName();
    public static final String TABLE = "Health";

    public static final String Key_UserId = "UserId";
    public static final String Key_Date = "Date";
    public static final String Key_Height = "Height";
    public static final String Key_Weight = "Weight";
    public static final String Key_BMI = "BMI";

    private Integer userId;
    private String date;
    private Double height;
    private Double  weight;
    private Double  bmi;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double  getHeight() {
        return height;
    }

    public void setHeight(Double  height) {
        this.height = height;
    }

    public Double  getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight= weight;
    }

    public Double  getBMI() {
        return bmi;
    }

    public void setBMI(Double bmi) {
        this.bmi = bmi;
    }

}
