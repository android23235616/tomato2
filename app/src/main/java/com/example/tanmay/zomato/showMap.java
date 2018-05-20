package com.example.tanmay.zomato;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Tanmay on 23-09-2016.
 */
public class showMap extends FragmentActivity implements OnMapReadyCallback{

    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle saved ){
        super.onCreate(saved);

        setContentView(R.layout.map);

// Receiving latitude from MainActivity screen
 lat = getIntent().getDoubleExtra("lat", 0);

// Receiving longitude from MainActivity screen
 lng = getIntent().getDoubleExtra("lng", 0);



SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map2);
        fm.getMapAsync(this);




    }

    private void Display(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng sydney = new LatLng(lat, lng);


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker"));
        Display("marked added");

    }


}
