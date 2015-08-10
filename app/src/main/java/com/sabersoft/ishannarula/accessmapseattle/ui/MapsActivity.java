package com.sabersoft.ishannarula.accessmapseattle.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sabersoft.ishannarula.accessmapseattle.R;
import com.sabersoft.ishannarula.accessmapseattle.data.Curb;
import com.sabersoft.ishannarula.accessmapseattle.data.Permit;
import com.sabersoft.ishannarula.accessmapseattle.data.Sidewalk;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MapsActivity extends FragmentActivity {

    private GoogleMap map;
    private ProgressDialog progress;

    private String sidewalkURL;
    private String curbURL;
    private String permitURL;

    public Sidewalk[] sidewalks;
    public Curb[] curbs;
    public Permit[] permits;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sidewalkURL = "http://hackcessibleapi.cs.washington.edu/sidewalks.geojson";
        curbURL = "http://hackcessibleapi.cs.washington.edu/curbs.geojson";
        permitURL = "http://hackcessibleapi.cs.washington.edu/permits.geojson";

        //CustomDialog dialog = new CustomDialog("Debug1", "Debug1");
        //dialog.show(getFragmentManager(), "debug");

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

        progress = new ProgressDialog(MapsActivity.this);
        progress.setTitle("Downloading content");
        progress.setMessage("Adding overlays to map");
        progress.show();

        getData(sidewalkURL);
        getData(curbURL);
        getData(permitURL);

        progress.dismiss();
        
    }

    public void getData(String url){

        int switcher = -1;

        if(url.equals(sidewalkURL))
            switcher = 0;

        else if(url.equals(curbURL))
            switcher = 1;

        else if(url.equals(permitURL))
            switcher = 2;

        if(isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);

            final int finalSwitcher = switcher;
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) { }

                @Override
                public void onResponse(Response response) throws IOException {

                    try {

                        String dataJSON = response.body().string();

                        if (response.isSuccessful()) {

                            Log.d("MapsActivity", dataJSON);
                            parseData(dataJSON, finalSwitcher);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {updateMapOverlay(finalSwitcher);
                                }
                            });
                        }

                        else {
                            Toast toast = Toast.makeText(MapsActivity.this, "Unable to complete request", Toast.LENGTH_LONG);
                            toast.show();
                        }

                    } catch (Exception e) {
                        Log.d("MapsActivity", "Cannot connect to database");
                    }
                }
            });

        }
    }


    private void parseData(String dataJSON, int switcher) throws JSONException{

        if (switcher == 0) {
           sidewalks = getSidewalkDetails(dataJSON);
        }

        if (switcher == 1) {

        }

        if (switcher == 2) {

        }

    }

    private Sidewalk[] getSidewalkDetails(String dataJSON) throws JSONException {

        JSONObject database = new JSONObject(dataJSON);
        JSONArray sidewalkData = database.getJSONArray("features");

        Sidewalk[] sidewalks = new Sidewalk[sidewalkData.length()];

        for (int inx = 0; inx < sidewalks.length; inx++) {

            Sidewalk finalSidewalk = new Sidewalk();

            JSONObject features = sidewalkData.getJSONObject(inx);
            JSONObject geometry = features.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            JSONObject properties = features.getJSONObject("properties");

            double grade = properties.getDouble("grade");
            finalSidewalk.setGrade(grade);

            int sidewalkID = properties.getInt("sidewalk_objectid");
            finalSidewalk.setSidewalkID(sidewalkID);

            JSONArray startCoordinate = coordinates.getJSONArray(0);
            JSONArray endCoordinate = coordinates.getJSONArray(1);
            Double startCoordinateLat = startCoordinate.getDouble(1);
            Double startCoordinateLong = startCoordinate.getDouble(0);
            Double endCoordinateLat = endCoordinate.getDouble(1);
            Double endCoordinateLong = endCoordinate.getDouble(0);

            LatLng startPoint = new LatLng(startCoordinateLat, startCoordinateLong);
            LatLng endPoint = new LatLng(endCoordinateLat, endCoordinateLong);

            finalSidewalk.setStartPoint(startPoint);
            finalSidewalk.setEndPoint(endPoint);

            sidewalks[inx]= finalSidewalk;
        }

        return sidewalks;
    }

    private void updateMapOverlay(int switcher) {

        if (switcher == 0) {

            for (int inx = 0; inx < sidewalks.length; inx++) {

                Sidewalk sidewalk = sidewalks[inx];
                LatLng start = sidewalk.getStartPoint();
                LatLng end = sidewalk.getEndPoint();

                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(start, end)
                        .width(5)
                        .color(Color.RED));

            }
        }

        if (switcher == 1) {

        }

        if (switcher == 2) {

        }
    }

    private boolean isNetworkAvailable() {
        boolean isAvailable = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
            isAvailable = true;

        return isAvailable;
    }

}
