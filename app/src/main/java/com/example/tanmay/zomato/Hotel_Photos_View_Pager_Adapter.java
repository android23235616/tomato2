package com.example.tanmay.zomato;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


public class Hotel_Photos_View_Pager_Adapter extends PagerAdapter {
    String[] database;
    ImageView image;
    int length;
    int height,  width;
    public Hotel_Photos_View_Pager_Adapter(String[] database,int height, int width,int length){
        this.database = database;
        this.height =height;
        this.width=width;
        this.length=length;
    }

    @Override
    public int getCount() {
        return length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.hote_photos_view_pager,container,false);
         image =  (ImageView)v.findViewById(R.id.mainImage);
        Glide.with(container.getContext()).load(database[position]).
                override(width,height).
                thumbnail(0.2f).centerCrop().into(image);
        container.addView(v);
        return v;
    }

    public int getPx(int r1, Context context) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r1, r.getDisplayMetrics());
        return (int) px;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      //  super.destroyItem(container, position, object);
        container.removeView((View)object);
    }
}
