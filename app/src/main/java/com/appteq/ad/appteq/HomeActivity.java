package com.appteq.ad.appteq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.appteq.ad.appteq.actions.App_Functions;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.UserModel;

public class HomeActivity extends NavigationActivity  {
    CardView cardViewpayment,cardViewprofile,cardViewscore,cardViewsubject,cardViewhistory;
    private DBHandler mdbhandler;
    private App_Functions func;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mdbhandler = DBHandler.getHelper(this);
        func = new App_Functions(this);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_home, contentFrameLayout);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("HOME");
        collapsingToolbar.setCollapsedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        collapsingToolbar.setExpandedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        //cardview initialization
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        cardViewprofile = findViewById(R.id.profilecard);
        cardViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomeActivity.this,ProfileActivity.class)));
            }
        });

        cardViewsubject = findViewById(R.id.subjectcard);
        cardViewsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomeActivity.this,SubjectActivity.class)));
            }
        });

        cardViewpayment = findViewById(R.id.paymentcard);
        cardViewpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomeActivity.this,PaymentActivity.class)));
            }
        });

        /*cardViewscore = findViewById(R.id.scorecard);
        cardViewscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomeActivity.this,ScoreActivity.class)));
            }
        });*/

        cardViewhistory = findViewById(R.id.historycard);
        cardViewhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomeActivity.this,PaymentHistory.class)));
            }
        });
    }
}