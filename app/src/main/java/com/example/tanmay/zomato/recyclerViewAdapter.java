package com.example.tanmay.zomato;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Tanmay on 16-09-2016.
 */
public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.MyViewHolderClass> {
    Restauraunt re;
    private Context context;
    List<Restauraunt> list, Flist=new ArrayList<>();
    List<photoUrl> photoUrlList,FphotoUrlList=new ArrayList<>();
    List<reviewArray> arrays, Farrays=new ArrayList<>();
    static int index=0;
    static int toggle=0;
    Animation animation;
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    final String nullRating="98187022332";
    SharedPreferences prefs;
    String favourite_status= "";
    public final static String Preferences = "MyPrefs9";
    SharedPreferences.Editor edit;
    static int fileCounter=0;
    public  recyclerViewAdapter(List<Restauraunt> list, List<photoUrl> p, List<reviewArray> arr,Context parent){
        this.list = list;
        photoUrlList = p;
        arrays=arr;
        context=parent;
       // Log.i("adapter", p.size()+" "+photoUrlList.size());
    }


    @Override
    public MyViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {



        prefs = parent.getContext().getSharedPreferences(Preferences, Context.MODE_PRIVATE|Context.MODE_APPEND);
        edit = prefs.edit();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        if(photoUrlList.size()==0){
            Toast.makeText(parent.getContext(), "list is already null", Toast.LENGTH_LONG).show();
        }else{
           // Toast.makeText(parent.getContext(), "list is not null", Toast.LENGTH_LONG).show();
            int i=0;
        }

        return new MyViewHolderClass(v);
    }

    public int getPx(int r1, Context context){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r1, r.getDisplayMetrics());
        return (int)px;
    }





    @Override
    public void onBindViewHolder(final MyViewHolderClass holder, int position) {

        re = list.get(holder.getAdapterPosition());
        holder.title.setText(re.getName());
        holder.rating.setText(re.getRating());
        final String vicinity = re.getVicinity();
        holder.number.setText(re.getNumber());
        holder.types.setText(re.getType());
        //Log.i("info","Name is "+re.getName()+" image url is "+re.getImageUrl()+" vicinity is "+re.getVicinity()+" and the rating is "+re.getRating());
        favourite_status = prefs.getString(re.getName(),"false");
        if(favourite_status.equals("false"))
            holder.favourite.setBackgroundResource(R.drawable.black_tomato);
        else {
            Log.i("crucial ", "I just ran " + holder.getAdapterPosition());
            Flist.add(list.get(holder.getAdapterPosition()));
            Farrays.add(arrays.get(holder.getAdapterPosition()));
            FphotoUrlList.add(photoUrlList.get(holder.getAdapterPosition()));
            holder.favourite.setBackgroundResource(R.drawable.tomato);
        }
        holder.vicinity.setText(vicinity);

        Glide.with(holder.pic.getContext()).load(photoUrlList.get(holder.getAdapterPosition()).getPhotoLocation()[0])
                .override(getPx(90,holder.pic.getContext()), getPx(130,holder.pic.getContext())).
                centerCrop().animate(R.anim.anim1).
                crossFade().into(holder.pic);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), Details.class);
                // i.putExtra("object",re);
                Restauraunt r = list.get(holder.getAdapterPosition());
                // b.putSerializable("object", r);

                i.putExtra("object", r);
                photoUrl myPhotoArray = photoUrlList.get(holder.getAdapterPosition());
                reviewArray hh = arrays.get(holder.getAdapterPosition());
                i.putExtra("photo_list", myPhotoArray);
                i.putExtra("reviews", hh);
                Log.i("detail", r.getName());

                v.getContext().startActivity(i);
            }
        });
        index++;

       // holder.cardView.startAnimation(animation);
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restauraunt r1 = list.get(holder.getAdapterPosition());
                favourite_status = prefs.getString(r1.getName(),"false");

                if(favourite_status.equals("false")){
                    holder.favourite.setBackgroundResource(R.drawable.tomato);
                    Log.i("prefs", "value is " + favourite_status + " for " + r1.getName());
                    favourite_status = "true";

                    //Favourites f = new Favourites(Flist,  FphotoUrlList, Farrays);
                    String s = prefs.getString("main_object","Not Available");
                    Favourites New=null;
                    if(!s.equals("Not Available"))
                    New = gson.fromJson(s,Favourites.class);

                    try {
                        if(s.equals("Not Available")){
                            Flist.add(list.get(holder.getAdapterPosition()));
                            Farrays.add(arrays.get(holder.getAdapterPosition()));
                            FphotoUrlList.add(photoUrlList.get(holder.getAdapterPosition()));
                        }else{
                            Flist = New.getList();
                            Farrays = New.getArr();
                            FphotoUrlList = New.getP();
                            Flist.add(list.get(holder.getAdapterPosition()));
                            Farrays.add(arrays.get(holder.getAdapterPosition()));
                            FphotoUrlList.add(photoUrlList.get(holder.getAdapterPosition()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        New  = new Favourites(Flist,FphotoUrlList,Farrays);
                        edit.putString("main_object",gson.toJson(New));
                    }
                    edit.putString(r1.getName(),favourite_status);
                    edit.commit();

                }else{
                    holder.favourite.setBackgroundResource(R.drawable.black_tomato);
                    favourite_status="false";
                    Flist.remove(list.get(holder.getAdapterPosition()));
                    Farrays.remove(arrays.get(holder.getAdapterPosition()));
                    FphotoUrlList.remove(photoUrlList.get(holder.getAdapterPosition()));
                    Favourites f = null;
                    try {
                        f  = new Favourites(Flist,FphotoUrlList,Farrays);
                        edit.putString("main_object", gson.toJson(f));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("prefs2", "value is "+favourite_status+" for "+r1.getName());
                    edit.putString(r1.getName(),favourite_status);
                    edit.commit();
                }
                toggle++;

            }
        });
    }

    private void displayLog(String s){
        Log.e("error",s);
    }

    public Favourites getFavourites() throws IOException, ClassNotFoundException {
        String s = prefs.getString("main_object","Not Available");
        displayLog(s);
        if(s.equals("Not Available")){
            Toast.makeText(context,"You still Don't have any Favourites",Toast.LENGTH_LONG).show();
            return null;
        }else {
            Favourites f = gson.fromJson(s, Favourites.class);
            return f;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolderClass extends RecyclerView.ViewHolder{
        public TextView title,rating,vicinity,number,types;
        public ImageView pic, favourite;
        public CardView cardView;
        public MyViewHolderClass(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.name);
            rating = (TextView)itemView.findViewById(R.id.rating);
            vicinity = (TextView)itemView.findViewById(R.id.vicinity);
            pic =(ImageView)itemView.findViewById(R.id.pic);
            number = (TextView)itemView.findViewById(R.id.number);
            types = (TextView)itemView.findViewById(R.id.types);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            favourite = (ImageView)itemView.findViewById(R.id.favourite);
           // animation.setDuration(500);
         //   cardView.startAnimation(animation);
        }
    }
}
