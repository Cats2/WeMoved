package com.wemove;

import java.util.Comparator;

/**
 * Created by Cats2 on 27/11/2015.
 */
public class ComparatorSiteLieu implements Comparator<SiteLieu> {
    @Override
    public int compare(SiteLieu a, SiteLieu b) {
        return compare(a.getDistance(), b.getDistance());
    }

    public int compare(double p1, double p2) {
        if (p1 < p2) return -1;
        if (p1 > p2) return 1;
        return 0;
    }

}
