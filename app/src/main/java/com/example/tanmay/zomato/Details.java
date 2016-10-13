package com.example.tanmay.zomato;
import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Tanmay on 20-09-2016.
 */

public class Details extends AppCompatActivity {
    Toolbar toolbar;
    AppBarLayout alo;
    Double lat,lng;
    NestedScrollView nsw;
    CollapsingToolbarLayout ctl;
    CoordinatorLayout cl;
    public TextView title, rating, vicinity, number, types, open_close;
    public ImageView pic, coverpic2;
    public FloatingActionButton toolbarCall;
    photoUrl mainPhoto;
    LinearLayout out;
    List<photoUrl> url;
    RecyclerView recycled, reviewsList;
    hotel_photos_adapter adapter;
    reviewArray reviewArrays;
    reviewRecycler reviewAdapters;
    CardView reviewButton;
    static int reviewChecker = 0;
    TextView expansion,f_name, f_add, f_number,f_types;
    Animation slide_down,slide_up;
    Intent intent;
    FloatingActionButton navigate;
    private GoogleApiClient client;
    String open_closeText;
    float dX,dY;
    int lastAction;
    ImageView f_pic;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.destails);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        url = new ArrayList<>();
        mainPhoto = null;
        f_name = (TextView)findViewById(R.id.Full_name);
        f_pic = (ImageView)findViewById(R.id.full_pic);
        f_add = (TextView)findViewById(R.id.full_address);
        f_number = (TextView)findViewById(R.id.full_number);
        f_types = (TextView)findViewById(R.id.full_types);
        open_close = (TextView)findViewById(R.id.open_close);
        navigate = (FloatingActionButton)findViewById(R.id.navigate);
        reviewButton = (CardView) findViewById(R.id.reviewButton);
        cl = (CoordinatorLayout) findViewById(R.id.main_content);
        ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        recycled = (RecyclerView) findViewById(R.id.recyclerRestautantphotos);
        reviewsList = (RecyclerView) findViewById(R.id.reviewsList);
        reviewsList.setVisibility(View.GONE);
        reviewsList.setNestedScrollingEnabled(true);
        expansion = (TextView) findViewById(R.id.expansion);
        setSupportActionBar(toolbar);
        toolbarCall = (FloatingActionButton) findViewById(R.id.call);
        ctl.setExpandedTitleColor(Color.WHITE);
        out = (LinearLayout) findViewById(R.id.location);
        alo = (AppBarLayout) findViewById(R.id.appBarLayout);
        nsw = (NestedScrollView) findViewById(R.id.nestedScrollView);
        title = (TextView) findViewById(R.id.name);
        rating = (TextView) findViewById(R.id.rating);
        vicinity = (TextView) findViewById(R.id.vicinity);
        pic = (ImageView) findViewById(R.id.pic);
        number = (TextView) findViewById(R.id.number);
        types = (TextView) findViewById(R.id.types);
        coverpic2 = (ImageView) findViewById(R.id.coverpic);
        Intent i = getIntent();
        final Restauraunt re = (Restauraunt) i.getExtras().getSerializable("object");
        mainPhoto = (photoUrl) i.getExtras().getSerializable("photo_list");
        title.setText(re.getName());
        rating.setText(re.getRating());
        open_closeText = re.getOpen_close();
        final String vicinity1 = re.getVicinity();
        number.setText(re.getNumber());
        types.setText(re.getType());
        f_types.setText(re.getFull_types());
        f_add.setText(re.getFull_vicinity());
        f_name.setText(re.getFull_title());
        f_number.setText(re.getNumber());
        Random random = new Random();
        if(open_closeText.equals(true+"")){
            open_close.setText("OPEN");
            open_close.setBackgroundResource(R.drawable.shaperating);
        }else if(open_closeText.equals("false"+"")){
            open_close.setText("CLOSED");
            open_close.setBackgroundResource(R.drawable.redshape);
        }else{
            open_close.setVisibility(View.GONE);
        }
        vicinity.setText(vicinity1);
        Glide.with(pic.getContext()).load(re.getImageUrl()).thumbnail(0.01f).override(75, 90).centerCrop().crossFade().into(pic);
        Glide.with(f_pic.getContext()).load(re.getImageUrl()).thumbnail(0.01f).override(75, 90).centerCrop().crossFade().into(f_pic);
        Glide.with(this).load(new localdatabase().
                url[showRandomInteger(0, new localdatabase().url.length - 1, random)]).
                override(getScreenWidth(), getPx(300)).centerCrop().
                crossFade().into(coverpic2);
        lat = re.getLatitude();
        lng = re.getLongitude();
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LatLng ltlng = new LatLng(re.getLatitude(), re.getLongitude());
                 lat = ltlng.latitude;
                 lng = ltlng.longitude;
                Intent showMap = new Intent(Intent.ACTION_VIEW);
                showMap.setData(Uri.parse("geo:0,0?q="+lat+","+lng));
                try{
                    startActivity(showMap);
                }catch (Exception e){
                    display(e.toString());
                }
            }
        });
         intent = new Intent(Intent.ACTION_CALL);
        reviewArrays = (reviewArray) i.getExtras().getSerializable("reviews");
        reviewAdapters = new reviewRecycler(reviewArrays.getHolder(), reviewArrays.getActual_length());
        adapter = new hotel_photos_adapter(mainPhoto.getPhotoLocation(), mainPhoto.getPhotoNumber());
        int mSize = adapter.getItemCount();
        if (mSize == 0) {
            recycled.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.photos)).setVisibility(View.GONE);
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
            recycled.setLayoutManager(layoutManager);
            recycled.setItemAnimator(new DefaultItemAnimator());
            recycled.setAdapter(adapter);
        }
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        reviewsList.setLayoutManager(layoutManager2);
        reviewsList.setItemAnimator(new DefaultItemAnimator());
        reviewsList.setAdapter(reviewAdapters);

       // Animation slide_down;
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        reviewButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (reviewChecker % 2 == 0) {
                    expansion.setText("Collapse Reviews");
                    reviewsList.setVisibility(View.VISIBLE);

                } else {
                    expansion.setText("Expand Reviews");
                    reviewsList.setVisibility(View.GONE);
                }
                reviewChecker++;
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        toolbarCall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = toolbarCall.getX() - event.getRawX();
                        dY = toolbarCall.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                   /* case MotionEvent.ACTION_MOVE:
                        toolbarCall.setY(event.getRawY() + dY);
                        toolbarCall.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;*/

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            call(re.getNumber());
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
        
        navigate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = navigate.getX() - event.getRawX();
                        dY = navigate.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        navigate.setY(event.getRawY() + dY);
                        navigate.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            Log.i("", lat+","+lng);
                            Uri main = Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lng);
                            final Intent i = new Intent(Intent.ACTION_VIEW,main);
                            startActivity(i);
                            display(lat+","+lng);
                        }
                        break;

                    default:
                        return false;
                }
                return true;
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

    private static int showRandomInteger(int aStart, int aEnd, Random aRandom) {
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

    private void call(String number) {
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            display("Permission to call denied");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }
    startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int request,String[] permission,int[] granted){
        if(granted.length>0&&granted[0]==PackageManager.PERMISSION_GRANTED){
            startActivity(intent);
        }else{
            display("Can't call! Permission denied");
        }
    }

    private void display(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Details Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.tanmay.zomato/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}
