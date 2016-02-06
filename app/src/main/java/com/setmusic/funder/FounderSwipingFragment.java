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

/**
 * Created by oscarlafarga on 2/6/16.
 */
public class FounderSwipingFragment extends Fragment {
    private static final String TAG = "FounderSwipingFragment";

    private FunderMainActivity mainActivity;
    private Context context;
    private View rootView;

    private ArrayList<String> investors;
    private ArrayAdapter<String> investorAdapter;

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

        rootView = inflater.inflate(R.layout.fragment_founder_swiping,container,false);

        //add the view via xml or programmatically
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id
                .founder_swipe_view);

        investors = new ArrayList<>();
        investors.add("Hack@Brown Ventures");
        investors.add("The Zuck");
        investors.add("Andreessen Horowitz");
        investors.add("Prick Investor Group Inc.");
        investors.add("Microsoft Ventures");
        investors.add("I Hate Your Company LLC");
        investors.add("Make Us Money Ltd.");

        //choose your favorite adapter
        investorAdapter = new ArrayAdapter<String>(context, R.layout.view_investor_card,
                R.id.investorText, investors);

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
                investors.remove(0);
                investorAdapter.notifyDataSetChanged();
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
                investorAdapter.add("XML ".concat(String.valueOf(itemsInAdapter)));
                investorAdapter.notifyDataSetChanged();
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
