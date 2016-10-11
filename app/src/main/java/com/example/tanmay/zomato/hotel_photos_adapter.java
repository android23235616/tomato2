package com.example.tanmay.zomato;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Tanmay on 24-09-2016.
 */
public class hotel_photos_adapter extends RecyclerView.Adapter<hotel_photos_adapter.ThisViewHolder> {
    String[] photoList;
    int hotelPics;
    MainActivity main;
    @Override
    public hotel_photos_adapter.ThisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        main = new MainActivity();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.namecardview,parent,false);
        return new ThisViewHolder(v);

    }

    public hotel_photos_adapter(String[] info,int hotelPics){
        photoList = info;
        this.hotelPics = hotelPics;
    }
    String mainUrl;
    @Override
    public void onBindViewHolder(final hotel_photos_adapter.ThisViewHolder holder, int position) {
          // String[] photo = photoList.get(holder.getAdapterPosition());
        //Log.i("loading", photo.getPhotoLocation());
       final String photo = photoList[holder.getAdapterPosition()];
//        Log.i(photo,photo);

        //if(photoList.length>1)
        // mainUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photo+"&key="+new MainActivity().api_key;
       //else
         //   mainUrl = photo;
        Glide.with(holder.image.getContext()).load(photo).thumbnail(0.1f).crossFade().into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(v.getContext(), ViewPhoto.class);
                i.putExtra("main_image",photoList);
                i.putExtra("length",hotelPics);
                i.putExtra("position", holder.getAdapterPosition());
                v.getContext().startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        return hotelPics;
    }
    public class ThisViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        private  CardView myPics;
        public ThisViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.chicken);
            myPics =(CardView)itemView.findViewById(R.id.card_viewHotelPics);
        }

    }
}
