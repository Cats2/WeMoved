package com.wemove;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Cats2 on 27/11/2015.
 */
public class SiteLieu {

    private double lattitude;
    private double longitude;
    private int id;
    private String name;
    private double distance;

    public SiteLieu(int id, String name, double longitude, double latitude)
    {
        this.id = id;
        this.name = name;
        this.lattitude = latitude;
        this.longitude = longitude;
    }
    public SiteLieu(int id, String name, double distance)
    {
        this.id = id;
        this.name = name;
        this.distance = distance;
    }
    public SiteLieu()
    {

    }
    public double getDistance()
    {
        return distance;
    }

    public int getId()
    {
        return id;
    }
    public String getname()
    {
        return name;
    }
    public double getLattitude()
    {
        return lattitude;
    }
    public double getLongitude()
    {
        return longitude;
    }

    public String toString()
    {
        DecimalFormat df = new DecimalFormat("###.##");
        df.setRoundingMode(RoundingMode.DOWN);
        return name + " : " + df.format(distance) + " km";
    }
}
