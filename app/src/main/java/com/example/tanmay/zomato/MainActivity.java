package com.example.tanmay.zomato;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ViewPager.OnPageChangeListener{
    final public static String api_key = "AIzaSyCWpiZ6BlebD3U8BVfyoGyIIRagibTUWNQ";
    String place_detailsPrefix = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    localdatabase local;
    RequestQueue requestQueue;
    static int radius = 1000;
    final int PLACE_PICKER_REQUEST=4136;
    List<Restauraunt> list = new ArrayList<>();
    Handler viewHierchy;
    RecyclerView recyclerView;
    double current_lat=20.3536, current_long=85.8193;
    recyclerViewAdapter adapter;
    CollapsingToolbarLayout cToolbar;
    int favourite_checker=0;
    ImageView tomato, s_tb, r_tb, f_tb, radius_tb;
    CardView cardviewtoolbar;
    EditText search;
    static int dataLength;
    AppBarLayout appBarLayout;
    // ChompProgressView chompProgressView = (ChompProgressView) findViewById(R.id.progressBar);
    Handler handler;
    public static Context context;
    Location l=null;

    GoogleApiClient api=null;
    int e1 = 0, e2 = 0, e3 = 0;
    List<photoUrl> photoUrlList;
    List<reviewArray> reviewArrayList;
    ImageView cover;
    public static int width;
    ImageView splash;
    int finalSize;
    ProgressBar bar;
    // File f;
    TextView change_location;
    ViewPager pager;
    View buffer;
    boolean gate = false;
    ImageButton next, back;
    pagerAdapter pageradater;
    int search_checker = 1;
    JSONObject mainObject;
    static int loaderLooper = 1;
    public String f_vicinity;
    AlertDialog.Builder builder;
    // TextView t;
    Toolbar tb;
    CardView load_more_card_view;
    ProgressBar load_more_progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForPermissions();
        handler = new Handler();
        load_more_card_view = (CardView)findViewById(R.id.load_more_card_view);
        load_more_progress_bar = (ProgressBar)findViewById(R.id.load_more_progress);
        load_more_progress_bar.setVisibility(View.GONE);
        load_more_card_view.setVisibility(View.GONE);
        s_tb = (ImageView)findViewById(R.id.search_tb);
        r_tb = (ImageView)findViewById(R.id.refresh_tb);
        f_tb = (ImageView)findViewById(R.id.fav_tb);
        change_location = (TextView)findViewById(R.id.new_location);
        radius_tb = (ImageView)findViewById(R.id.radius_tb);
        cardviewtoolbar = (CardView)findViewById(R.id.cardviewtoolbar);
        cardviewtoolbar.setVisibility(View.GONE);
        reviewArrayList = new ArrayList<>();
        cToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        context = this;
        mainObject = null;
        tb = (Toolbar) findViewById(R.id.toolbar);
         buffer = (View)findViewById(R.id.buffer);
        buffer.setVisibility(View.GONE);
        photoUrlList = new ArrayList<>();
        local = new localdatabase();
        pageradater = new pagerAdapter(local);

        tomato = (ImageView) findViewById(R.id.tomato);
        next = (ImageButton) findViewById(R.id.right_arrow);

        search = (EditText) findViewById(R.id.search_bar);
        search.setVisibility(View.INVISIBLE);
        back = (ImageButton) findViewById(R.id.left_arrow);
        Random rand = new Random();
        width = getScreenWidth();
        viewHierchy = new Handler();
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        setSupportActionBar(tb);
        tb.setTitle("Tomato");
        pager = (ViewPager) findViewById(R.id.view_pager);
        appBarLayout.setVisibility(View.GONE);
//
        splash = (ImageView) findViewById(R.id.splash);
       // splash.setVisibility(View.GONE);
        initCollapsingToolbar();
        if(api==null)
        api = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        api.connect();
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setIndeterminate(true);
        requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQeueu();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //fetch data with current location
        fetchData(sbMethod(loaderLooper,false));
        gate = true;
        recyclerView.setVisibility(View.GONE);
        pager.setAdapter(pageradater);
        pager.setOnPageChangeListener(this);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = pager.getCurrentItem() + 1;
                pager.setCurrentItem(current);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = pager.getCurrentItem() - 1;
                if (current >= 0)
                    pager.setCurrentItem(current);
            }
        });

        addTextListener();



    load_more_card_view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (loaderLooper >= 3) {

                Toast.makeText(context, "No more restaurants", Toast.LENGTH_LONG).show();
            } else {

                load_more_progress_bar.setVisibility(View.VISIBLE);
                fetchData(sbMethod(loaderLooper, true));
            }

        }
    });
        s_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_checker % 2 != 0) {
                    change_location.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                } else {
                    change_location.setVisibility(View.VISIBLE);
                    search.setVisibility(View.INVISIBLE);
                    adapter = new recyclerViewAdapter(list, photoUrlList, reviewArrayList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    //oration(         new HorizontalDividerItemDecoration.Builder(context)                 .color(Color.RED)                 .sizeResId(1)                 .marginResId(0, 0)                 .build());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                search_checker++;
            }

        });

        r_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loaderLooper=1;
                fetchData(sbMethod(loaderLooper,false));
                photoUrlList.clear();
                list.clear();
                reviewArrayList.clear();
                bar.setVisibility(View.VISIBLE);
            }
        });

        f_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favourite_checker%2==0){
                    Glide.with(context).load(R.drawable.ic_home_white_48dp).into(f_tb);
                    favourites();
                    r_tb.setClickable(false);
                    radius_tb.setClickable(false);
                }else{
                    r_tb.setClickable(true);
                    radius_tb.setClickable(true);
                    Glide.with(context).load(R.drawable.favourite_off).fitCenter().into(f_tb);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    //oration(         new HorizontalDividerItemDecoration.Builder(context)                 .color(Color.RED)                 .sizeResId(1)                 .marginResId(0, 0)                 .build());
                    adapter = new recyclerViewAdapter(list,photoUrlList,reviewArrayList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                favourite_checker++;
            }
        });

        radius_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_radius();
            }
        });

    change_location.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            get_new_location();
        }
    });

    changeLocation();
    }

    private void checkForPermissions() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            askforpermission(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION});
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            askforpermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
        }
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
            askforpermission(new String[]{Manifest.permission.CALL_PHONE});
        }
    }

    private void askforpermission(String[] permission) {
        ActivityCompat.requestPermissions(this,permission,1);
    }

    private void favourites(){
        Favourites fav = null;
        try {
            fav = adapter.getFavourites();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (fav != null) {
            adapter = new recyclerViewAdapter(fav.getList(), fav.getP(), fav.getArr());

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            //oration(         new HorizontalDividerItemDecoration.Builder(context)                 .color(Color.RED)                 .sizeResId(1)                 .marginResId(0, 0)                 .build());
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void new_radius(){
        builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.seekbar, null);
        builder.setView(v);
        builder.setCancelable(true);
        final SeekBar sb = (SeekBar) v.findViewById(R.id.seekbar);
        final TextView rad = (TextView) v.findViewById(R.id.radiusText);
        rad.setText(radius + " Metres");
        sb.setProgress(radius / 1000-1);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = 1000 * (progress + 1);
                rad.setText(1000 * (progress + 1) + " Metres");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loaderLooper = 1;
                bar.setVisibility(View.VISIBLE);
                list.clear();
                photoUrlList.clear();
                reviewArrayList.clear();
                fetchData(sbMethod(loaderLooper,false));
                adapter.notifyDataSetChanged();
                builder.create().dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                radius = 1000;
                builder.create().cancel();
            }
        });
        builder.show();


    }
    private void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase();
                final List<Restauraunt> searchList = new ArrayList<Restauraunt>();
                final List<photoUrl> searchPhotoUrlList = new ArrayList<photoUrl>();
                final List<reviewArray> searchReviewArrayList = new ArrayList<reviewArray>();

                for (int i = 0; i < list.size(); i++) {
                    final Restauraunt re = list.get(i);
                    final photoUrl url = photoUrlList.get(i);
                    final reviewArray arr = reviewArrayList.get(i);
                    if (re.getName().
                            toLowerCase().contains(s)) {
                        searchList.add(re);
                        searchPhotoUrlList.add(url);
                        searchReviewArrayList.add(arr);
                    }
                    Animation animation;
                    animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                    animation.setDuration(440);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    //oration(         new HorizontalDividerItemDecoration.Builder(context)                 .color(Color.RED)                 .sizeResId(1)                 .marginResId(0, 0)                 .build());
                    adapter = new recyclerViewAdapter(searchList, searchPhotoUrlList, searchReviewArrayList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView.startAnimation(animation);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public int getPx(int r1) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r1, r.getDisplayMetrics());
        return (int) px;
    }

    public int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    public static int showRandomInteger(int aStart, int aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }

    public void fetchPlaceDetails(String place_id, String title, String vicinity, String i, int as, String open_close) {

        String mainUrl = place_detailsPrefix + place_id + "&key=" + api_key;
        Log.i("url for main url ", mainUrl);
        asd(mainUrl, title, vicinity, i, as, open_close);

    }

    private void initCollapsingToolbar() {

        tb.setTitle("Current Location");
         appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
             boolean isShow = false;
             int scrollRange = -1;

             @Override
             public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                 if (scrollRange == -1) {
                     scrollRange = appBarLayout.getTotalScrollRange();
                 }
                 if (scrollRange + i == 0) {
                     cToolbar.setTitle("Tomato");
                     isShow = true;
                 } else if (isShow) {
                     cToolbar.setTitle("");
                     isShow = false;
                 }
             }
         });
    }

    String t;

    public void asd(final String id, final String title, final String vicinity, final String ur, final int i, final String open_close) {
        StringRequest sr = new StringRequest(Request.Method.GET, id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.i("response", response);
                    JSONObject j = obj.getJSONObject("result");

                    if(response.contains("OVER_QUERY_LIMIT")){
                        display("OVER_QUERY_LIMIT");
                        finish();
                    }
                    JSONArray jar = j.getJSONArray("types");
                    JSONObject geometry = j.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    JSONArray photos = null;
                    JSONArray reviews = null;
                    try {
                        reviews = j.getJSONArray("reviews");

                    } catch (Exception e) {
                        review_holder[] holder = {new review_holder("Not Available", "Not Available", -4.0, "http://www.designofsignage.com/application/symbol/building/image/600x600/no-photo.jpg"
                                , 1000000, "A reviews hasn't been provided for this inn!")};
                        reviewArray arr = new reviewArray(holder, 1);
                        reviewArrayList.add(arr);
                    } finally {
                        if (reviews != null)
                            fetchReviews(reviews, i);
                    }
                    try {
                        photos = j.getJSONArray("photos");
                    } catch (Exception e) {
                        //e.printStackTrace();
                        e3++;
                        Log.i("e3", e3 + "");
                        String[] nullPhoto = {"http://www.designofsignage.com/application/symbol/building/image/600x600/no-photo.jpg"};
                        photoUrl nullPic = new photoUrl(nullPhoto, 1);
                        photoUrlList.add(nullPic);
                        // display("Photos don't exist "+e.toString());
                    } finally {
                        if (photos != null) {


                            Thread t;
                            try {
                                runPhotoThread(photos, i);
                                //t1.start();
                            } catch (Exception e) {
                            }

                            Log.i("photo_null", "photo is not null for " + title);
                        } else {
                            Log.i("photo_null", "photo is null for " + t);
                        }
                    }

                    LatLng ltlg = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                    t = title;
                    String types = "";
                    int k = 0;
                    while (k < jar.length()) {
                        types += jar.getString(k) + " ";
                        k++;
                    }
                    types += ".";
                    String f_types = types;
                    if (types.length() >= 25)
                        types = types.substring(0, 22) + "...";
                    if (t.length() >= 22) {
                        t = t.substring(0, 17) + "...";
                    }
                    String number;
                    try {
                        number = j.getString("international_phone_number");

                    } catch (Exception e) {
                        number = "Not Available";
                        e1++;
                        Log.i("e", e1 + "");
                    }

                    double dou;
                    try {
                        dou = j.getDouble("rating");
                    } catch (Exception e) {
                        dou = 4.0;
                        e2++;
                        Log.i("e2", e2 + "");
                    }
                    Restauraunt re = new Restauraunt(t, vicinity, String.valueOf(dou), ur, number, types, ltlg.latitude, ltlg.longitude, open_close, id,title,f_types,f_vicinity);
                    list.add(re);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //here it was
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("sdsd", error.toString());
            }
        });
        requestQueue.add(sr);
        sr.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void runPhotoThread(final JSONArray photos, final int index) {
        int i = 0;
        String[] url = new String[1000];
        while (i < photos.length()) {
            try {
                JSONObject iterator = photos.getJSONObject(i);
                String reference = iterator.getString("photo_reference");
                url[i] = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + reference + "&key=" + api_key;
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
                // display(e.toString());
            }
        }
        photoUrl myPhotourl = new photoUrl(url, photos.length());
        photoUrlList.add(myPhotourl);
        //Toast.makeText(this, "added "+url+" "+photoUrlList.size()+" "+index,Toast.LENGTH_LONG).show();
        Log.i("vital info", "added " + url + " " + photoUrlList.size() + " " + index);

        if (index <= finalSize - 1) {
            viewHierchy.post(new Runnable() {
                @Override
                public void run() {
                    adapter = new recyclerViewAdapter(list, photoUrlList, reviewArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    //oration(         new HorizontalDividerItemDecoration.Builder(context)                 .color(Color.RED)                 .sizeResId(1)                 .marginResId(0, 0)                 .build());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setNestedScrollingEnabled(false);
                    bar.setVisibility(View.GONE);
                    buffer.setVisibility(View.VISIBLE);
                    splash.setVisibility(View.GONE);
                    tomato.setVisibility(View.GONE);
                    appBarLayout.setVisibility(View.VISIBLE);

                    recyclerView.setVisibility(View.VISIBLE);
                    load_more_card_view.setVisibility(View.VISIBLE);
                    cardviewtoolbar.setVisibility(View.VISIBLE);
                    load_more_progress_bar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop(){
        super.onStop();
        api.disconnect();
    }


    private void fetchReviews(JSONArray reviews, final int number) throws JSONException {
        int i = 0;
        review_holder[] holder = new review_holder[1000];
        while (i < reviews.length()) {
            JSONObject iterator = reviews.getJSONObject(i);
            String author_name;
            try {
                author_name = iterator.getString("author_name");
            } catch (Exception e) {
                author_name = "Not Available";
            }
            String author_url;
            try {
                author_url = iterator.getString("author_url");
            } catch (Exception e) {
                author_url = "null";
            }

            String profile_photo_url;
            try {
                profile_photo_url = iterator.getString("profile_photo_url");
            } catch (Exception e) {
                profile_photo_url = "http://stmichaelsknightsofcolumbus.com/wordpress/wp-content/uploads/2013/08/Photo-not-available.jpg";
            }
            double rating;

            try {
                rating = iterator.getDouble("rating");
            } catch (Exception e) {
                rating = 3.0;
            }
            String mainText;
            try {
                mainText = iterator.getString("text");
            } catch (Exception e) {
                mainText = "Not Available";
            }
            long unix_time_stamp;
            try {

                unix_time_stamp = iterator.getLong("unix_time_stamp");
            } catch (Exception e) {
                unix_time_stamp = 1234567890;
            }
            holder[i] = new review_holder(author_name, author_url, rating, profile_photo_url, unix_time_stamp, mainText);
            i++;
        }
        reviewArray arr = new reviewArray(holder, i);
        reviewArrayList.add(arr);
    }

    public void fetchData(String dataPool) {

        // Glide.with(cover.getContext()).load(local.url[showRandomInteger(0,local.url.length-1,rand)]).into(cover);

        //display(dataPool);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, dataPool, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.i("response rece","");
                    mainObject = response;

                    String title = "", vicinity = "", imageUrl = "", rating = "";
                    JSONArray ja = response.getJSONArray("results");
                    finalSize = ja.length();
                    int i = 0;
                    //Log.i("","final size is "+finalSize);
                    //display("final size is "+finalSize);
                    dataLength = ja.length();
                    int a = 0;
                    while (i < ja.length()) {

                        JSONObject obj = ja.getJSONObject(i);
                        title = obj.getString("name");
                        JSONObject opening = null;
                        String open_close = false + "";
                        try {
                            opening = obj.getJSONObject("opening_hours");
                            open_close = opening.getBoolean("open_now") + "";
                        } catch (Exception e) {
                            open_close = "Not Available";
                        }
                        vicinity = obj.getString("vicinity");

                        imageUrl = local.url[a];
                        if (a == local.url.length - 1)
                            a++;
                        Log.i("show this ", "\nplace id: " + obj.getString("place_id") + " and ");
                        f_vicinity=vicinity;
                        if (vicinity.length() >= 23) {
                            vicinity = vicinity.substring(0, 23) + "...";
                        }
                        if (vicinity.equals("null"))
                            vicinity = "Shop No 6, Ground Floor...";
                        if (rating.equals("null"))
                            if (rating.equals("null"))
                                rating = "4.2";

                        fetchPlaceDetails(obj.getString("place_id"), title, vicinity, local.url[a], i, open_close);
                        //list.add(re);
                        i++;
                        a++;
                    }

                    Log.d("erer", list.size() + " " + i + " error 1: " + e1 + " error2: " + e2);

                } catch (JSONException e) {
                    e.toString();
                    display(e.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                display(error.toString());

            }
        });
        requestQueue.add(jsObjRequest);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void display(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    public String sbMethod(int i,boolean trigger) {

        //use your current location here


        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + current_lat + "," + current_long);
        sb.append("&radius="+radius);
        sb.append("&types=" + "restaurant");
        sb.append("&sensor=false");
        sb.append("&hasNextPage=true&nextPage()=true");
        sb.append("&key=" + api_key);
        String page_token = "";
        // if(gate){
        page_token = getPageToken(loaderLooper);
        //}

        if (gate && !(page_token.equals("loader"))&&trigger) {
            sb.append("&pagetoken=" + page_token);
        }else{
           // display("No more restaurants to be displayed");
        }
        Log.d("Map", "api: " + sb.toString());

        return sb.toString();
    }

    private String getPageToken(int i) {
        String page_token = "";
        if (mainObject != null) {
            if (i < 3) {

                try {
                    page_token = mainObject.getString("next_page_token");
                } catch (JSONException e) {
                    page_token = "null";
                    loaderLooper=5;
                    e.printStackTrace();
                    return "loader";
                }
            }
        }
        return page_token;
    }

    public void changeLocation(){
        if(l==null)
            l = LocationServices.FusedLocationApi.getLastLocation(api);

        if (l != null) {
            errorLog("Location isnt null");
            current_lat = l.getLatitude();
            current_long = l.getLongitude();
            loaderLooper = 1;
            fetchData(sbMethod(loaderLooper,false));
            //      break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        errorLog("Connected to services");
        LocationRequest lr = LocationRequest.create();
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lr.setInterval(2000);
       // while(true) {
            if(l==null)
         l = LocationServices.FusedLocationApi.getLastLocation(api);

            if (l != null) {
                errorLog("Location isnt null");
                current_lat = l.getLatitude();
                current_long = l.getLongitude();
                loaderLooper = 1;
                fetchData(sbMethod(loaderLooper,false));
          //      break;
            }
        //}

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        display("Connected to services " + connectionResult.toString());

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == local.url.length - 1) {
            next.setVisibility(View.GONE);
        } else {
            next.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

   

    private void errorLog(String s){
        Log.e("error",s);
    }

    private void get_new_location() {
       if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
               &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
           display("Location Permissions have been denied");
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
       }else{
           PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
           try {
               startActivityForResult(intentBuilder.build(this),PLACE_PICKER_REQUEST);
           } catch (GooglePlayServicesRepairableException e) {
               e.printStackTrace();
               display(e.toString());
           } catch (GooglePlayServicesNotAvailableException e) {
               e.printStackTrace();
               display(e.toString());
           }
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode==RESULT_OK&&requestCode==PLACE_PICKER_REQUEST){
           Place place = PlacePicker.getPlace(data,this);
           String name = (String)place.getName();
           if(name.length()>20)
               name = name.substring(0,20);
           change_location.setText(name);
           current_lat = place.getLatLng().latitude;
           current_long = place.getLatLng().longitude;
           loaderLooper=1;
           list.clear();
           photoUrlList.clear();
           reviewArrayList.clear();
           fetchData(sbMethod(loaderLooper,false));
       }
    }
}
