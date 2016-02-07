package com.setmusic.funder;

/**
 * Created by oscarlafarga on 2/7/16.
 */
public class Swipe {
    public int id;
    public boolean type;
    public String timestamp;
    public Founder founder;
    public Investor investor;

    public Swipe() {
    }

    public int getId() {
        return id;
    }

    public boolean isType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Founder getFounder() {
        return founder;
    }

    public Investor getInvestor() {
        return investor;
    }
}
