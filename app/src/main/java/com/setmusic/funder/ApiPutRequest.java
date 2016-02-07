package com.setmusic.funder;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.concurrent.TimeUnit;

/**
 * Created by oscarlafarga on 2/6/16.
 */
public class ApiPutRequest extends ApiRequest {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MP4
            = MediaType.parse("multipart/form-data");
    private Context context;

    public ApiPutRequest(Context context) {
        this.context = context;

    }

    public void runJson(String route, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        String headerType;
        String headerValue;

        Request request = new Request.Builder()
                .url(route)
                .put(body)
                .addHeader("Authorization", "bearer " + Constants.VIMEO_KEY)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        this.call = call;
    }

    public void runMp4(String route, String data, String contentLength, Callback callback) {
        RequestBody body = RequestBody.create(MP4, data);
        String headerType;
        String headerValue;

        Request request = new Request.Builder()
                .url(route)
                .addHeader("Authorization", "bearer " + Constants.VIMEO_KEY)
                .addHeader("Content-Length", contentLength)
                .addHeader("Content-Type", "video/mp4")
                .put(body)
                .build();

        Log.d("runMp4", request.toString());
        Log.d("runMp4", request.headers().toString());

        Call call = client.newCall(request);
        call.enqueue(callback);
        this.call = call;
    }

    public void runResume(String route, String data, String contentLength, String
            contentRange, Callback callback) {
        RequestBody body = RequestBody.create(MP4, data);

        Request request = new Request.Builder()
                .url(route)
                .addHeader("Authorization", "bearer " + Constants.VIMEO_KEY)
                .addHeader("Content-Length", contentLength)
                .addHeader("Content-Range", contentRange)
                .addHeader("Content-Type", "video/mp4")
                .put(body)
                .build();

        Log.d("runResume", request.toString());
        Log.d("runResume", request.headers().toString());

        Call call = client.newCall(request);
        call.enqueue(callback);
        this.call = call;
    }

    public void runVerify(String route, Callback callback) {

        Request request = new Request.Builder()
                .url(route)
                .addHeader("Authorization", "bearer " + Constants.VIMEO_KEY)
                .addHeader("Content-Length", "0")
                .addHeader("Content-Range", "bytes */*")
                .put(null)
                .build();

        Log.d("runVerify", request.toString());
        Log.d("runVerify", request.headers().toString());

        Call call = client.newCall(request);
        call.enqueue(callback);
        this.call = call;
    }

}
