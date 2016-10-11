package com.example.tanmay.zomato;

import android.support.design.widget.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tanmay on 08-10-2016.
 */
public class Favourites implements Serializable {
   private List<Restauraunt> list;
    private List<photoUrl> p;
   private List<reviewArray> arr;
    public Favourites(List<Restauraunt> list, List<photoUrl> p, List<reviewArray> arr){
        this.list=list;
        this.p= p;
        this.arr = arr;
    }

    public List<Restauraunt> getList(){return list;};
    public List<photoUrl> getP(){return p;}
    public List<reviewArray> getArr(){return arr;}
}
