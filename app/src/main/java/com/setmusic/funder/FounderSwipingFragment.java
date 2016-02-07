package com.setmusic.funder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarlafarga on 2/6/16.
 */
public class FounderSwipingFragment extends Fragment {
    private static final String TAG = "FounderSwipingFragment";

    private FunderMainActivity mainActivity;
    private Context context;
    private View rootView;

    private List<Investor> investors;
    private InvestorCardAdapter investorAdapter;

    private int match;

    final Handler handler = new Handler();
    final Runnable apiFailure = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "apiFailure");

        }
    };
    final Runnable updateSwiperUI = new Runnable() {
        @Override
        public void run() {
            configureSwipePager();

        }
    };
    final Runnable checkMatchUI = new Runnable() {
        @Override
        public void run() {
            checkMatch();

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

        rootView = inflater.inflate(R.layout.fragment_founder_swiping, container, false);

        kickOffInvestorsApiRequest();



        return rootView;
    }

    public void kickOffInvestorsApiRequest() {
        Log.d(TAG, "kickOffInvestorsApiRequest");

        new ApiGetRequest(context).run("http://funder-app.azurewebsites.net/api/investors", new
                Callback
                        () {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d(TAG, "ApiGetRequest: onFailure");

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Log.d(TAG, "ApiGetRequest: onResponse");
                        Log.d(TAG, Integer.toString(response.code()));
                        Log.d(TAG, response.headers().toString());
                        try {
                            JSONArray investorsJson = new JSONArray(response.body().string());
                            Log.d(TAG, investorsJson.toString());
                            investors = new ArrayList<>();
                            for (int i = 0; i < investorsJson.length(); i++) {
                                investors.add(new Investor(investorsJson.getJSONObject(i)));
                            }
                            handler.post(updateSwiperUI);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void configureSwipePager() {
        Log.d(TAG, "configureSwipePager");
        Log.d(TAG, "" + investors.size());

        //add the view via xml or programmatically
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id
                .founder_swipe_view);

        //choose your favorite adapter
        investorAdapter = new InvestorCardAdapter(context, investors);

        //set the listener and the adapter
        flingContainer.setAdapter(investorAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void onScroll(float v) {

            }

            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Toast.makeText(context, "Left!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onLeftCardExit: " + dataObject.toString());
                Log.d(TAG, "onLeftCardExit: founders" + investors.size());
                investors.remove(0);
                investorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(context, "Right!", Toast.LENGTH_SHORT).show();
                swipeRight(investors.get(0).getCompany(), "Elefunder");
                investors.remove(0);
                investorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Log.d(TAG, "onAdapterAboutToEmpty");
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(context, "Click!", Toast.LENGTH_SHORT).show();
            }
        });
        investorAdapter.notifyDataSetChanged();

    }

    public void swipeRight(String investor, String user) {
        String postDataJson = "{\"target\": \"" + investor + "\", " +
                "\"type\":true,\"user\":\"" + user + "\"}";
        new ApiPostRequest(context).run("http://funder-app.azurewebsites.net/api/swipe/new",
                postDataJson, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d(TAG, "ApiPostRequest: onFailure");
                        handler.post(apiFailure);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Log.d(TAG, "ApiPostRequest: onResponse");
                        Log.d(TAG, "" + response.code());
                        Log.d(TAG, response.headers().toString());

                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            match = json.getInt("match");
                            handler.post(checkMatchUI);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    public void checkMatch() {
        Log.d(TAG, "checkMatch: " + match);

    }
}
