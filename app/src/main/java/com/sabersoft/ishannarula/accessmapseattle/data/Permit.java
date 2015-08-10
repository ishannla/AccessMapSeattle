package com.sabersoft.ishannarula.accessmapseattle.data;


import com.google.android.gms.maps.model.LatLng;

public class Permit {

    private LatLng point;
    private int permitID;

    public LatLng getPoint() {
        return point;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }

    public int getPermitID() {
        return permitID;
    }

    public void setPermitID(int permitID) {
        this.permitID = permitID;
    }
}
