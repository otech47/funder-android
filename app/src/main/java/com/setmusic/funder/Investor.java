package com.setmusic.funder;

import java.util.List;

/**
 * Created by oscarlafarga on 2/7/16.
 */
public class Investor {
    public int id;
    public String name;
    public String company;
    public String imageUrl;
    public String avgInvestment;
    public String location;
    public List<Swipe> swipes;

    public Investor() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAvgInvestment() {
        return avgInvestment;
    }

    public String getLocation() {
        return location;
    }

    public List<Swipe> getSwipes() {
        return swipes;
    }
}
