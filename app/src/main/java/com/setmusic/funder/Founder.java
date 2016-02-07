package com.setmusic.funder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by oscarlafarga on 2/7/16.
 */
public class Founder {
    public String id;

    public String videoUrl;
    public String company;
    public String name;
    public String description;
    public String imageUrl;
    public String foundingDate;

    public String raise;
    public String equity;

    public List<Swipe> swipes;

    public Founder(JSONObject json) {
        try {
            this.id = json.getString("_id");
            this.name = json.getString("name");
            this.company = json.getString("company");
            this.description = json.getString("description");
            if(json.has("video_url")) {
                this.videoUrl = json.getString("video_url");
            } else {
                this.videoUrl = "https://d1wbxby8dwa4u.cloudfront.net/brown/Suh+Dude.mp4";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Founder(String name) {
        this.name = name;
        this.raise = "$500K";
        this.equity = "10%";
        this.description = "A company description";
        this.company = "Funder";
        this.foundingDate = "Founded 2015";
        this.videoUrl = "https://skyfiregcs-a.akamaihd.net/exp=1454834098~acl=%2A%2F477292419.mp4%2A~hmac=7ed1ed7acf3a624d67b6caef73198120e74221214803500fecb910d1db3a35ba/2tierchgci/vimeo-prod-skyfire-std-us/01/892/6/154461399/477292419.mp4";

    }

    public String getId() {
        return id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Swipe> getSwipes() {
        return swipes;
    }

    public String getRaise() {
        return raise;
    }

    public String getEquity() {
        return equity;
    }
}
