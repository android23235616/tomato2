package com.example.tanmay.zomato;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tanmay on 28-09-2016.
 */
public class reviewRecycler extends RecyclerView.Adapter<reviewRecycler.MyViewHolder> {

    int actual_length;
    review_holder[] holderArray;
    public reviewRecycler(review_holder[] mainArray,int actual_length){
        holderArray = mainArray;
        this.actual_length=actual_length;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
      review_holder reviewObject = holderArray[holder.getAdapterPosition()];
        String name, profile_pic,mainText;
        double rating;
        long unixStamp;
        name = reviewObject.getAuthor_name();
        profile_pic = reviewObject.getProfile_pic_url();
        mainText = reviewObject.getMainText();
        rating = reviewObject.getRating();
        unixStamp = reviewObject.getUnix_timestamp();
        holder.actual_review.setText(mainText);
        holder.rating.setText(rating + "");
        Glide.with(holder.profilePic.getContext()).load("https:"+profile_pic).centerCrop().crossFade().thumbnail(0.1f).into(holder.profilePic);
        holder.name.setText(name);
        String getActualTime =convertUnixTimeStamp(unixStamp);
        holder.date.setText(getActualTime);
    }

    @Override
    public int getItemCount() {
        return actual_length;
    }

    private String convertUnixTimeStamp(long stamp){
        Date date = new Date(stamp*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String finalDate = sdf.format(date);
        return finalDate;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profilePic;
        private TextView actual_review;
        private TextView date;
        private TextView rating;
        private TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            profilePic =(CircleImageView)itemView.findViewById(R.id.profile_pic);
            actual_review = (TextView)itemView.findViewById(R.id.actual_review);
            date = (TextView)itemView.findViewById(R.id.date);
            rating = (TextView)itemView.findViewById(R.id.rating);
            name = (TextView)itemView.findViewById(R.id.name);
        }
    }
}
