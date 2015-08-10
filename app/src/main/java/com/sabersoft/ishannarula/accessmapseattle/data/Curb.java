package com.sabersoft.ishannarula.accessmapseattle.data;


import com.google.android.gms.maps.model.LatLng;

public class Curb {

    private LatLng point;
    private double angle;
    private int curbID;

    public LatLng getPoint() {
        return point;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getCurbID() {
        return curbID;
    }

    public void setCurbID(int curbID) {
        this.curbID = curbID;
    }
}
