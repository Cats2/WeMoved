package com.wemove;

import java.util.Comparator;

/**
 * Created by utilisateur on 08/02/2015.
 */
public class Site_select {

    String nom;
    int id;

    public Site_select(String nom, int id)
    {
        this.nom = nom;
        this.id = id;
    }

    public String getNom()
    {
        return nom;
    }

    public int getId()
    {
        return id;
    }


}

class Comparatorsite implements Comparator<Site_select> {
    @Override
    public int compare(Site_select a, Site_select b) {
        return a.getNom().compareToIgnoreCase(b.getNom());
    }
}

