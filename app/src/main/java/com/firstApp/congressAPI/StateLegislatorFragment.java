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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.io.StringReader;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.util.*;
import android.view.View.OnClickListener;

public class StateLegislatorFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView lv;
    Map<String, Integer> mapIndex;

    // URL to get legislators JSON
    private static String url = "http://sample-env.k7fuibzcdm.us-east-1.elasticbeanstalk.com/?action=legislators";
    private static List<LegislatorData.ResultsBean> results;
    View stateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        stateView = inflater.inflate(R.layout.legislator_list,null);

        lv = (ListView) stateView.findViewById(R.id.list);

        new GetLegislators().execute();
        lv.setOnItemClickListener(this);

        return stateView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), DisplayLegislatorActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Legislator Intent", (Serializable) results.get(position));

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class GetLegislators extends AsyncTask<Void, Void, Void> {

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
                        LegislatorData legData = gson.fromJson(reader, LegislatorData.class);

                        results = legData.getResults();

                    } catch (Exception e) {
                        System.out.println("Json parsing error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Couldn't get json from server.");

                }
            }

            return null;
        }

        private void getIndexList(List<LegislatorData.ResultsBean> legislators) {
            mapIndex = new LinkedHashMap<String, Integer>();
            for (int i = 0; i < legislators.size(); i++) {
                String legState = legislators.get(i).getState_name();
                String index = legState.substring(0, 1);

                if (mapIndex.get(index) == null)
                    mapIndex.put(index, i);
            }
        }


        private void displayIndex() {
            LinearLayout indexLayout = (LinearLayout) stateView.findViewById(R.id.side_index);

            TextView textView;
            List<String> indexList = new ArrayList<String>(mapIndex.keySet());
            for (String index : indexList) {
                textView = (TextView) getActivity().getLayoutInflater().inflate(
                        R.layout.side_index, null);
                textView.setText(index);

                textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView selectedIndex = (TextView) view;
                            lv.setSelection(mapIndex.get(selectedIndex.getText()));
                        }
                });

                indexLayout.addView(textView);

            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ArrayList<LegislatorData.ResultsBean> arrayOfLegislators = new ArrayList<LegislatorData.ResultsBean>(results);
            LegislatorAdapter adapter = new LegislatorAdapter(getActivity(), arrayOfLegislators);

            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            getIndexList(results);
            displayIndex();
        }

    }
}
