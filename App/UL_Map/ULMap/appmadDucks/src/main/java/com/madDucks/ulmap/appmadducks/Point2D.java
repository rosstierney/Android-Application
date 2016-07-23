package com.madDucks.ulmap.appmadducks;

public class Point2D {

    double lon;
    double lat;


    public Point2D() {

    }

    public Point2D(double lat, double lon) {
        super();
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double distance(Point2D dest) {

        double lat1 = dest.getLat();
        double lon1 = dest.getLon();
        double lon2 = lon;
        double lat2 = lat;
        double theta = lon1 - lon2;

        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);

        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;


        dist = dist * 1.609344;

        return (dist);

    }


    private double deg2rad(double deg) {

        return (deg * Math.PI / 180.0);

    }

    private double rad2deg(double rad) {

        return (rad * 180 / Math.PI);

    }


    public boolean isOnMap() {

        if (lat >= 52.67153000 && lat <= 52.679050) {
            if (lon >= -8.5789000 && lon <= -8.56390) {
                return true;
            }
        }

        return false;
    }


    public Double[] whatGrid() {
        Double[] grids = new Double[2];
        grids[1] = lat - 52.67153000;
        grids[0] = lon - -8.5789000;
        //grids[1] = grid / (0.00752 / 1158);
        //grids[0] = grid2 / (0.015 / 1405);

        return grids;


    }

}
