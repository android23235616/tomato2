package com.example.tanmay.zomato;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Tanmay on 14-09-2016.
 */
public class MySingleton {
    private static MySingleton instance;
    private static Context mContext;
    private RequestQueue requestQueue;
    private ImageLoader loader;

    private MySingleton(Context context){
        mContext=context;
        requestQueue=getRequestQeueu();
        loader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache =new LruCache<String,Bitmap>(40);
            @Override
            public Bitmap getBitmap(String url) {
                //loader.get(url);
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
    }

    public static  synchronized MySingleton getInstance(Context c){
        if(instance==null){
            instance = new MySingleton(c);
        }
        return instance;
    }

    public RequestQueue getRequestQeueu() {
        if(requestQueue==null){
            Cache cache   = new DiskBasedCache(mContext.getCacheDir(),50*1024*1024);
            Network net = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache,net);
            requestQueue.start();
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader(){
        return loader;
    }
}
