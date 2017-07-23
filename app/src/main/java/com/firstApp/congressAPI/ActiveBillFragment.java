package com.firstApp.congressAPI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.io.StringReader;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.util.*;
import android.view.View.OnClickListener;

public class ActiveBillFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView lv;
    Map<String, Integer> mapIndex;

    // URL to get Bills JSON
    private static String url = "http://sample-env.k7fuibzcdm.us-east-1.elasticbeanstalk.com/?action=bills&type=Active";
    private static List<BillData.ResultsBean> results;
    View billView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        billView = inflater.inflate(R.layout.bill_list,null);

        lv = (ListView) billView.findViewById(R.id.billlist);

        new GetBills().execute();
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

    private class GetBills extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            if (results == null) {
                GetJSONData sh = new GetJSONData();

                String jsonResp = sh.makeHTTPCall(url);

                if (jsonResp != null) {
                    try {

                        Gson gson = new Gson();
                        jsonResp = jsonResp.substring(1, jsonResp.length() - 1);
                        JsonReader reader = new JsonReader(new StringReader(jsonResp));
                        reader.setLenient(true);
                        BillData billData = gson.fromJson(reader, BillData.class);

                        results = billData.getResults();

                    } catch (Exception e) {
                        System.out.println("Json parsing error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Couldn't get json from server.");

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ArrayList<BillData.ResultsBean> arrayOfBills = new ArrayList<BillData.ResultsBean>(results);

            // Create the adapter to convert the array to views
            BillAdapter adapter = new BillAdapter(getActivity(), arrayOfBills);

            // Attach the adapter to a ListView
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
}
