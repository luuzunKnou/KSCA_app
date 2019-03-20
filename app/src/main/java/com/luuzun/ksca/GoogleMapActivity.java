package com.luuzun.ksca;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(GoogleMapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        Log.i("ksca_log", "On Map Ready-"+address);

        mMap = googleMap;
        geocoder = new Geocoder(this);

        List<Address> list = null;

        try {
            list = geocoder.getFromLocationName(address, 10); // 주소,읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ksca_log","ERROR : GEOCODER");
        }

        if (list != null) {
            if (list.size() == 0) {
                Log.i("ksca_log", "CANNOT FOUND ADDRESS");
            } else {
                Address addr = list.get(0);
                double latitude = addr.getLatitude(); //위도
                double longitude = addr.getLongitude(); //경도

                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(latitude, longitude);

                MarkerOptions mOptions = new MarkerOptions(); //마커 생성
                mOptions.title("search result");
                mOptions.snippet(address);
                mOptions.position(point);
                // 마커 추가
                mMap.addMarker(mOptions);
                // 해당 좌표로 화면 이동
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
            }
        } else {
            Log.i("ksca_log", "NULL LIST");
        }
    }
}
