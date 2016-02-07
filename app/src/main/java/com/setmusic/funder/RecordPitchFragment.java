package com.setmusic.funder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
    private long videoSize;
    private String linkSecure;
    private byte[] binaryBytes;
    private String binaryData;
    private String completeUri;
    private String ticketId;

    private String locationUrl;


    final Handler handler = new Handler();

    final Runnable apiFailure = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "apiFailure");

        }
    };

    final Runnable putRequestUI = new Runnable() {
        @Override
        public void run() {
            putRequest();

        }
    };

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
//            File fileHD = new File(Environment.getExternalStorageDirectory() + File
//                    .separator + "videoToUpload.jpg");
//            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileHD));
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
            Cursor returnCursor = context.getContentResolver().query(videoUrl, null, null, null,
                    null);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);

            InputStream in = null;
            try {
                in = context.getContentResolver().openInputStream(videoUrl);

                binaryBytes = IOUtils.toByteArray(in);
                binaryData = new String(binaryBytes);
                Log.d(TAG, "binaryBytes: " + binaryBytes.length);
            } catch (FileNotFoundException e) {
                Log.e("cpsample", "file not found " + videoUrl, e);
            } catch (IOException e) {
                Log.e("io", "error found " + videoUrl, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e("cpsample", "could not close stream", e);
                    }
                }
            }

            returnCursor.moveToFirst();
            videoSize = returnCursor.getLong(sizeIndex);
            Log.d(TAG, "" + videoSize);
            returnCursor.close();

            Toast.makeText(context, "Starting upload..." + videoUrl, Toast.LENGTH_SHORT).show();
            rootView.findViewById(R.id.instructions_container).setVisibility(View.GONE);
            rootView.findViewById(R.id.upload_progress_container).setVisibility(View.VISIBLE);

//            uploadVideo();

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
                        Log.d(TAG, "Code: " + Integer.toString(response.code()));
                        try {
                            JSONObject jsonResponse = new JSONObject(response.body().string());
                            Log.d(TAG, jsonResponse.toString());
                            linkSecure = jsonResponse.getString("upload_link_secure");
                            completeUri = jsonResponse.getString("complete_uri");
                            Log.d(TAG, linkSecure);

                            handler.post(putRequestUI);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void putRequest() {
        Log.d(TAG, binaryData);
        Log.d(TAG, linkSecure);
        Log.d(TAG, "" + videoSize);

        new ApiPutRequest(context).runMp4(linkSecure, binaryData, Long.toString(videoSize), new
                Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d(TAG, "ApiPutRequest: onFailure");
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Log.d(TAG, "ApiPutRequest: onResponse");
                        Log.d(TAG, "Code: " + Integer.toString(response.code()));
                        Log.d(TAG, "Headers: " + response.headers().toString());
                        verifyUpload();
                    }
                });
    }

    public void verifyUpload() {
        new ApiPutRequest(context).runVerify(linkSecure, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "ApiPostRequest: onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d(TAG, "ApiPostRequest: onResponse");
                Log.d(TAG, "Code: " + Integer.toString(response.code()));
                Log.d(TAG, "Headers: " + response.headers().toString());
                String content = response.header("Content-Length");
                String range = response.header("Range");
                String[] segs = range.split("-");
                Long lastByte = Long.parseLong(segs[segs.length - 1]);
                Log.d(TAG, "Range: " + lastByte);

                String resumeRange = "bytes " + (lastByte + 1) + "-" + Long.toString(videoSize) + "/" +
                        Long.toString(videoSize);
                Log.d(TAG, resumeRange);

                if (lastByte >= videoSize) {
                    completeUpload();
                } else {
                    resumeUpload(resumeRange);
                }
            }
        });
    }

    public void resumeUpload(String range) {
        Log.d(TAG, "resumeUpload");
        Log.d(TAG, range);
        Log.d(TAG, "" + videoSize);


        new ApiPutRequest(context).runResume(linkSecure, binaryData, Long.toString(videoSize),
                range, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d(TAG, "ApiPutRequest: onFailure");
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Log.d(TAG, "ApiPutRequest: onResponse");
                        Log.d(TAG, "Code: " + Integer.toString(response.code()));
                        Log.d(TAG, "Headers: " + response.headers().toString());

                        verifyUpload();
                    }
                });
    }

    public void completeUpload() {
        new ApiDeleteRequest(context).run("https://api.vimeo.com" + completeUri, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "ApiDeleteRequest: onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d(TAG, "ApiDeleteRequest: onResponse");
                Log.d(TAG, "Code: " + Integer.toString(response.code()));
                Log.d(TAG, "Headers: " + response.headers().toString());
                locationUrl = response.header("Location");

            }
        });
    }



}
