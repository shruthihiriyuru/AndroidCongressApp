package com.firstApp.congressAPI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shruthihiriyuru on 11/22/16.
 */

public class DisplayLegislatorActivity extends AppCompatActivity {
    LegislatorData.ResultsBean legislator;
    boolean isFavourite;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_legislator_layout);
        long totalterm = 0;
        long currentelapsed = 0;

        Intent intent = getIntent();
        legislator =  (LegislatorData.ResultsBean) intent.getSerializableExtra("Legislator Intent");

        CheckBox checkBox = (CheckBox)findViewById(R.id.legstarIcon);
        isFavourite = readState(legislator.getBioguide_id());
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

        Toolbar tb = (Toolbar) findViewById(R.id.legtoolbar);
        tb.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView fbImg = (ImageView) findViewById(R.id.legFBIcon);
        fbImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (legislator.getFacebook_id() == null) {
                    Context context = getApplicationContext();
                    CharSequence text = "Legislator does not have a Facebook page!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http:facebook.com/" + legislator.getFacebook_id()));
                    startActivity(intent);
                }
            }
        });

        ImageView twImg = (ImageView)findViewById(R.id.legTWIcon);
        twImg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(legislator.getTwitter_id() == null) {
                    Context context = getApplicationContext();
                    CharSequence text = "Legislator does not have a Twitter page!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http:twitter.com/" + legislator.getTwitter_id()));
                    startActivity(intent);
                }
            }
        });

        ImageView webImg = (ImageView)findViewById(R.id.legWebIcon);
        webImg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(legislator.getWebsite() == null) {
                    Context context = getApplicationContext();
                    CharSequence text = "Legislator does not have a Website!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(legislator.getWebsite()));
                    startActivity(intent);
                }
            }
        });

        ImageView legImage = (ImageView) findViewById(R.id.legImg);
        String legislatorImageURI = "https://theunitedstates.io/images/congress/225x275/" + legislator.getBioguide_id() + ".jpg";
        Picasso.with(getApplicationContext()).load(legislatorImageURI).into(legImage);

        ImageView legPartyImage = (ImageView) findViewById(R.id.legPartyImg);
        String partyImgURI = legislator.getParty().equals("R")?"http://cs-server.usc.edu:45678/hw/hw8/images/r.png":"http://cs-server.usc.edu:45678/hw/hw8/images/d.png";
        Picasso.with(getApplicationContext()).load(partyImgURI).into(legPartyImage);

        TextView tv = (TextView) findViewById(R.id.legislatorParty);
        tv.setText(legislator.getParty().equals("R")?"Republican":"Democrat");

        TextView tv1 = (TextView) findViewById(R.id.legislatorName);
        tv1.setText(legislator.getTitle()+". "+legislator.getLast_name()+", "+legislator.getFirst_name());

        TextView tv2 = (TextView) findViewById(R.id.legislatorEmail);
        tv2.setText(legislator.getOc_email() == null?"N.A":legislator.getOc_email());

        TextView tv3 = (TextView) findViewById(R.id.legislatorChamber);
        tv3.setText(legislator.getChamber().substring(0,1).toUpperCase() + legislator.getChamber().substring(1));

        TextView tv4 = (TextView) findViewById(R.id.legislatorContact);
        tv4.setText(legislator.getPhone() == null?"N.A":legislator.getPhone());

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startdate = null;
        Date endDate = null;
        try {
            startdate = formatter.parse(legislator.getTerm_start());
            endDate = formatter.parse(legislator.getTerm_end());

            totalterm = endDate.getTime() - startdate.getTime();
            Date today = new Date();
            currentelapsed = today.getTime() - startdate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String startTerm = sdf.format(startdate);
        String endTerm = sdf.format(endDate);

        TextView tv5 = (TextView) findViewById(R.id.legislatorStartTerm);
        tv5.setText(startTerm);

        TextView tv6 = (TextView) findViewById(R.id.legislatorEndTerm);
        tv6.setText(endTerm);

        ProgressBar pb = (ProgressBar) findViewById(R.id.legislatorTerm);
        long progress = (long)((float)currentelapsed / totalterm * 100) ;
        pb.setProgress((int)progress);

        TextView progressBarText = (TextView) findViewById(R.id.progressBarinsideText);
        progressBarText.setText(progress+"%");

        TextView tv8 = (TextView) findViewById(R.id.legislatorOffice);
        tv8.setText(legislator.getOffice()==null?"N.A":legislator.getOffice());

        TextView tv9 = (TextView) findViewById(R.id.legislatorState);
        tv9.setText(legislator.getState());

        TextView tv10 = (TextView) findViewById(R.id.legislatorFax);
        tv10.setText(legislator.getFax() == null?"N.A":legislator.getFax());

        TextView tv11 = (TextView) findViewById(R.id.legislatorBirthday);
        tv11.setText(legislator.getBirthday());
    }

    private void saveState(boolean isFavourite) {

        // Write legislator data into favorites
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourites", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean(legislator.getBioguide_id(), isFavourite);
        aSharedPreferencesEdit.commit();

        SharedPreferences wSharedPreferences = this.getSharedPreferences(
                "LegislatorFavouritesList", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor wSharedPreferencesEdit = wSharedPreferences
                .edit();

        if (isFavourite) {
            Gson gson = new Gson();
            String json = gson.toJson(legislator);
            wSharedPreferencesEdit.putString(legislator.getBioguide_id(), json);

        }
        else {
            wSharedPreferencesEdit.remove(legislator.getBioguide_id());
        }

        wSharedPreferencesEdit.commit();
    }

    private boolean readState(String legislatorID) {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourites", getApplicationContext().MODE_PRIVATE);
        return aSharedPreferences.getBoolean(legislatorID, false);
    }
}