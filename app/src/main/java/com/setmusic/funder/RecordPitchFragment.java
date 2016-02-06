package com.setmusic.funder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * Created by oscarlafarga on 2/6/16.
 */
public class RecordPitchFragment extends Fragment {
    private static final String TAG = "RecordPitchFragment";
    private static final int REQUEST_VIDEO_CAPTURE = 10;
    private static final int RESULT_OK = -1;

    private FunderMainActivity mainActivity;
    private Context context;
    private View rootView;
    private VideoView pitchVideoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        mainActivity = (FunderMainActivity)getActivity();
        context = mainActivity.getApplicationContext();

        rootView = inflater.inflate(R.layout.fragment_record_pitch,container,false);
        pitchVideoView = (VideoView)rootView.findViewById(R.id.pitch_video_container);

        dispatchTakeVideoIntent();

        return rootView;
    }

    private void dispatchTakeVideoIntent() {
        Log.d(TAG, "dispatchTakeVideoIntent");

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult");
        Log.d(TAG, Integer.toString(resultCode));

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            Log.d(TAG, videoUri.toString());

            pitchVideoView.setVideoURI(videoUri);
        }
    }
}
