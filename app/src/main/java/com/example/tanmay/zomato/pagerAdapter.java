package com.example.tanmay.zomato;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Random;

/**
 * Created by Tanmay on 01-10-2016.
 */
public class pagerAdapter extends PagerAdapter {
    LayoutInflater inflater;
    localdatabase database;
    ImageView imageView;
    public  pagerAdapter(localdatabase database){
        this.database = database;
    }
    @Override
    public int getCount() {
        return database.url.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup parent, int position){
        inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_pager_layout, parent, false);
        imageView = (ImageView)v.findViewById(R.id.view_pager_cover);
        Random rand = new Random();
                Glide.with(parent.getContext()).load(database.url[new MainActivity().showRandomInteger(0,database.url.length-1,rand)]).
                      override(new MainActivity().width, getPx(270,parent.getContext())).centerCrop().thumbnail(0.1f).
                    crossFade().into(imageView);
        Log.i("pager", "adapter has been called");
        parent.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }

    public int getPx(int r1,Context context){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r1, r.getDisplayMetrics());
        return (int)px;
    }
}


