package com.example.tanmay.zomato;

import java.io.Serializable;

/**
 * Created by Tanmay on 26-09-2016.
 */
public class review_holder implements Serializable{

    private String author_url, author_name;
    private double rating;
    private String profile_pic_url;
    private long unix_timestamp;
    private String mainText;

    public review_holder(String author_name,String author_url,double rating, String profile_pic_url, long unix_timestamp, String mainText){
        this.author_url=author_url;
        this.profile_pic_url=profile_pic_url;
        this.unix_timestamp = unix_timestamp;
        this.mainText=mainText;
        this.rating=rating;
        this.author_name=author_name;
    }

    public String getAuthor_url(){return author_url;}
    public double getRating(){return rating;}
    public String getProfile_pic_url(){return profile_pic_url;}
    public String getMainText(){return mainText;}
    public long getUnix_timestamp(){return unix_timestamp;}
    public String getAuthor_name(){return author_name;}
}
