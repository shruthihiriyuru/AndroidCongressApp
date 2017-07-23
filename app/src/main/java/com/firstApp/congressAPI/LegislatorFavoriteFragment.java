package com.firstApp.congressAPI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shruthihiriyuru on 11/24/16.
 */

public class LegislatorFavoriteFragment extends Fragment implements AdapterView.OnItemClickListener{
    View legislatorView;
    private List<LegislatorData.ResultsBean> results;
    private ListView lv;
    Map<String, Integer> mapIndex;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        legislatorView = inflater.inflate(R.layout.legislator_list,null);
        lv = (ListView) legislatorView.findViewById(R.id.list);
        SharedPreferences wSharedPreferences = getActivity().getSharedPreferences(
                "LegislatorFavouritesList", getActivity().MODE_APPEND);

        results = new ArrayList<LegislatorData.ResultsBean>();

        Map<String, ?> allEntries = wSharedPreferences.getAll();
        for(Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Gson gson = new Gson();
            String json = (String)entry.getValue();
            LegislatorData.ResultsBean legislator = gson.fromJson(json, LegislatorData.ResultsBean.class);
            results.add(legislator);
        }
        Collections.sort(results, new Comparator<LegislatorData.ResultsBean>() {
            @Override
            public int compare(LegislatorData.ResultsBean one, LegislatorData.ResultsBean two) {
                return one.getLast_name().compareTo(two.getLast_name());
            }
        });

        Collections.sort(results);

        ArrayList<LegislatorData.ResultsBean> arrayOfLegislators = new ArrayList<LegislatorData.ResultsBean>(results);

        LegislatorAdapter adapter = new LegislatorAdapter(getActivity(), arrayOfLegislators);

        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(this);
        getIndexList(results);
        displayIndex();

        return legislatorView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), DisplayLegislatorActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Legislator Intent", (Serializable) results.get(position));

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getIndexList(List<LegislatorData.ResultsBean> legislators) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < legislators.size(); i++) {
            String legLastName = legislators.get(i).getLast_name();
            String index = legLastName.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }


    private void displayIndex() {
        LinearLayout indexLayout = (LinearLayout) legislatorView.findViewById(R.id.side_index);

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getActivity().getLayoutInflater().inflate(
                    R.layout.side_index, null);
            textView.setText(index);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView selectedIndex = (TextView) view;
                    lv.setSelection(mapIndex.get(selectedIndex.getText()));
                }
            });

            indexLayout.addView(textView);

        }
    }
}
