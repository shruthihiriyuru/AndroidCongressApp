package com.firstApp.congressAPI;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shruthihiriyuru on 11/20/16.
 */

public class BillAdapter extends ArrayAdapter<BillData.ResultsBean> {

    private Context context;
    private ArrayList<BillData.ResultsBean> bills;

    public BillAdapter(Context context, ArrayList<BillData.ResultsBean> bills) {
        super(context, R.layout.bill_list, bills);
        this.context = context;
        this.bills = bills;

    }

    @Override
    public int getCount(){
        return bills.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        BillData.ResultsBean bill = bills.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bill_item, parent, false);
        }

        TextView billName = (TextView) convertView.findViewById(R.id.billText1);
        TextView billDetails = (TextView) convertView.findViewById(R.id.billText2);

        billName.setText(bill.getBill_id().toUpperCase());

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(bill.getIntroduced_on());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String strDate = sdf.format(date);

        String billTitle = bill.getShort_title() == null?bill.getOfficial_title():bill.getShort_title();
        billDetails.setText(billTitle + "\n\n" + strDate );

        return convertView;

    }
}