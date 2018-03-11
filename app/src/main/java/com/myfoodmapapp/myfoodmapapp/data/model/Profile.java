package com.myfoodmapapp.myfoodmapapp.data.model;

/**
 * Created by chris on 24/8/17.
 */

public class Profile {

    public static final String TAG = Profile.class.getSimpleName();
    public static final String TABLE = "Profile";


    //public static final String Key_UserId = "UserId";
    public static final String Key_Name = "Name";
    public static final String Key_ImagePath = "ImagePath";

    private Integer userId;
    private String name;
    private String imagePath;


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public Integer getUserId() {
        return userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
