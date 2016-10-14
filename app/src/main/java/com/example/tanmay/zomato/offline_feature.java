package com.example.tanmay.zomato;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Tanmay on 14-10-2016.
 */
public class offline_feature {
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    private Context context;
    Gson gson = new GsonBuilder().create();
    public final static String Preferences = "MyPrefs9";
    public offline_feature(Context parent){
        context=parent;
        prefs = parent.getSharedPreferences(Preferences, Context.MODE_PRIVATE|Context.MODE_APPEND);
        edit = prefs.edit();
    }

    public Favourites getofflineFavourites(){
        String s = prefs.getString("main_object", "Not Available");
        if(s.equals("Not Available")){
            Toast.makeText(context,"You still Don't have any Favourites",Toast.LENGTH_LONG).show();
            return null;
        }else {
            Favourites f = gson.fromJson(s, Favourites.class);
            return f;
        }
    }
}
