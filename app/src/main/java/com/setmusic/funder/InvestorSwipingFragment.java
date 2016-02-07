package com.setmusic.funder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

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
public class InvestorSwipingFragment extends Fragment {
    private static final String TAG = "InvestorSwipingFragment";

    private FunderMainActivity mainActivity;
    private Context context;
    private View rootView;
    private RelativeLayout matchOverlay;
    private LinearLayout buttonContainer;
    private ImageView investorMatchIcon;
    private ImageView founderMatchIcon;
    private Button callNow;
    private Button later;

    private SwipeFlingAdapterView swipeView;

    private List<Founder> founders;
    private FounderCardAdapter founderCardAdapter;

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

        rootView = inflater.inflate(R.layout.fragment_investor_swiping, container, false);
        matchOverlay = (RelativeLayout)rootView.findViewById(R.id.matchOverlayContainer);
        buttonContainer = (LinearLayout)rootView.findViewById(R.id.button_container);

        investorMatchIcon = (ImageView)rootView.findViewById(R.id.investorMatchIcon);
        founderMatchIcon = (ImageView)rootView.findViewById(R.id.founderMatchIcon);
        callNow = (Button)rootView.findViewById(R.id.call_now);
        later = (Button)rootView.findViewById(R.id.later);

        callNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sky = new Intent("android.intent.action.VIEW");
                sky.setData(Uri.parse("skype:emart21?call&video=true"));
                startActivity(sky);
            }
        });

        kickOffFoundersApiRequest();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    public void kickOffFoundersApiRequest() {
        Log.d(TAG, "kickOffInvestorsApiRequest");

        new ApiGetRequest(context).run("http://funder-app.azurewebsites.net/api/founders", new Callback
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
                    JSONArray foundersJson = new JSONArray(response.body().string());
                    Log.d(TAG, foundersJson.toString());
                    founders = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        founders.add(new Founder(foundersJson.getJSONObject(i)));
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
        Log.d(TAG, "" + founders.size());

        swipeView = (SwipeFlingAdapterView) rootView.findViewById(R.id
                .investor_swipe_view);
        //choose your favorite adapter
        founderCardAdapter = new FounderCardAdapter(context, founders);

        //set the listener and the adapter
        swipeView.setAdapter(founderCardAdapter);
        swipeView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
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
                Log.d(TAG, "onLeftCardExit: " + dataObject.toString());
                Log.d(TAG, "onLeftCardExit: founders" + founders.size());
                founders.remove(0);
                founderCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d(TAG, "onRightCardExit: " + dataObject.toString());
                Log.d(TAG, "onRightCardExit: founders" + founders.get(0));
                swipeRight("Andreessen Horowitz", founders.get(0).getCompany());
                founders.remove(0);
                founderCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                Log.d(TAG, "onAdapterAboutToEmpty");
            }
        });

        // Optionally add an OnItemClickListener
        swipeView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

            }
        });
        founderCardAdapter.notifyDataSetChanged();

    }

    public void swipeRight(String investor, String user) {
        String postDataJson = "{\"user\": \"" + investor + "\", " +
                "\"type\":true,\"target\":\"" + user + "\"}";
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
        if(match > 0) {
            matchOverlay.setVisibility(View.VISIBLE);
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setFillAfter(true);
            fadeIn.setDuration(1200);
            fadeIn.setInterpolator(new DecelerateInterpolator());
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    matchOverlay.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onAnimationStart: " + matchOverlay.getAlpha());
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    swipeView.setVisibility(View.GONE);
                    Log.d(TAG, "onAnimationEnd: " + matchOverlay.getAlpha());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            buttonContainer.setVisibility(View.VISIBLE);
            Animation fadeIn2 = new AlphaAnimation(0, 1);
            fadeIn2.setFillAfter(true);
            fadeIn2.setDuration(600);
            fadeIn2.setStartOffset(600);
            fadeIn2.setInterpolator(new DecelerateInterpolator());
            fadeIn2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    buttonContainer.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onAnimationStart: " + matchOverlay.getAlpha());
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.d(TAG, "onAnimationEnd: " + matchOverlay.getAlpha());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            ScaleAnimation growInv = new ScaleAnimation(0,1,0,1,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            growInv.setInterpolator(new BounceInterpolator());
            growInv.setFillAfter(true);
            growInv.setDuration(2000);

            ScaleAnimation growFounder = new ScaleAnimation(0,1,0,1,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            growFounder.setInterpolator(new BounceInterpolator());
            growFounder.setFillAfter(true);
            growFounder.setDuration(2000);

            TranslateAnimation slideU1 = new TranslateAnimation(0,0,1000, 0);
            slideU1.setInterpolator(new BounceInterpolator());
            slideU1.setStartOffset(1000);
            slideU1.setDuration(900);

            TranslateAnimation slideUp2 = new TranslateAnimation(0,0,1000, 0);
            slideUp2.setInterpolator(new DecelerateInterpolator());
            slideUp2.setStartOffset(1200);
            slideUp2.setDuration(800);

            buttonContainer.startAnimation(fadeIn2);
            investorMatchIcon.startAnimation(growInv);
            founderMatchIcon.startAnimation(growFounder);
            matchOverlay.startAnimation(fadeIn);
            callNow.startAnimation(slideU1);
            later.startAnimation(slideUp2);
            Log.d(TAG, "startAnimation: " + matchOverlay.getAlpha());

        }
    }


}
