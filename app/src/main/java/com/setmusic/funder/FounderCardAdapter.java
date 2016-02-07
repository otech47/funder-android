package com.setmusic.funder;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarlafarga on 2/7/16.
 */
public class FounderCardAdapter extends BaseAdapter {

    private static final String TAG = "FounderCardAdapter";

    LayoutInflater inflater;
    public List<Founder> founders;
    private Context context;
    private DisplayImageOptions options;
    private int limit;
    public AudioManager audioManager;
    public int volumeToggle;

    public FounderCardAdapter(Context context, List<Founder> founders) {
        this.context = context;
        this.founders = founders;
        this.inflater = LayoutInflater.from(context);
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        this.volumeToggle = 0;
    }
    public void clear() {
        founders = new ArrayList<>();
    }

    public void add(Founder f) {
        this.founders.add(f);
    }

    @Override
    public int getCount() {
        return founders.size();
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
        final FounderViewHolder holder;
        Founder founder = founders.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.view_founder_card, parent, false);
            holder = new FounderViewHolder();
            holder.video = (VideoView) view.findViewById(R.id.companyPitchVideo);
            holder.companyName = (TextView) view.findViewById(R.id.companyName);
            holder.founderName = (TextView) view.findViewById(R.id.founderName);
            holder.mute = (ImageView) view.findViewById(R.id.mute_button);
            holder.replay = (ImageView) view.findViewById(R.id.replay_button);
            view.setTag(holder);
        } else {
            holder = (FounderViewHolder) view.getTag();
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




}
