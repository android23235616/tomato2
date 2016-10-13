package com.example.tanmay.zomato;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Tanmay on 16-09-2016.
 */
public class Restauraunt implements Serializable {

    private String name,vicinity,rating,ImageUrl,number,type, open_close,place_id, full_title, full_vicinity, full_types;

    private double lat, lng;

    public Restauraunt(String n,String v,String r, String u,String num, String type,double lat, double lng ,String open_close, String id, String ft, String fv, String ftypes){
        name=n;
        vicinity=v;
        rating=r;
        ImageUrl=u;
        number = num;
        this.type=type;
        this.lat=lat;
        this.lng=lng;
        this.open_close=open_close;
        this.place_id=id;
        full_title=ft;
        full_vicinity=fv;
        full_types=ftypes;
    }



    public String getName(){
        return name;
    }
    public String getVicinity(){
        return vicinity;

    }
    public String getRating(){
        return rating;
    }
    public String getImageUrl(){
        return ImageUrl;
    }
    public String getNumber(){return number;}
    public String getType(){return type;}
    public double getLatitude(){return lat;}
    public double getLongitude(){return lng;}
    public String getOpen_close(){return open_close;}
    public String getPlace_id(){return place_id;}
    public String getFull_title(){return full_title;}
    public String getFull_vicinity(){return full_vicinity;}
    public String getFull_types(){return full_types;}
  //  public LatLng getLtlg(){return ltlg;}
}
