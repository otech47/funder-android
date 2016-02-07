package com.setmusic.funder;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarlafarga on 2/7/16.
 */
public class InvestorCardAdapter extends BaseAdapter {
    private static final String TAG = "InvestorCardAdapter";

    LayoutInflater inflater;
    public List<Investor> investors;
    private Context context;
    private DisplayImageOptions options;
    private int limit;
    public AudioManager audioManager;
    public int volumeToggle;

    public InvestorCardAdapter(Context context, List<Investor> investors) {
        this.context = context;
        this.investors = investors;
        this.inflater = LayoutInflater.from(context);
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        this.volumeToggle = 0;
    }
    public void clear() {
        investors = new ArrayList<>();
    }

    public void add(Investor i) {
        this.investors.add(i);
    }

    @Override
    public int getCount() {
        return investors.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final InvestorViewHolder holder;
        Investor investor = investors.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.view_investor_card, parent, false);
            holder = new InvestorViewHolder();
            holder.investorName = (TextView) view.findViewById(R.id.investorName);
            holder.location = (TextView) view.findViewById(R.id.locationText);
            holder.avgInvestment = (TextView) view.findViewById(R.id.avgInvestmentText);
            holder.image = (ImageView) view.findViewById(R.id.investorImage);
            holder.dollarIcon = (ImageView) view.findViewById(R.id.avgInvestmentIcon);
            holder.locationIcon = (ImageView) view.findViewById(R.id.locationIcon);
            view.setTag(holder);
        } else {
            holder = (InvestorViewHolder) view.getTag();
        }
        holder.investorName.setText(investor.getName());
        holder.location.setText(investor.getLocation());
        holder.avgInvestment.setText(investor.getAvgInvestment());

        holder.dollarIcon.setImageDrawable(new IconicsDrawable(context)
                .icon(FontAwesome.Icon.faw_usd)
                .color(Color.WHITE));
        holder.locationIcon.setImageDrawable(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_location_on)
                .color(Color.WHITE));


        return view;
    }
    public class InvestorViewHolder {
        public ImageView image;
        public ImageView locationIcon;

        public ImageView dollarIcon;

        public TextView investorName;
        public TextView location;
        public TextView avgInvestment;

    }
}
