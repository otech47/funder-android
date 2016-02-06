package com.setmusic.funder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

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
}
