package com.setmusic.funder;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

/**
 * Created by oscarlafarga on 2/6/16.
 */
public class ApiDeleteRequest extends ApiRequest {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MP4
            = MediaType.parse("multipart/form-data");
    private Context context;

    public ApiDeleteRequest(Context context) {
        this.context = context;

    }

    public void run(String route, Callback callback) {
 
        Request request = new Request.Builder()
                .url(route)
                .delete()
                .addHeader("Authorization", "bearer " + Constants.VIMEO_KEY)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        this.call = call;
    }

}
