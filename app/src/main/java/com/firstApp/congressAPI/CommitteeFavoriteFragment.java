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
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by shruthihiriyuru on 11/24/16.
 */

public class CommitteeFavoriteFragment extends Fragment implements AdapterView.OnItemClickListener{
    View committeeView;
    private List<CommitteeData.ResultsBean> results;
    private ListView lv;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        committeeView = inflater.inflate(R.layout.committee_list,null);
        lv = (ListView) committeeView.findViewById(R.id.committeelist);
        SharedPreferences wSharedPreferences = getActivity().getSharedPreferences(
                "CommitteeFavouritesList", getActivity().MODE_APPEND);

        results = new ArrayList<CommitteeData.ResultsBean>();

        Map<String, ?> allEntries = wSharedPreferences.getAll();
        for(Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Gson gson = new Gson();
            String json = (String)entry.getValue();
            CommitteeData.ResultsBean committee = gson.fromJson(json, CommitteeData.ResultsBean.class);
            results.add(committee);
        }

        Collections.sort(results, new Comparator<CommitteeData.ResultsBean>() {
            @Override
            public int compare(CommitteeData.ResultsBean one, CommitteeData.ResultsBean two) {
                return one.getName().compareTo(two.getName());
            }
        });

        Collections.sort(results);

        ArrayList<CommitteeData.ResultsBean> arrayOfCommittees = new ArrayList<CommitteeData.ResultsBean>(results);

        CommitteeAdapter adapter = new CommitteeAdapter(getActivity(), arrayOfCommittees);

        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(this);

        return committeeView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), DisplayCommitteeActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Committee Intent", (Serializable) results.get(position));

        intent.putExtras(bundle);
        startActivity(intent);
    }
}
