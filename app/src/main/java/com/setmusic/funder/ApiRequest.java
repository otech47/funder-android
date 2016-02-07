package com.setmusic.funder;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by oscarlafarga on 2/6/16.
 */
public class ApiRequest {
    public final OkHttpClient client = new OkHttpClient();
    public Call call;

    public void cancel() {
        client.getDispatcher().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                call.cancel();
            }
        });
    }
}
