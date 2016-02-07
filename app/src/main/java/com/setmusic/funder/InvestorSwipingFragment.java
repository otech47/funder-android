package com.setmusic.funder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarlafarga on 2/6/16.
 */
public class InvestorSwipingFragment extends Fragment {
    private static final String TAG = "InvestorSwipingFragment";

    private FunderMainActivity mainActivity;
    private Context context;
    private View rootView;
    private VideoView videoView;

    String videoUrl;

    private List<Founder> founders;
    private FounderCardAdapter founderCardAdapter;

    final android.os.Handler handler = new android.os.Handler();
    final Runnable apiFailure = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "apiFailure");

        }
    };
    final Runnable updateVideoUI = new Runnable() {
        @Override
        public void run() {
            updateVideo();

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

        rootView = inflater.inflate(R.layout.fragment_investor_swiping,container,false);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id
                .investor_swipe_view);

        founders = new ArrayList<>();

        founders.add(new Founder("Evan Martinez"));
        founders.add(new Founder("Oscar Lafarga"));

        //choose your favorite adapter
        founderCardAdapter = new FounderCardAdapter(context, founders);

        //set the listener and the adapter
        flingContainer.setAdapter(founderCardAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void onScroll(float v) {

            }

            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                founders.remove(0);
                founderCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(context, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(context, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                founderCardAdapter.add(new Founder("Founder"));
                founderCardAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                itemsInAdapter++;
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(context, "Click!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
//        new ApiGetRequest(context).run("https://player.vimeo.com/video/154461399/config", new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.d(TAG, "ApiGetRequest: onFailure");
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                Log.d(TAG, "ApiGetRequest: onResponse");
//                Log.d(TAG, Integer.toString(response.code()));
//                Log.d(TAG, response.headers().toString());
//                try {
//                    JSONObject json = new JSONObject(response.body().string());
//                    Log.d(TAG, json.toString());
//                    videoUrl = json.getJSONObject("request").getJSONObject("files")
//                            .getJSONArray("progressive").getJSONObject(0).getString("url");
//                    handler.post(updateVideoUI);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

    }

    public void updateVideo() {
        videoView.setVideoPath(videoUrl);
        videoView.start();
    }

}
