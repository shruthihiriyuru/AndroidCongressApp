package com.firstApp.congressAPI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * Created by shruthihiriyuru on 11/22/16.
 */

public class DisplayCommitteeActivity extends AppCompatActivity {
    String imgsrc = "";
    CommitteeData.ResultsBean committee;
    boolean isFavourite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_committee_layout);

        Intent intent = getIntent();
        committee =  (CommitteeData.ResultsBean) intent.getSerializableExtra("Committee Intent");

        CheckBox checkBox = (CheckBox)findViewById(R.id.comstarIcon);
        isFavourite = readState(committee.getCommittee_id());
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

        Toolbar tb = (Toolbar) findViewById(R.id.comtoolbar);
        tb.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tv1 = (TextView) findViewById(R.id.committeeID);
        tv1.setText(committee.getCommittee_id());

        TextView tv2 = (TextView) findViewById(R.id.committeeName);
        tv2.setText(committee.getName());

        ImageView iv = (ImageView) findViewById(R.id.committeeImg);

        if(committee.getChamber().equals("house")){
            imgsrc = "http://cs-server.usc.edu:45678/hw/hw8/images/h.png";
            Picasso.with(getApplicationContext()).load(imgsrc).into(iv);
        }else{
            iv.setImageResource(R.drawable.s);
        }

        TextView tv3 = (TextView) findViewById(R.id.committeeChamber);
        tv3.setText(committee.getChamber().substring(0, 1).toUpperCase() + committee.getChamber().substring(1));

        TextView tv4 = (TextView) findViewById(R.id.committeePC);
        tv4.setText(committee.getParent_committee_id() == null ? "N.A" : committee.getParent_committee_id());

        TextView tv5 = (TextView) findViewById(R.id.committeeContact);
        tv5.setText(committee.getPhone() == null ? "N.A" : committee.getPhone());

        TextView tv6 = (TextView) findViewById(R.id.committeeOffice);
        tv6.setText(committee.getOffice() == null ? "N.A" : committee.getOffice());
    }

    private void saveState(boolean isFavourite) {

        // Write committee data into favorites
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourites", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean(committee.getCommittee_id(), isFavourite);
        aSharedPreferencesEdit.commit();

        SharedPreferences wSharedPreferences = this.getSharedPreferences(
                "CommitteeFavouritesList", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor wSharedPreferencesEdit = wSharedPreferences
                .edit();

        if (isFavourite) {
            Gson gson = new Gson();
            String json = gson.toJson(committee); // myObject - instance of MyObject
            wSharedPreferencesEdit.putString(committee.getCommittee_id(), json);

        }
        else {
            wSharedPreferencesEdit.remove(committee.getCommittee_id());
        }

        wSharedPreferencesEdit.commit();


    }

    private boolean readState(String committeeID) {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourites", getApplicationContext().MODE_PRIVATE);
        return aSharedPreferences.getBoolean(committeeID, false);
    }
}
