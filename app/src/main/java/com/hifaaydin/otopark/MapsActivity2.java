package com.hifaaydin.otopark;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {
static final LatLng Kumluca =new LatLng(36.361151, 30.285809);
static final LatLng Konyaaltı =new LatLng(36.860925, 30.637387);
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if(mMap != null){
            mMap.addMarker(new MarkerOptions().position(Kumluca).title("Kumluca"));
            mMap.addMarker(new MarkerOptions().position(Konyaaltı)
                    .title("Konyaaltı")
                    .snippet("Konyaaltı Sahili"));
            /*  PolylineOptions options =new PolylineOptions().add(Kumluca)
                    .add(Konyaaltı)
                    .width(5)
                    .color(Color.BLUE)
                    .visible(true);
             mMap.addPolyline(options); //cizgiyi cizdirir. */
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Kumluca,12));
        mMap.setTrafficEnabled(true);


    }
}
