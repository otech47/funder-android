package com.setmusic.funder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private FloatingActionButton recordButton;
    private FloatingActionButton uploadButton;

    private Uri videoUrl;
    private int videoSize;

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

        rootView = inflater.inflate(R.layout.fragment_record_pitch, container, false);

        recordButton = (FloatingActionButton)rootView.findViewById(R.id.record_button);
        recordButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));
        recordButton.setImageDrawable(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_videocam)
                .color(context.getResources().getColor(R.color.black)));
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakeVideoIntent();
            }
        });

        uploadButton = (FloatingActionButton)rootView.findViewById(R.id.upload_button);
        uploadButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));
        uploadButton.setImageDrawable(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_file_upload)
                .color(context.getResources().getColor(R.color.black)));
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo();
            }
        });

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
            videoUrl = intent.getData();
            Log.d(TAG, videoUrl.toString());
            Toast.makeText(context, "Starting upload..." + videoUrl, Toast.LENGTH_SHORT).show();
            rootView.findViewById(R.id.instructions_container).setVisibility(View.GONE);
            rootView.findViewById(R.id.upload_progress_container).setVisibility(View.VISIBLE);


            uploadVideo();
        }
    }

    public void uploadVideo() {
        String postDataJson = "{\"type\": \"streaming\"}";

        new ApiPostRequest(context).run("https://api.vimeo.com/me/videos", postDataJson, new
                Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d(TAG, "ApiPostRequest: onFailure");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Log.d(TAG, "ApiPostRequest: onResponse");
                        try {
                            JSONObject jsonResponse = new JSONObject(response.body().string());
                            Log.d(TAG, jsonResponse.toString());
                            String uploadLinkSecure = jsonResponse.getString("upload_link_secure");
                            Log.d(TAG, uploadLinkSecure);
                            int size = 22343;

                            String putDataJson = "{\"Content-Length\": \"" + Integer.toString(size)
                                    + "\",\"Content-Type\": \"video/mp4\"}";

                            new ApiPutRequest(context).run(uploadLinkSecure, putDataJson, new Callback() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                    Log.d(TAG, "ApiPutRequest: onFailure");

                                }

                                @Override
                                public void onResponse(Response response) throws IOException {
                                    Log.d(TAG, "ApiPutRequest: onResponse");

                                    try {
                                        JSONObject jsonResponse = new JSONObject(response.body()
                                                .string());
                                        Log.d(TAG, jsonResponse.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



}
