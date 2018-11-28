package com.appteq.ad.appteq;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.ChapterModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChapterActivity extends AppCompatActivity {
    private TextView subjectname;
    private ImageView image;
    private int test_id = 0;
    private ArrayList<ChapterModel> chatpers;
    private RecyclerView chapterrec;
    private LinearLayout card_layout;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChapterActivity.this,SubjectActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        //TODO: tool bar with back button
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        loadBackDrop(collapsingToolbar);
        collapsingToolbar.setCollapsedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        collapsingToolbar.setExpandedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        chapterrec = findViewById(R.id.chapterrecycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        chapterrec.setLayoutManager(llm);
        DBHandler dbHandler = new DBHandler(this);
        chatpers = new ArrayList<ChapterModel>();
        Bundle bundle = getIntent().getBundleExtra("subjectdata");
        //image = findViewById(R.id.chapter_image_icon);
        //card_layout = findViewById(R.id.chapter_cardLayout);
        int subject_id = bundle.getInt("subject_id");
        String subject_name = bundle.getString("subject_name");
        subject_name = subject_name.substring(0,1).toUpperCase()+subject_name.substring(1)+"'s chapter";
        final String finalSubject_name = subject_name;
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    setToolBarColor(toolbar);
                }
                else{
                    toolbar.setBackground(null);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChapterActivity.this,SubjectActivity.class));
            }
        });
        //subjectname = (TextView)findViewById(R.id.subject_name);
        //subjectname.setText(subject_name);
        DBHandler mdbhandler = new DBHandler(this);
        String sql = "select * from "+DBHandler.CHAPTER_TABLE+" where subject_id="+subject_id;
        Cursor chaptercursor = mdbhandler.getAllData(sql);
        if(chaptercursor!=null){
            int subc =0;
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            while(chaptercursor.moveToNext()){
                ChapterModel chapter = new ChapterModel();
                boolean has_test = false;
                int chapterid = chaptercursor.getInt(chaptercursor.getColumnIndex(DBHandler.USER_CHAPTER));
                String chaptername = chaptercursor.getString(chaptercursor.getColumnIndex(DBHandler.USER_CHAPTER_NAME));
                chapter.setChapter_id(chapterid);
                chapter.setChapter_name(chaptername);
                sql = "select * from "+DBHandler.TABLE_TEST+" where chapter_id="+chapterid+" and is_active=1 order by "+DBHandler.COLUMN_TEST_ID+" DESC LIMIT 1";
                Cursor testc =  mdbhandler.getAllData(sql);
                Date testenddate1 = null;
                if(testc!=null && testc.getCount()>0){
                    testc.moveToNext();
                    String testenddate =  testc.getString(testc.getColumnIndex(DBHandler.COLUMN_END_TIME));
                    try {
                        testenddate1 = format.parse(testenddate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    test_id = testc.getInt(testc.getColumnIndex(DBHandler.COLUMN_TEST_ID));
                    if(test_id > 0 && testenddate1.getTime() > today.getTime()){
                        chapter.setTest_id(test_id);
                    }
                }
                chatpers.add(chapter);
                subc++;
            }
            chaptercursor.close();
        }else{
            ChapterModel chapter = new ChapterModel();
            chapter.setChapter_name("No chapter found. Please sync app");
        }
        ChapterAdapter adapter = new ChapterAdapter(this, chatpers,bundle);

        //setting adapter to recyclerview
        chapterrec.setAdapter(adapter);
    }

    private void setToolBarColor(Toolbar toolbar) {
        Bundle bundle = getIntent().getBundleExtra("subjectdata");
        String subject_name = bundle.getString("subject_name");
        if (subject_name.equalsIgnoreCase("mathematics")){
            toolbar.setBackgroundResource(R.color.card_1);
        }
        if (subject_name.equalsIgnoreCase("social science")){
            toolbar.setBackgroundResource(R.color.card_2);
        }
        if (subject_name.equalsIgnoreCase("science")){
            toolbar.setBackgroundResource(R.color.card_3);
        }
        if (subject_name.equalsIgnoreCase("english")){
            toolbar.setBackgroundResource(R.color.card_5);
        }
        if (subject_name.equalsIgnoreCase("hindi")){
            toolbar.setBackgroundResource(R.color.card_4);
        }
        if (subject_name.equalsIgnoreCase("physics")){
            toolbar.setBackgroundResource(R.color.card_6);
        }
        if (subject_name.equalsIgnoreCase("chemistry")){
            toolbar.setBackgroundResource(R.color.card_7);
        }
        if (subject_name.equalsIgnoreCase("biology")){
            toolbar.setBackgroundResource(R.color.card_8);
        }
    }

    private void loadBackDrop(CollapsingToolbarLayout collapsingToolbar) {
        Bundle bundle = getIntent().getBundleExtra("subjectdata");
        String subject_name = bundle.getString("subject_name");
        if (subject_name.equalsIgnoreCase("science")){
            final ImageView imageView = findViewById(R.id.subject_image);
            Glide.with(this).load(R.drawable.science_card).apply(RequestOptions.centerCropTransform()).into(imageView);
            collapsingToolbar.setTitle(subject_name);
        }
        if (subject_name.equalsIgnoreCase("mathematics")){
            final ImageView imageView = findViewById(R.id.subject_image);
            Glide.with(this).load(R.drawable.maths_card).apply(RequestOptions.centerCropTransform()).into(imageView);
            collapsingToolbar.setTitle(subject_name);
        }
        if (subject_name.equalsIgnoreCase("social science")){
            final ImageView imageView = findViewById(R.id.subject_image);
            Glide.with(this).load(R.drawable.social_science_card).apply(RequestOptions.centerCropTransform()).into(imageView);
            collapsingToolbar.setTitle(subject_name);
        }
        if (subject_name.equalsIgnoreCase("science")){
            final ImageView imageView = findViewById(R.id.subject_image);
            Glide.with(this).load(R.drawable.science_card).apply(RequestOptions.centerCropTransform()).into(imageView);
            collapsingToolbar.setTitle(subject_name);
        }
        if (subject_name.equalsIgnoreCase("english")){
            final ImageView imageView = findViewById(R.id.subject_image);
            Glide.with(this).load(R.drawable.english_card).apply(RequestOptions.centerCropTransform()).into(imageView);
            collapsingToolbar.setTitle(subject_name);
        }
        if (subject_name.equalsIgnoreCase("hindi")){
            final ImageView imageView = findViewById(R.id.subject_image);
            Glide.with(this).load(R.drawable.hindi_card).apply(RequestOptions.centerCropTransform()).into(imageView);
            collapsingToolbar.setTitle(subject_name);
        }
        if (subject_name.equalsIgnoreCase("physics")){
            final ImageView imageView = findViewById(R.id.subject_image);
            Glide.with(this).load(R.drawable.physics_card).apply(RequestOptions.centerCropTransform()).into(imageView);
            collapsingToolbar.setTitle(subject_name);
        }
        if (subject_name.equalsIgnoreCase("chemistry")){
            final ImageView imageView = findViewById(R.id.subject_image);
            Glide.with(this).load(R.drawable.chemistry_card).apply(RequestOptions.centerCropTransform()).into(imageView);
            collapsingToolbar.setTitle(subject_name);
        }
        if (subject_name.equalsIgnoreCase("biology")){
            final ImageView imageView = findViewById(R.id.subject_image);
            Glide.with(this).load(R.drawable.science_card).apply(RequestOptions.centerCropTransform()).into(imageView);
            collapsingToolbar.setTitle(subject_name);
        }
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.colorwhite));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorwhite));

    }

}
