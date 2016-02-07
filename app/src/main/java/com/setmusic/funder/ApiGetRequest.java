package com.setmusic.funder;

import android.content.Context;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;

/**
 * Created by oscarlafarga on 2/7/16.
 */
public class ApiGetRequest extends ApiRequest {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MP4
            = MediaType.parse("multipart/form-data");
    private Context context;

    public ApiGetRequest(Context context) {
        this.context = context;

    }

    public void run(String route, Callback callback) {

        Request request = new Request.Builder()
                .url(route)
                .get()
                .addHeader("Authorization", "bearer " + Constants.VIMEO_KEY)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        this.call = call;
    }
}
