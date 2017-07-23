package com.firstApp.congressAPI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by shruthihiriyuru on 11/20/16.
 */

public class CommitteeAdapter extends ArrayAdapter<CommitteeData.ResultsBean> {

    private Context context;
    private ArrayList<CommitteeData.ResultsBean> committees;

    public CommitteeAdapter(Context context, ArrayList<CommitteeData.ResultsBean> committees) {
        super(context, R.layout.bill_list, committees);
        this.context = context;
        this.committees = committees;

    }

    @Override
    public int getCount(){
        return committees.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CommitteeData.ResultsBean committee = committees.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.committee_item, parent, false);
        }

        TextView committeeName = (TextView) convertView.findViewById(R.id.committeeText1);
        TextView committeeDetails = (TextView) convertView.findViewById(R.id.committeeText2);

        committeeName.setText(committee.getCommittee_id().toUpperCase());
        committeeDetails.setText("\n" + committee.getName() + "\n\n" + committee.getChamber().substring(0, 1).toUpperCase() + committee.getChamber().substring(1) );

        return convertView;

    }
}