package com.hifaaydin.otopark;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    // static final LatLng Kumluca =new LatLng(36.361151, 30.285809);
    // static final LatLng Konyaaltı =new LatLng(36.860925, 30.637387);
   //  {"name":"Demirbey Otopark","latitude":38.6740488,"longitude":39.209936,"capacity":40,"status":40}
    private GoogleMap mMap;
    String yeni = "{\"otopark\":[{\"name\":\"Demirbey Otopark\",\"latitude\":38.6740488,\"longitude\":39.209936,\"capacity\":40,\"status\":40}," +
            "{\"name\":\"Ertan Yediemin Otoparkı\",\"latitude\":38.6617885,\"longitude\":39.2159702,\"capacity\":50,\"status\":50}," +
            "{\"name\":\"Tuncay Yediemin Otoparkı\",\"latitude\":38.6812884,\"longitude\":39.2219526,\"capacity\":45,\"status\":45}," +
            "{\"name\":\"En Has Otopark\",\"latitude\":38.671053,\"longitude\":39.2181701,\"capacity\":95,\"status\":95}," +
            "{\"name\":\"İbrahim Ethem Otoparkı\",\"latitude\":38.677916,\"longitude\":39.2263361,\"capacity\":30,\"status\":30}," +
            "{\"name\":\"Olgunlar Otopark\",\"latitude\":38.6669,\"longitude\":39.209515,\"capacity\":65,\"status\":65}," +
            "{\"name\":\"Yasemin Otopark\",\"latitude\":38.660036,\"longitude\":39.220585,\"capacity\":25,\"status\":25}," +
            "{\"name\":\"Kültür Otopark\",\"latitude\":38.672858,\"longitude\":39.205401,\"capacity\":87,\"status\":87}," +
            "{\"name\":\"Üniversite Otopark\",\"latitude\":38.679618,\"longitude\":39.209387,\"capacity\":100,\"status\":100}," +
            "{\"name\":\"Harput Otopark\",\"latitude\":38.705562,\"longitude\":39.255542,\"capacity\":125,\"status\":85}]}";
    public static final int LOCATION_REQUEST_CODE = 99;
    Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final Button btn_Htip= (Button) findViewById(R.id.btn_uydu);
        btn_Htip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btn_Htip.setText("NORMAL");
                }
                else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btn_Htip.setText("UYDU");
                }
            }
        });

        Button btngit= (Button)findViewById(R.id.buton);
        btngit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editlokasyon=(EditText)findViewById(R.id.git_lokasyon);
                String location =editlokasyon.getText().toString();
                if(location !=null && !location.equals(" ")){
                    List<Address> adresslist = null;
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try{
                        adresslist=geocoder.getFromLocationName(location,1);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    Address address=adresslist.get(0);
                    LatLng latLng =new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                }
            }
        });
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            Toast.makeText(getApplicationContext(), "Location izinleri verilmedi!!!", Toast.LENGTH_LONG).show();
        }
