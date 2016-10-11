package com.example.tanmay.zomato;

import java.io.Serializable;

/**
 * Created by Tanmay on 24-09-2016.
 */
public class photoUrl implements Serializable{


    private String[] photoReference;
    private int photoNumber;
    public photoUrl(String[] photo,int photoNumber){
        photoReference=photo;
        this.photoNumber=photoNumber;
    }
    public int getPhotoNumber(){
        return photoNumber;
    }
    public String[] getPhotoLocation(){
     //  String[] mainUrl1={"https://static.pexels.com/photos/5249/bread-food-restaurant-people-medium.jpg","https://static.pexels.com/photos/5249/bread-food-restaurant-people-medium.jpg", "https://static.pexels.com/photos/5249/bread-food-restaurant-people-medium.jpg"};
       // String mainUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photoReference+"&key="+new MainActivity().api_key;
        return  photoReference;
    }
}
