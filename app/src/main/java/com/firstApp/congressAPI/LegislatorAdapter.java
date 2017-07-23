package com.firstApp.congressAPI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by shruthihiriyuru on 11/20/16.
 */

public class LegislatorAdapter extends ArrayAdapter<LegislatorData.ResultsBean> {

    private Context context;
    private ArrayList<LegislatorData.ResultsBean> legislators;

    public LegislatorAdapter(Context context, ArrayList<LegislatorData.ResultsBean> legislators) {
        super(context, R.layout.legislator_list, legislators);
        this.context = context;
        this.legislators = legislators;

    }

    @Override
    public int getCount(){
        return legislators.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LegislatorData.ResultsBean legislator = legislators.get(position);

        String imageURI = "https://theunitedstates.io/images/congress/225x275/" + legislator.getBioguide_id() + ".jpg";
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.legislator_item, parent, false);
        }

        TextView legislatorName = (TextView) convertView.findViewById(R.id.legText1);
        TextView legDetails = (TextView) convertView.findViewById(R.id.legText2);
        ImageView legImage = (ImageView) convertView.findViewById(R.id.legPhoto);

        legislatorName.setText(legislator.getLast_name() + ", " + legislator.getFirst_name());
        legDetails.setText("("+ legislator.getParty() + ")" + legislator.getState_name() + " - District " +legislator.getDistrict() );

        Picasso.with(context).load(imageURI).into(legImage);

        return convertView;

    }
}