package com.firstApp.congressAPI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shruthihiriyuru on 11/22/16.
 */

public class DisplayBillActivity extends AppCompatActivity {
    String imgsrc="";
    BillData.ResultsBean bill;
    boolean isFavourite;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_bill_layout);

        Intent intent = getIntent();
        bill =  (BillData.ResultsBean) intent.getSerializableExtra("Bill Intent");

        CheckBox checkBox = (CheckBox)findViewById(R.id.billstarIcon);
        isFavourite = readState(bill.getBill_id());
        checkBox.setChecked(isFavourite);

        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isFavourite) {
                    saveState(false);

                } else {
                    saveState(true);

                }

            }
        });

        Toolbar tb = (Toolbar) findViewById(R.id.billtoolbar);
        tb.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tv1 = (TextView) findViewById(R.id.billID);
        tv1.setText(bill.getBill_id().toUpperCase());

        TextView tv2 = (TextView) findViewById(R.id.billTitle);
        tv2.setText(bill.getShort_title() == null ? bill.getOfficial_title() : bill.getShort_title());

        TextView tv3 = (TextView) findViewById(R.id.billType);
        tv3.setText(bill.getBill_type().toUpperCase());

        TextView tv4 = (TextView) findViewById(R.id.billSponsor);
        tv4.setText(bill.getSponsor().getTitle()+". "+bill.getSponsor().getLast_name()+", "+bill.getSponsor().getFirst_name());

        ImageView iv = (ImageView) findViewById(R.id.billImg);

        if(bill.getChamber().equals("house")){
            imgsrc = "http://cs-server.usc.edu:45678/hw/hw8/images/h.png";
            Picasso.with(getApplicationContext()).load(imgsrc).into(iv);
        }else{
            iv.setImageResource(R.drawable.s);
        }

        TextView tv5 = (TextView) findViewById(R.id.billChamber);
        tv5.setText(bill.getChamber().substring(0, 1).toUpperCase() + bill.getChamber().substring(1));

        TextView tv6 = (TextView) findViewById(R.id.billStatus);
        tv6.setText(bill.getHistory().isActive() ? "Active" : "New");

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(bill.getIntroduced_on());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String strDate = sdf.format(date);

        TextView tv7 = (TextView) findViewById(R.id.billIntroduced);
        tv7.setText(strDate);

        TextView tv8 = (TextView) findViewById(R.id.billCongressURL);
        tv8.setText(bill.getUrls().getCongress());

        TextView tv9 = (TextView) findViewById(R.id.billVersionStatus);
        if (bill.getLast_version() == null)
            tv9.setText("None");
        else
            tv9.setText(bill.getLast_version().getVersion_name());

        TextView tv10 = (TextView) findViewById(R.id.billURL);
        if(bill.getLast_version() == null)
            tv10.setText("None");
        else
            tv10.setText(bill.getLast_version().getUrls().getPdf());
    }

    private void saveState(boolean isFavourite) {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourites", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean(bill.getBill_id(), isFavourite);
        aSharedPreferencesEdit.commit();

        // Write Bill data into favorites
        SharedPreferences wSharedPreferences = this.getSharedPreferences(
                "BillFavouritesList", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor wSharedPreferencesEdit = wSharedPreferences
                .edit();

        if (isFavourite) {
            Gson gson = new Gson();
            String json = gson.toJson(bill);
            wSharedPreferencesEdit.putString(bill.getBill_id(), json);

        }
        else {
            wSharedPreferencesEdit.remove(bill.getBill_id());
        }

        wSharedPreferencesEdit.commit();
    }

    private boolean readState(String billID) {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourites", getApplicationContext().MODE_PRIVATE);
        return aSharedPreferences.getBoolean(billID, false);
    }
}
