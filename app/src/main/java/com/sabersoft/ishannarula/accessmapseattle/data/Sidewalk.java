package com.sabersoft.ishannarula.accessmapseattle.data;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

public class Sidewalk {

    private LatLng startPoint;
    private LatLng endPoint;
    private double grade;
    private int sidewalkID;

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(LatLng endPoint) {
        this.endPoint = endPoint;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getSidewalkID() {
        return sidewalkID;
    }

    public void setSidewalkID(int sidewalkID) {
        this.sidewalkID = sidewalkID;
    }

}