//        mMap.setMyLocationEnabled(true);
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            try {
                JSONObject obj = new JSONObject(yeni);//json yapısı oluştu
                System.out.println(obj);
                JSONArray userArray = obj.getJSONArray("otopark");
                int index = - 1;
                float min_distance = -1;
                for (int i = 0; i < userArray.length(); i++) {
                    // create a JSONObject for fetching single user data   (fetch <=> convert)
                    //bir json objesini bir otopark verisine dönüştürüyo
                    JSONObject userDetail = userArray.getJSONObject(i);

                    String name = userDetail.getString("name");
                    double latitute = userDetail.getDouble("latitude");
                    double longitude = userDetail.getDouble("longitude");
                    int capacity = userDetail.getInt("capacity");
                    int status = userDetail.getInt("status");
                    int bos = capacity-status;


                    float distance = uzaklıkBulma( latitute, longitude);
                    Log.i("DISTANCE3: ", distance + " ");
                    if(bos!=0){
                        if (min_distance == -1) {
                            min_distance = distance;
                            index=i;
                        }
                        else if(min_distance > distance){
                            min_distance = distance;
                            index=i;
                        }
                    }
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitute, longitude)).title(name+ "--"+distance+"--" + capacity+"--"+bos));


                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {

                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            View v = getLayoutInflater().inflate(R.layout.googlemapstitle, null);

                            final TextView txt_otopark = (TextView) v.findViewById(R.id.txt_otopark);
                            TextView txt_doluluk = (TextView) v.findViewById(R.id.txt_doluluk);
                            TextView txt_uzaklık = (TextView) v.findViewById(R.id.txt_uzaklık);
                            TextView txt_kapasite = (TextView) v.findViewById(R.id.txt_kapasite);
                            String[] options = marker.getTitle().split("--");//marker a eklenen string -- ye göre split edilip text alanlarına yazılmış

                            txt_otopark.setText("Adı: " + options[0] );
                            Log.i("BOS: ", marker.getTitle()); //konsola yazdırma işlemi
                            txt_kapasite.setText("Bos Alan: " + options[3]);
                            txt_uzaklık.setText("Uzaklık: " + options[1] +" m. ");
                            txt_doluluk.setText("Kapasite : " + options[2] );

                            return v;
                        }
                    });

                }
                JSONObject userDetail = userArray.getJSONObject(index);
                double latitute = userDetail.getDouble("latitude");
                double longitude = userDetail.getDouble("longitude");
                LatLng myLatLng = new LatLng(latitute, longitude);
                rotaOlustur(myLatLng);

            } catch (JSONException ex) {
                Log.e("Hata: ", ex.toString());

            } catch (Exception ex) {
                Log.e("Hata: ", ex.toString());

            }
        }
    }
    public void rotaOlustur(LatLng destLocation) {
        List<LatLng> path = new ArrayList();
        //Execute Directions API request
        String strlat = String.valueOf(myLocation.getLatitude());
        String strlon = String.valueOf(myLocation.getLongitude());
        String lat_lon = strlat+","+strlon;
        String strdestlat = String.valueOf(destLocation.latitude);
        String strdestlon = String.valueOf(destLocation.longitude);
        String dest_lat_lon = strdestlat+","+strdestlon;
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAPU_3J6KmVxgL5cGajEbQOvjEWkNN6zCQ");
        DirectionsApiRequest req = DirectionsApi.getDirections(context, lat_lon, dest_lat_lon);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            //polyline stringini rota koordinatlarına çevirir.
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("Hata: ", ex.toString());
        }

        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destLocation,14)); //büyük küçük
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public float uzaklıkBulma(double dest_lat, double dest_long) {
        //location servisleri
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        float min_distance = -1l; //uzaklık negatif olamayacagi icin baslangic degeri -1 verdik,

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "Konum İzinleri Verilmedi!!!",Toast.LENGTH_LONG).show();
            return -1l;
        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        if (location != null) {
            //Execute Directions API request
            myLocation = location;
            String strlat = String.valueOf(location.getLatitude());
            String strlon = String.valueOf(location.getLongitude());
            String lat_lon = strlat + "," + strlon;
            int min_index = 0;
            String dest_lat_lon = String.valueOf(dest_lat) + "," + String.valueOf(dest_long);
            GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAPU_3J6KmVxgL5cGajEbQOvjEWkNN6zCQ");
            DirectionsApiRequest req = DirectionsApi.getDirections(context, lat_lon, dest_lat_lon);
            try {
                DirectionsResult res = req.await();
                //Loop through legs and steps to get encoded polylines of each step
                if (res.routes != null && res.routes.length > 0) {
                    DirectionsRoute route = res.routes[0];
                    Log.i("DISTANCE routes: ", route.legs  + " ");

                    if (route.legs != null) {
                        for (int i = 0; i < route.legs.length; i++) {
                            Long distance = route.legs[i].distance.inMeters;// iki node arasındaki uzaklığı metre cinsinden bulduk
                            //Log.e("uzaklık metre: ", distance + "");
                            if (min_distance == -1l) {
                                min_distance = distance;
                                //min_index = index;
                            } else if (min_distance > distance) {
                                min_distance = distance;
                                //min_index = index;
                            }
                        }

                    }
                }
            } catch (Exception ex) {

                Log.e("Hata: ", ex.getMessage());
                return -1l;
            }
            //int distance = uzaklıkBulma(myLatLng, latitute, longitude);
        }


        return min_distance;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                        mMap.setMyLocationEnabled(true);
                    }
                }else{
                    Toast.makeText(this,"Izin reddedildi.",Toast.LENGTH_LONG).show();
                }
        }
    }

    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }
            return false;
        }else
            return true;
    }
}