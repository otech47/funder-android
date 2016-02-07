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
    public List<Founder> investors;
    private Context context;
    private DisplayImageOptions options;
    private int limit;
    public AudioManager audioManager;
    public int volumeToggle;

    public InvestorCardAdapter(Context context, List<Founder> investors) {
        this.context = context;
        this.investors = investors;
        this.inflater = LayoutInflater.from(context);
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        this.volumeToggle = 0;
    }
    public void clear() {
        investors = new ArrayList<>();
    }

    public void add(Founder f) {
        this.investors.add(f);
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
        Founder founder = investors.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.view_founder_card, parent, false);
            holder = new InvestorViewHolder();
            holder.video = (VideoView) view.findViewById(R.id.companyPitchVideo);
            holder.companyName = (TextView) view.findViewById(R.id.companyName);
            holder.founderName = (TextView) view.findViewById(R.id.founderName);
            holder.mute = (ImageView) view.findViewById(R.id.mute_button);
            holder.replay = (ImageView) view.findViewById(R.id.replay_button);
            view.setTag(holder);
        } else {
            holder = (InvestorViewHolder) view.getTag();
        }
        holder.companyName.setText(founder.getCompany());
        holder.founderName.setText(founder.getName());

        holder.video.setVideoPath(founder.getVideoUrl());
        holder.video.start();

        holder.mute.setImageDrawable(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_volume_mute)
                .color(Color.WHITE));
        holder.mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeToggle, 0);
                if (volumeToggle == 6) {
                    volumeToggle = 0;
                    holder.mute.setImageDrawable(new IconicsDrawable(context)
                            .icon(GoogleMaterial.Icon.gmd_volume_off)
                            .color(Color.WHITE));
                } else {
                    volumeToggle = 6;
                    holder.mute.setImageDrawable(new IconicsDrawable(context)
                            .icon(GoogleMaterial.Icon.gmd_volume_mute)
                            .color(Color.WHITE));
                }

            }
        });

        holder.replay.setImageDrawable(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_replay)
                .color(Color.WHITE));
        holder.replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 6, 0);
                volumeToggle = 0;
                holder.video.start();
            }
        });


        return view;
    }
    public class InvestorViewHolder {
        public VideoView video;
        public ImageView mute;
        public ImageView replay;
        public TextView companyName;
        public TextView founderName;
        public TextView raise;
        public TextView equity;

    }
}
