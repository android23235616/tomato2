package com.example.tanmay.zomato;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewPhoto extends AppCompatActivity {
    Intent i;
    ImageView imageView;
    int height,width,length;
    ViewPager view_pager;
    int position;
    Hotel_Photos_View_Pager_Adapter hotel_photos_view_pager_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        view_pager = (ViewPager)findViewById(R.id.hotel_photos_view_pager);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim1);
        animation.setDuration(550);
        i = getIntent();
        String[] url = i.getStringArrayExtra("main_image");
        length = i.getIntExtra("length",1);
        getScreenWidth();
        position = i.getIntExtra("position",1);
        hotel_photos_view_pager_adapter = new Hotel_Photos_View_Pager_Adapter(url,height,width,length);
        view_pager.setAdapter(hotel_photos_view_pager_adapter);
        view_pager.startAnimation(animation);
        view_pager.setCurrentItem(position);
        //Glide.with(this).load(url).override(getPx(300),getPx(400)).thumbnail(0.3f).centerCrop().into(imageView);
    }

    public int getPx(int r1) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r1, r.getDisplayMetrics());
        return (int) px;
    }

    private void getScreenWidth(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
         height = displaymetrics.heightPixels;
         width = displaymetrics.widthPixels;

    }

}
