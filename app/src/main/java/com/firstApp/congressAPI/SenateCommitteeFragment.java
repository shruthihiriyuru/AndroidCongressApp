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

public class SenateCommitteeFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView lv;
    Map<String, Integer> mapIndex;

    // URL to get committees JSON
    private static String url = "http://sample-env.k7fuibzcdm.us-east-1.elasticbeanstalk.com/?action=committees&type=Senate";
    private static List<CommitteeData.ResultsBean> results;
    View committeeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        committeeView = inflater.inflate(R.layout.committee_list,null);

        lv = (ListView) committeeView.findViewById(R.id.committeelist);

        new GetCommittees().execute();
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

    private class GetCommittees extends AsyncTask<Void, Void, Void> {

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
                        CommitteeData committeeData = gson.fromJson(reader, CommitteeData.class);

                        results = committeeData.getResults();
                        Collections.sort(results, new Comparator<CommitteeData.ResultsBean>() {
                            @Override
                            public int compare(CommitteeData.ResultsBean one, CommitteeData.ResultsBean two) {
                                return one.getName().compareTo(two.getName());
                            }
                        });

                        Collections.sort(results);

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

            ArrayList<CommitteeData.ResultsBean> arrayOfCommittees = new ArrayList<CommitteeData.ResultsBean>(results);

            CommitteeAdapter adapter = new CommitteeAdapter(getActivity(), arrayOfCommittees);

            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
}
