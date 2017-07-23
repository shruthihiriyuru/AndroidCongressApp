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

public class BillFavoriteFragment extends Fragment implements AdapterView.OnItemClickListener{
    View billView;
    private List<BillData.ResultsBean> results;
    private ListView lv;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        billView = inflater.inflate(R.layout.bill_list,null);
        lv = (ListView) billView.findViewById(R.id.billlist);
        SharedPreferences wSharedPreferences = getActivity().getSharedPreferences(
                "BillFavouritesList", getActivity().MODE_APPEND);

        results = new ArrayList<BillData.ResultsBean>();

        Map<String, ?> allEntries = wSharedPreferences.getAll();
        for(Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Gson gson = new Gson();
            String json = (String)entry.getValue();
            BillData.ResultsBean bill = gson.fromJson(json, BillData.ResultsBean.class);
            results.add(bill);
        }

        Collections.sort(results, new Comparator<BillData.ResultsBean>() {
            @Override
            public int compare(BillData.ResultsBean one, BillData.ResultsBean two) {
                return one.getIntroduced_on().compareTo(two.getIntroduced_on());
            }
        });

        Collections.sort(results);
        Collections.reverse(results);

        ArrayList<BillData.ResultsBean> arrayOfBills = new ArrayList<BillData.ResultsBean>(results);

        BillAdapter adapter = new BillAdapter(getActivity(), arrayOfBills);

        // Attach the adapter to a ListView
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(this);

        return billView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), DisplayBillActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Bill Intent", (Serializable) results.get(position));

        intent.putExtras(bundle);
        startActivity(intent);
    }
}
