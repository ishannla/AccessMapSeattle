package com.sabersoft.ishannarula.accessmapseattle;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        //
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            if (map != null)
                setUpMap();
        }
    }


    private void setUpMap() {

        map.setMyLocationEnabled(true);

        UiSettings settings = map.getUiSettings();
        settings.setAllGesturesEnabled(true);
        settings.setZoomControlsEnabled(true);

        LatLng initialCenter = new LatLng(47.650045, -122.301186);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialCenter, 14));

        addSidewalks();

    }

    public void addSidewalks(){

        Polyline line = map.addPolyline(new PolylineOptions()
                .add(new LatLng(47.655358, -122.312173), new LatLng(47.656795, -122.312138))
                .width(5)
                .color(Color.RED));

    }
}
