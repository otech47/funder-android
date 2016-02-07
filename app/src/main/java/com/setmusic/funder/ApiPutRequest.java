package com.setmusic.funder;

import android.content.Context;

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
    private Context context;

    public ApiPutRequest(Context context) {
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        this.context = context;

    }

    public void run(String route, String json, Callback callback) {
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
}
