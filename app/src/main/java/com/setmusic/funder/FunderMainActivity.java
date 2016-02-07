package com.setmusic.funder;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

public class FunderMainActivity extends FragmentActivity  {

    private static final String TAG = "FunderMainActivity";

    private final float HUE_SET_PURPLE = 283.0F;
    private final float HUE_SET_GREEN = 150.5F;

    private View mainRootView;
    private ImageButton investorSwipingButton;
    private ImageButton founderSwipingButton;
    private ImageButton recordPitchButton;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_funder_main);

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .diskCacheExtraOptions(480, 800, null)
//                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileCount(100)
//                .build();
//
//        ImageLoader.getInstance().init(config);

        mainRootView = getWindow().getDecorView();

        investorSwipingButton = (ImageButton)mainRootView.findViewById(R.id.investor_swiping);
        investorSwipingButton.setImageDrawable(new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_usd)
                .color(Color.WHITE));
        investorSwipingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndAddFragment("InvestorSwipingFragment", InvestorSwipingFragment.class,
                        true, null);
            }
        });

        founderSwipingButton = (ImageButton)mainRootView.findViewById(R.id.founder_swiping);
        founderSwipingButton.setImageDrawable(new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_user)
                .color(Color.WHITE));
        founderSwipingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndAddFragment("FounderSwipingFragment", FounderSwipingFragment.class,
                        true, null);
            }
        });

        recordPitchButton = (ImageButton)mainRootView.findViewById(R.id.record_pitch);
        recordPitchButton.setImageDrawable(new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_video_camera)
                .color(Color.WHITE));
        recordPitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndAddFragment("RecordPitchFragment", RecordPitchFragment.class,
                        true, null);
            }
        });

        investorSwipingButton.callOnClick();

    }

    public void createAndAddFragment(String tag, Class<? extends Fragment> fragClass, boolean
            addToBackStack, Bundle bundleData) {

        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
        if(frag == null) {
            Log.d(TAG, "frag is null: " + tag);

            try {
                frag = fragClass.newInstance();
                frag.setArguments(bundleData);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
                ft.replace(R.id.fragment_container, frag, tag);
                if(addToBackStack)
                    ft.addToBackStack(tag);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "frag is not null: " + tag);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
            ft.replace(R.id.fragment_container, frag, tag);
            if(addToBackStack)
                ft.addToBackStack(tag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }


    public void showLoader() {
        if(getWindow().getDecorView().findViewById(R.id.loader) != null) {
            getWindow().getDecorView().findViewById(R.id.loader).setVisibility(View.VISIBLE);
        }
    }

    public void hideLoader() {
        if(getWindow().getDecorView().findViewById(R.id.loader) != null) {
            getWindow().getDecorView().findViewById(R.id.loader).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");

        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onStart");

        super.onResume();

    }


//    private class AsyncTaskRunner extends AsyncTask<Intent, String, OAuthTokensResponse> {
//
//        private Intent mIntent;
//
//        public AsyncTaskRunner(Intent mIntent) {
//            this.mIntent = mIntent;
//        }
//
//        @Override
//        protected OAuthTokensResponse doInBackground(Intent... uris) {
//            Log.d(TAG, "doInBackground AUTH");
//            try {
//                return OAuth.completeAuthorization(BitmineMainActivity.this, Constants.CLIENT_ID,
//                        Constants.CLIENT_SECRET, mIntent.getData());
//            } catch (UnauthorizedException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        @Override
//
//        protected void onPostExecute(OAuthTokensResponse result) {
//            hideLoader();
//            Log.d(TAG, "onPostExecute AUTH: " + result.getAccessToken());
//            Bundle args = new Bundle();
//            args.putString("oauth", result.getAccessToken());
//            createAndAddFragment("WalletFragment", WalletFragment.class, true, args);
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            Log.d(TAG, "onPreExecute AUTH");
//            showLoader();
//        }
//
//    }
//
//    public class JSONAsyncTask extends AsyncTask<Void, Void, String> {
//        private Context mContext;
//        private String mUrl;
//
//        public JSONAsyncTask(Context context, String url) {
//            mContext = context;
//            mUrl = url;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String resultString = null;
//            resultString = getJSON(mUrl);
//
//            return resultString;
//        }
//
//        @Override
//        protected void onPostExecute(String strings) {
//            super.onPostExecute(strings);
//            try {
//                JSONObject json = new JSONObject(strings);
//                float usdFloat = (float)json.getJSONObject("bpi").getJSONObject("USD").getDouble
//                        ("rate_float");
//
//                double rounded = (Math.round(usdFloat * 100.0)/100.0);
//
//                ((TextView) mainRootView.findViewById(R.id.exchange_rate)).setText("$"+rounded);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        private String getJSON(String url) {
//            HttpURLConnection c = null;
//            try {
//                URL u = new URL(url);
//                c = (HttpURLConnection) u.openConnection();
//                c.connect();
//                int status = c.getResponseCode();
//                switch (status) {
//                    case 200:
//                    case 201:
//                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
//                        StringBuilder sb = new StringBuilder();
//                        String line;
//                        while ((line = br.readLine()) != null) {
//                            sb.append(line+"\n");
//                        }
//                        br.close();
//                        return sb.toString();
//                }
//
//            } catch (Exception ex) {
//                return ex.toString();
//            } finally {
//                if (c != null) {
//                    try {
//                        c.disconnect();
//                    } catch (Exception ex) {
//                        //disconnect error
//                    }
//                }
//            }
//            return null;
//        }
//    }


}
