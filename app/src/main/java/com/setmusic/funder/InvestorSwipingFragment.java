package com.setmusic.funder;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private ArrayList<String> companies;
    private ArrayAdapter<String> companyAdapter;

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

        //add the view via xml or programmatically
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id
                .investor_swipe_view);

        companies = new ArrayList<>();

        companies.add("Funder");
        companies.add("Setmusic");
        companies.add("Googluber");
        companies.add("Just Another Startup LLC");
        companies.add("Snapstagram");
        companies.add("Sign My NDA First Inc.");
        companies.add("Need Developers for App Idea Ltd.");

        //choose your favorite adapter
        companyAdapter = new ArrayAdapter<String>(context, R.layout.view_founder_card,
                R.id.companyText, companies);

        //set the listener and the adapter
        flingContainer.setAdapter(companyAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void onScroll(float v) {

            }

            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                companies.remove(0);
                companyAdapter.notifyDataSetChanged();
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
                companyAdapter.add("XML ".concat(String.valueOf(itemsInAdapter)));
                companyAdapter.notifyDataSetChanged();
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
//        new JSONAsyncTask(this, "https://player.vimeo.com/video/" + + "/config").execute();

    }

    public class JSONAsyncTask extends AsyncTask<Void, Void, String> {
        private Context mContext;
        private String mUrl;
        private String videoUrl;

        public JSONAsyncTask(Context context, String url) {
            mContext = context;
            mUrl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String resultString = null;
            resultString = getJSON(mUrl);

            return resultString;
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            Log.d(TAG, strings);
            try {
                JSONObject json = new JSONObject(strings);
                videoUrl = strings;
                ((VideoView) rootView.findViewById(R.id.companyPitchVideo)).setVideoPath(videoUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private String getJSON(String url) {
            HttpURLConnection c = null;
            try {
                URL u = new URL(url);
                c = (HttpURLConnection) u.openConnection();
                c.connect();
                int status = c.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        //disconnect error
                    }
                }
            }
            return null;
        }
    }

}
