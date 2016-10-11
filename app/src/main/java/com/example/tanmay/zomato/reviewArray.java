package com.example.tanmay.zomato;

import java.io.Serializable;

/**
 * Created by Tanmay on 26-09-2016.
 */
public class reviewArray implements Serializable{
    private review_holder[] holder;
    private int actual_length;

    public reviewArray(review_holder[] holder,int actual_length){
        this.holder=holder;
        this.actual_length=actual_length;

    }

    public review_holder[] getHolder(){return holder;}
    public int getActual_length(){return actual_length;}
}
