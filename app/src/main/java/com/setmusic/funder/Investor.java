package com.setmusic.funder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by oscarlafarga on 2/7/16.
 */
public class Investor {
    public String id;
    public String name;
    public String company;
    public String imageUrl;
    public String avgInvestment;
    public String location;
    public List<Swipe> swipes;

    public Investor() {
    }

    public Investor(JSONObject json) {
        try {
            this.id = json.getString("_id");
            this.company = json.getString("company");
            this.avgInvestment = json.getString("average_investment");
            this.location = json.getString("location");
            this.imageUrl = json.getString("image_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Investor(String company) {
        this.company = company;
        this.avgInvestment = "$50K - $300K";
        this.location = "Providence, RI";
        this.imageUrl = "http://blogdailyherald.com/wp-content/uploads/2015/02/Hack@Brown.png";
    }

    public String getId() {
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
