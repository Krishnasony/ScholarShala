package com.appteq.ad.appteq;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appteq.ad.appteq.actions.GetTestData;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.ChapterModel;
import com.appteq.ad.appteq.model.ScoreModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private SQLiteDatabase sqLiteDatabase ;
    private Cursor testdata;
    private LinearLayout chapterlayout;
    private TextView chapter_name;
    private RecyclerView scorerec;
   private ArrayList<ScoreModel> sc;
    private AsyncTask mMyTask;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        //TODO: tool bar with back button
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setCollapsedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        collapsingToolbar.setExpandedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        final int chapter_id = getIntent().getIntExtra("chapter_id",0);
        dbHandler = DBHandler.getHelper(this);
        //chapterlayout = findViewById(R.id.scorelayout);
        scorerec = findViewById(R.id.scorerecycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        scorerec.setLayoutManager(llm);
        sc = new ArrayList<ScoreModel>();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        try{
            String sql = "Select * from " + DBHandler.CHAPTER_TABLE + " where "+DBHandler.USER_CHAPTER+"="+chapter_id;
            Log.d("sql",sql);
            Cursor chapter  = dbHandler.getAllData(sql);
            if(chapter!=null && chapter.getCount()>0){
                Log.d("sql",sql);
                chapter.moveToNext();
                ChapterModel model = new ChapterModel();
                model.setChapter_name(chapter.getString(chapter.getColumnIndex(DBHandler.USER_CHAPTER_NAME)));
                collapsingToolbar.setTitle(model.getChapter_name());
                sql = "SELECT tc.*,tt.test_name FROM "+DBHandler.TABLE_SCORE+" tc inner join "+DBHandler.TABLE_TEST+" tt on tc."+DBHandler.SCORE_TEST_ID+" = tt."+DBHandler.COLUMN_TEST_ID+"  WHERE tc.chapter_id =  '"+chapter_id+"' GROUP BY tc.ID";
                testdata = dbHandler.getAllData(sql);
                //Log.d("sql",testdata.getCount()+"");
                if(testdata!=null && testdata.getCount()>0){
                    while(testdata.moveToNext()) {
                        int score = testdata.getInt(testdata.getColumnIndex(DBHandler.SCORE_TEST_SCORE));
                        int total = testdata.getInt(testdata.getColumnIndex(DBHandler.SCORE_QUESTIONS));
                        String testname = testdata.getString(testdata.getColumnIndex(DBHandler.COLUMN_TEST_NAME));
                        // Set the CardView layoutParams
                        ScoreModel scoreobj = new ScoreModel();
                        scoreobj.setTestname(testname);
                        score = (score*100/total);
                        scoreobj.setScore(score);
                        sc.add(scoreobj);
                    }
                    if(testdata!=null){
                        testdata.close();
                    }
                    //startProgress();
                }else{
                   ScoreModel sc1 = new ScoreModel();
                   sc1.setTestname("No score found");
                   sc1.setScore(0);
                   sc.add(sc1);
                }
                ScoreAdapter adapter = new ScoreAdapter(this, sc);

                //setting adapter to recyclerview
                scorerec.setAdapter(adapter);
            }
        }
        catch(Exception ex){

        }
    }
}
