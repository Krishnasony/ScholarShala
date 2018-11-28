package com.appteq.ad.appteq;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appteq.ad.appteq.actions.APP_CONSTANT;
import com.appteq.ad.appteq.actions.App_Functions;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.UserModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class NavigationActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    DBHandler mDbhelper;
    App_Functions func;
    UserModel user;
    boolean is_user_payment_valid = false;
    private MenuItem mActiveBottomNavigationViewMenuItem;
    private CircleImageView profile_image;
    private TextView userName;
    private TextView className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        mDbhelper = DBHandler.getHelper(this);
        func = new App_Functions(this);
        user = func.getloggedinuser(mDbhelper);
        is_user_payment_valid = func.is_user_payment_valid(mDbhelper);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView =  navigationView.inflateHeaderView(R.layout.header);
        userName = (TextView)headerView.findViewById(R.id.username);
        className = (TextView)headerView.findViewById(R.id.class_name);
        profile_image = (CircleImageView) headerView.findViewById(R.id.profile_image);
        drawerusersetting();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(NavigationActivity.this, HomeActivity.class));
                        break;

                    case R.id.pyh:
                        startActivity(new Intent(NavigationActivity.this, PaymentHistory.class));
                        break;
                    case R.id.editprof:
                        startActivity(new Intent(NavigationActivity.this, ProfileActivity.class));
                        break;
                    case R.id.subject:
                        startActivity(new Intent(NavigationActivity.this, SubjectActivity.class));
                        break;
                    case R.id.test:
                        startActivity(new Intent(NavigationActivity.this, TestActivity.class));
                        break;
                    case R.id.visit:
                        Uri uri = Uri.parse(APP_CONSTANT.website_url); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;

                    case R.id.l_out:
                        func.user_logout(mDbhelper);
                        startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
                        finish();
                        break;

                    case R.id.share:
                        Intent i=new Intent(android.content.Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(android.content.Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
                        i.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.extra));
                        startActivity(Intent.createChooser(i,"Share via"));
                        break;

                }
                item.setChecked(true);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    public void drawerusersetting(){
        userName.setText(user.getUser_fullname());
        className.setText(user.getUser_class_name());
        if(user.getUserimage()!=null && !user.getUserimage().equalsIgnoreCase("")){
            Picasso.with(this).load(user.getUserimage()).into(profile_image);
        }
    }
}